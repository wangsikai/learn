package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaper;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaperDifficulty;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaperQuestion;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartPaperStatus;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseWeakKnowpointStat;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.PracticeHistoryService;
import com.lanking.uxb.service.zuoye.api.ZySmartPaperService;
import com.lanking.uxb.service.zuoye.form.PaperPullForm;
import com.lanking.uxb.service.zuoye.form.PracticeHistoryForm;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;

@Transactional(readOnly = true)
@Service
public class ZySmartPaperServiceImpl implements ZySmartPaperService {

	@Autowired
	@Qualifier("SmartExamPaperRepo")
	Repo<SmartExamPaper, Long> smartExamPaperRepo;
	@Autowired
	@Qualifier("SmartExamPaperQuestionRepo")
	Repo<SmartExamPaperQuestion, Long> smartExamPaperQuestionRepo;
	@Autowired
	@Qualifier("StudentExerciseWeakKnowpointStatRepo")
	Repo<StudentExerciseWeakKnowpointStat, Long> weakStatRepo;

	@Autowired
	private SearchService searchService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private PracticeHistoryService practiceHistoryService;
	@Autowired
	private MqSender mqSender;

	@Transactional
	@Override
	public List<SmartExamPaperQuestion> savePaper(PaperPullForm form) {
		String textBookName = textbookService.get(form.getTextBookCode()).getName();
		SmartExamPaper smartExamPaper = new SmartExamPaper();
		smartExamPaper.setCreateAt(new Date());
		smartExamPaper.setName(String.format(form.getSmartExamPaperDifficulty().getTitle(), textBookName));
		smartExamPaper.setUserId(form.getUserId());
		smartExamPaper.setSmartExamPaperDifficulty(form.getSmartExamPaperDifficulty());
		smartExamPaper.setTextbookCode(form.getTextBookCode());
		smartExamPaper.setQuestionCount(form.getqIds().size());
		List<Question> questionList = questionService.mgetList(form.getqIds());
		if (questionList.size() > 0) {
			Double totalDiff = 0.0;
			for (Question q : questionList) {
				totalDiff += q.getDifficulty();
			}
			double f = totalDiff / questionList.size();
			DecimalFormat df = new DecimalFormat("#.00");
			// 平均难度
			smartExamPaper.setDifficulty(Double.parseDouble(df.format(f)));
		}
		smartExamPaperRepo.save(smartExamPaper);
		List<SmartExamPaperQuestion> list = new ArrayList<SmartExamPaperQuestion>();
		if (form.getqIds().size() > 0) {
			for (Long questionId : form.getqIds()) {
				SmartExamPaperQuestion smartExamPaperQuestion = new SmartExamPaperQuestion();
				smartExamPaperQuestion.setPaperId(smartExamPaper.getId());
				smartExamPaperQuestion.setQuestionId(questionId);
				smartExamPaperQuestionRepo.save(smartExamPaperQuestion);
				list.add(smartExamPaperQuestion);
			}
		}
		return list;
	}

	@Override
	public List<SmartExamPaperQuestion> queryPaperQuestion(PaperPullForm paperPullForm) {
		Params params = Params.param();
		params.put("userId", paperPullForm.getUserId());
		params.put("textBookCode", paperPullForm.getTextBookCode());
		params.put("smartDifficulty", paperPullForm.getSmartExamPaperDifficulty().getValue());
		params.put("status", SmartPaperStatus.NEWEST.getValue());
		// 获取对应的试卷编号
		SmartExamPaper sq = smartExamPaperRepo.find("$getPaper", params).get();
		if (sq == null) {
			return null;
		}
		return queryPaperQuestion(sq.getId());
	}

	@Transactional
	@Override
	public VUserReward savePaperResult(VExerciseResult result, int rightCount, long paperId,
			List<Map<Long, List<String>>> answerList, Integer homeworkTime, long userId) {
		// 获取试卷
		SmartExamPaper smartExamPaper = smartExamPaperRepo.get(paperId);
		List<SmartExamPaperQuestion> list = queryPaperQuestion(paperId);
		// 1. (a)表示第一次做这套试卷 ----(b)之前保存过没有提交(没有提交时间) 不需要备份
		if (smartExamPaper.getStatus() == SmartPaperStatus.NEWEST || smartExamPaper.getCommitAt() == null) {
			smartExamPaper.setRightCount(rightCount);
			smartExamPaper.setWrongCount(smartExamPaper.getQuestionCount() - rightCount);
			smartExamPaper.setRightRate(result.getRightRate());
			smartExamPaper.setCommitAt(new Date());
			// 作业提交，更新为历史试卷
			smartExamPaper.setStatus(SmartPaperStatus.PREVIOUS);
			smartExamPaper.setHomeworkTime(homeworkTime);
			smartExamPaperRepo.save(smartExamPaper);
			PracticeHistoryForm p = new PracticeHistoryForm();
			p.setCreateAt(smartExamPaper.getCommitAt());
			p.setBiz(Biz.SMART_PAPER);
			p.setBizId(smartExamPaper.getId());
			p.setUserId(smartExamPaper.getUserId());
			p.setRightRate(result.getRightRate());
			p.setName(smartExamPaper.getName());
			p.setCreateAt(smartExamPaper.getCreateAt());
			p.setCompletionRate(BigDecimal.valueOf(100));
			practiceHistoryService.updateHistory(p);
		} // 2.表示第二次做，重新练习
		else if (smartExamPaper.getStatus() != SmartPaperStatus.NEWEST) {
			// 获取老试卷的数据
			SmartExamPaper oldSmartExamPaper = smartExamPaper;
			// 给老的复制一套
			SmartExamPaper newSmartExamPaper = new SmartExamPaper();
			newSmartExamPaper.setCreateAt(oldSmartExamPaper.getCreateAt());
			newSmartExamPaper.setName(oldSmartExamPaper.getName());
			newSmartExamPaper.setUserId(oldSmartExamPaper.getUserId());
			newSmartExamPaper.setSmartExamPaperDifficulty(oldSmartExamPaper.getSmartExamPaperDifficulty());
			newSmartExamPaper.setTextbookCode(oldSmartExamPaper.getTextbookCode());
			newSmartExamPaper.setQuestionCount(oldSmartExamPaper.getQuestionCount());
			newSmartExamPaper.setHomeworkTime(oldSmartExamPaper.getHomeworkTime());
			newSmartExamPaper.setDifficulty(oldSmartExamPaper.getDifficulty());
			// 把新的paperId指向老的
			if (oldSmartExamPaper.getPaperId() == 0) {
				newSmartExamPaper.setPaperId(oldSmartExamPaper.getId());
			} else {
				newSmartExamPaper.setPaperId(oldSmartExamPaper.getPaperId());
			}
			newSmartExamPaper.setCommitAt(oldSmartExamPaper.getCommitAt());
			newSmartExamPaper.setStatus(SmartPaperStatus.PREVIOUS);
			newSmartExamPaper.setRightRate(oldSmartExamPaper.getRightRate());
			newSmartExamPaper.setRightCount(oldSmartExamPaper.getRightCount());
			newSmartExamPaper.setWrongCount(oldSmartExamPaper.getWrongCount());
			smartExamPaperRepo.save(newSmartExamPaper);
			// 清空老的重新保存新的数据
			oldSmartExamPaper.setCreateAt(new Date());
			oldSmartExamPaper.setRightCount(rightCount);
			oldSmartExamPaper.setWrongCount(smartExamPaper.getQuestionCount() - rightCount);
			oldSmartExamPaper.setRightRate(result.getRightRate());
			oldSmartExamPaper.setCommitAt(new Date());
			oldSmartExamPaper.setStatus(SmartPaperStatus.PREVIOUS);
			oldSmartExamPaper.setHomeworkTime(homeworkTime);
			smartExamPaperRepo.save(oldSmartExamPaper);
			// 获取老试卷对应的题目,备份
			for (SmartExamPaperQuestion sq : list) {
				SmartExamPaperQuestion smartExamPaperQuestion = new SmartExamPaperQuestion();
				smartExamPaperQuestion.setPaperId(newSmartExamPaper.getId());
				smartExamPaperQuestion.setQuestionId(sq.getQuestionId());
				smartExamPaperQuestion.setAnswer(sq.getAnswer());
				smartExamPaperQuestion.setResult(sq.getResult());
				smartExamPaperQuestion.setDone(sq.isDone());
				smartExamPaperQuestionRepo.save(smartExamPaperQuestion);
			}
			// 老的清空，存新的数据
			PracticeHistoryForm p = new PracticeHistoryForm();
			p.setCreateAt(oldSmartExamPaper.getCreateAt());
			p.setBiz(Biz.SMART_PAPER);
			p.setBizId(oldSmartExamPaper.getId());
			p.setUserId(oldSmartExamPaper.getUserId());
			p.setRightRate(oldSmartExamPaper.getRightRate());
			p.setName(oldSmartExamPaper.getName());
			p.setCreateAt(oldSmartExamPaper.getCreateAt());
			p.setCompletionRate(BigDecimal.valueOf(100));
			practiceHistoryService.updateHistory(p);
			// 被备份的
			PracticeHistoryForm p1 = new PracticeHistoryForm();
			p1.setBiz(Biz.SMART_PAPER);
			p1.setBizId(newSmartExamPaper.getId());
			p1.setUserId(newSmartExamPaper.getUserId());
			p1.setRightRate(newSmartExamPaper.getRightRate());
			p1.setName(newSmartExamPaper.getName());
			p1.setCreateAt(newSmartExamPaper.getCreateAt());
			p1.setCompletionRate(BigDecimal.valueOf(100));
			practiceHistoryService.updateHistory(p1);
		}
		Map<Long, SmartExamPaperQuestion> map = new HashMap<Long, SmartExamPaperQuestion>(list.size());
		for (SmartExamPaperQuestion sq : list) {
			map.put(sq.getQuestionId(), sq);
		}

		/*
		 * GrowthLog growthLog = null; CoinsLog coinsLog = null; VUserReward
		 * vUserReward = null; UserHonor userHonor = null; int getGrowth = 0;
		 * int getCoins = 0;
		 */
		int doneCount = 0;
		for (int i = 0; i < result.getqIds().size(); i++) {
			SmartExamPaperQuestion sq = map.get(result.getqIds().get(i));
			sq.setAnswer(answerList.get(i));
			sq.setDone(result.getDones().get(i));
			// 如果题目已做，需要入给予金币和成长值
			/*
			 * if (result.getDones().get(i)) { growthLog =
			 * growthService.grow(GrowthAction.DOING_DAILY_EXERCISE, userId,
			 * true); coinsLog =
			 * coinsService.earn(CoinsAction.DOING_DAILY_EXERCISE, userId); //
			 * 为空表示做题已经达到每天最高值 if (growthLog.getHonor() != null) { getGrowth++;
			 * getCoins++; userHonor = growthLog.getHonor(); } }
			 */
			doneCount = result.getDones().get(i) ? doneCount + 1 : doneCount;
			sq.setResult(result.getResults().get(i));
			smartExamPaperQuestionRepo.save(sq);
		}
		if (doneCount > 0) {
			JSONObject messageObj = new JSONObject();
			messageObj.put("taskCode", 101010006);
			messageObj.put("userId", userId);
			Map<String, Object> params = new HashMap<String, Object>(1);
			params.put("rightRate", result.getRightRate().intValue());
			messageObj.put("params", params);
			messageObj.put("isClient", Security.isClient());
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
					MQ.builder().data(messageObj).build());
		}
		/*
		 * if (userHonor != null) { vUserReward = new
		 * VUserReward(userHonor.getUpRewardCoins(), userHonor.isUpgrade(),
		 * userHonor.getLevel(), getGrowth, getCoins); }
		 */
		return null;

	}

	@Transactional
	@Override
	public void updateStatus(long studentId, int textbookCode) {
		List<Integer> vals = new ArrayList<Integer>();
		for (SmartExamPaperDifficulty sd : SmartExamPaperDifficulty.values()) {
			vals.add(sd.getValue());
		}
		Params params = Params.param();
		params.put("userId", studentId);
		params.put("textBookCode", textbookCode);
		params.put("smartDifficultys", vals);
		params.put("status", SmartPaperStatus.PREVIOUS.getValue());
		smartExamPaperRepo.execute("$updatePaperStatus", params);
	}

	@Override
	public CursorPage<Long, SmartExamPaper> queryHistoryPaperList(long studentId, int textbookCode,
			CursorPageable<Long> cpr) {
		Params params = Params.param();
		params.put("userId", studentId);
		params.put("textBookCode", textbookCode);
		params.put("status", SmartPaperStatus.PREVIOUS.getValue());
		return smartExamPaperRepo.find("$queryHistoryPaperList", params).fetch(cpr);
	}

	@Override
	public BigDecimal getHistoryPaperAvg(long studentId, int textbookCode) {
		Params params = Params.param();
		params.put("userId", studentId);
		params.put("textBookCode", textbookCode);
		params.put("status", SmartPaperStatus.PREVIOUS.getValue());
		Double avg = smartExamPaperRepo.find("$getHistoryPaperAvg", params).get(Double.class);
		return avg == null ? BigDecimal.valueOf(0) : BigDecimal.valueOf(avg);
	}

	@Override
	public List<SmartExamPaperQuestion> queryPaperQuestion(Long paperId) {
		return smartExamPaperQuestionRepo.find("$queryPaperQuestions", Params.param("paperId", paperId)).list();
	}

	@Override
	public Long getHistoryPaperCount(long studentId, int textbookCode) {
		Params params = Params.param("userId", studentId);
		params.put("textBookCode", textbookCode);
		params.put("status", SmartPaperStatus.PREVIOUS.getValue());
		return smartExamPaperRepo.find("$getHistoryPaperCount", params).count();
	}

	@Override
	public List<Long> queryWeakKnowpoints(long studentId, Integer phaseCode, int count) {
		Params params = Params.param("studentId", studentId);
		params.put("phaseCode", phaseCode);
		params.put("count1", count);

		return weakStatRepo.find("$queryWeakKnowpoints", params).list(Long.class);
	}

	@Transactional
	@Override
	public void rePractice(Long paperId) {
		SmartExamPaper smartExamPaper = smartExamPaperRepo.get(paperId);
		smartExamPaper.setStatus(SmartPaperStatus.DELETED);
		smartExamPaperRepo.save(smartExamPaper);

	}

	@Transactional
	@Override
	public void saveNotCommitPaper(long paperId, List<Map<Long, List<String>>> answerList, List<Long> qIds,
			Integer homeworkTime) {
		SmartExamPaper smartExamPaper = smartExamPaperRepo.get(paperId);
		List<SmartExamPaperQuestion> list = queryPaperQuestion(paperId);
		PracticeHistoryForm p2 = new PracticeHistoryForm();
		// 如果是从最新试卷进去做的保存的
		if (smartExamPaper.getStatus() == SmartPaperStatus.NEWEST
				|| smartExamPaper.getStatus() == SmartPaperStatus.HIDE) {
			smartExamPaper.setHomeworkTime(homeworkTime);
			smartExamPaperRepo.save(smartExamPaper);
		} else {
			// 如果是从往期回顾进来的，肯定是点重新练进来的，需要备份原来的试卷
			// 获取老试卷的数据
			SmartExamPaper oldSmartExamPaper = smartExamPaper;
			// 给老的复制一套
			SmartExamPaper newSmartExamPaper = new SmartExamPaper();
			newSmartExamPaper.setCreateAt(oldSmartExamPaper.getCreateAt());
			newSmartExamPaper.setName(oldSmartExamPaper.getName());
			newSmartExamPaper.setUserId(oldSmartExamPaper.getUserId());
			newSmartExamPaper.setSmartExamPaperDifficulty(oldSmartExamPaper.getSmartExamPaperDifficulty());
			newSmartExamPaper.setTextbookCode(oldSmartExamPaper.getTextbookCode());
			newSmartExamPaper.setQuestionCount(oldSmartExamPaper.getQuestionCount());
			newSmartExamPaper.setHomeworkTime(oldSmartExamPaper.getHomeworkTime());
			newSmartExamPaper.setDifficulty(oldSmartExamPaper.getDifficulty());
			// 把新的paperId指向老的
			if (oldSmartExamPaper.getPaperId() == 0) {
				newSmartExamPaper.setPaperId(oldSmartExamPaper.getId());
			} else {
				newSmartExamPaper.setPaperId(oldSmartExamPaper.getPaperId());
			}
			newSmartExamPaper.setCommitAt(oldSmartExamPaper.getCommitAt());
			newSmartExamPaper.setStatus(SmartPaperStatus.PREVIOUS);
			newSmartExamPaper.setRightRate(oldSmartExamPaper.getRightRate());
			newSmartExamPaper.setRightCount(oldSmartExamPaper.getRightCount());
			newSmartExamPaper.setWrongCount(oldSmartExamPaper.getWrongCount());
			smartExamPaperRepo.save(newSmartExamPaper);
			// 清空老的重新保存新的数据
			oldSmartExamPaper.setCreateAt(new Date());
			oldSmartExamPaper.setRightCount(null);
			oldSmartExamPaper.setWrongCount(null);
			oldSmartExamPaper.setCommitAt(null);
			oldSmartExamPaper.setRightRate(null);
			// 这个试卷只会在历史作业里面显示，不会在往期回顾里面,更改状态
			oldSmartExamPaper.setStatus(SmartPaperStatus.HIDE);
			oldSmartExamPaper.setHomeworkTime(homeworkTime);
			smartExamPaperRepo.save(oldSmartExamPaper);
			// 获取老试卷对应的题目,备份
			for (SmartExamPaperQuestion sq : list) {
				SmartExamPaperQuestion smartExamPaperQuestion = new SmartExamPaperQuestion();
				smartExamPaperQuestion.setPaperId(newSmartExamPaper.getId());
				smartExamPaperQuestion.setQuestionId(sq.getQuestionId());
				smartExamPaperQuestion.setAnswer(sq.getAnswer());
				smartExamPaperQuestion.setResult(sq.getResult());
				smartExamPaperQuestion.setDone(sq.isDone());
				smartExamPaperQuestionRepo.save(smartExamPaperQuestion);
			}
			// 老的清空，存新的数据
			p2.setBiz(Biz.SMART_PAPER);
			p2.setBizId(oldSmartExamPaper.getId());
			p2.setUserId(oldSmartExamPaper.getUserId());
			p2.setName(oldSmartExamPaper.getName());
			p2.setCreateAt(oldSmartExamPaper.getCreateAt());
			// 被备份的
			PracticeHistoryForm p1 = new PracticeHistoryForm();
			p1.setBiz(Biz.SMART_PAPER);
			p1.setBizId(newSmartExamPaper.getId());
			p1.setUserId(newSmartExamPaper.getUserId());
			p1.setRightRate(newSmartExamPaper.getRightRate());
			p1.setName(newSmartExamPaper.getName());
			p1.setCreateAt(newSmartExamPaper.getCreateAt());
			p1.setCompletionRate(BigDecimal.valueOf(100));
			practiceHistoryService.updateHistory(p1);
		}
		Map<Long, SmartExamPaperQuestion> map = new HashMap<Long, SmartExamPaperQuestion>(list.size());
		for (SmartExamPaperQuestion sq : list) {
			map.put(sq.getQuestionId(), sq);
		}
		List<Boolean> dones = new ArrayList<Boolean>();
		for (int i = 0; i < qIds.size(); i++) {
			SmartExamPaperQuestion sq = map.get(qIds.get(i));
			sq.setAnswer(answerList.get(i));
			sq.setDone(CollectionUtils.isEmpty(answerList.get(i).get(String.valueOf(qIds.get(i)))) ? false : true);
			dones.add(sq.isDone());
			sq.setResult(null);
			smartExamPaperQuestionRepo.save(sq);
		}
		// hide 说明是从练习历史进去的保存未提交的
		if (smartExamPaper.getStatus() == SmartPaperStatus.NEWEST
				|| smartExamPaper.getStatus() == SmartPaperStatus.HIDE) {
			PracticeHistoryForm p = new PracticeHistoryForm();
			p.setUpdateAt(new Date());
			p.setBiz(Biz.SMART_PAPER);
			p.setBizId(smartExamPaper.getId());
			p.setUserId(smartExamPaper.getUserId());
			p.setName(smartExamPaper.getName());
			p.setCreateAt(smartExamPaper.getCreateAt());
			p.setCompletionRate(getCompletionRate(dones));
			practiceHistoryService.updateHistory(p);
		} else {
			p2.setCompletionRate(getCompletionRate(dones));
			practiceHistoryService.updateHistory(p2);
		}
	}

	/**
	 * 完成率
	 * 
	 * @param dones
	 * @return
	 */
	public BigDecimal getCompletionRate(List<Boolean> dones) {
		Integer doNum = 0;
		for (Boolean done : dones) {
			if (done) {
				doNum++;
			}
		}
		return BigDecimal.valueOf(doNum * 100f / dones.size()).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	@Override
	public SmartExamPaper get(long paperId) {
		return smartExamPaperRepo.get(paperId);
	}
}
