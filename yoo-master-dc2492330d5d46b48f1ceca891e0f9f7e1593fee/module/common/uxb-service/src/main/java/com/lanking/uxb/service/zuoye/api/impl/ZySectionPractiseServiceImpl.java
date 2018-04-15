package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoomath.sectionPractise.SectionPractise;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.zuoye.api.PracticeHistoryService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;
import com.lanking.uxb.service.zuoye.api.ZySectionPractiseQuestionService;
import com.lanking.uxb.service.zuoye.api.ZySectionPractiseService;
import com.lanking.uxb.service.zuoye.form.PracticeHistoryForm;
import com.lanking.uxb.service.zuoye.form.SectionPractiseForm;
import com.lanking.uxb.service.zuoye.form.SectionPractiseQuestionForm;

/**
 * @see ZySectionPractiseService
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.1
 */
@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class ZySectionPractiseServiceImpl implements ZySectionPractiseService {

	@Autowired
	@Qualifier("SectionPractiseRepo")
	Repo<SectionPractise, Long> repo;

	@Autowired
	private ZyCorrectingService zyCorrectingService;
	@Autowired
	private ZySectionPractiseQuestionService sectionPractiseQuestionService;
	@Autowired
	private PracticeHistoryService practiceHistoryService;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private MqSender mqSender;

	@Override
	@Transactional
	public Map<String, Object> commit(SectionPractiseForm form) {
		Date now = new Date();

		SectionPractise sectionPractise = null;
		if (form.getId() != null) {
			sectionPractise = repo.get(form.getId());
			sectionPractise.setUpdateAt(now);
			sectionPractise.setCommitAt(now);
		} else {
			sectionPractise = new SectionPractise();
			sectionPractise.setCreateAt(now);
			sectionPractise.setName(form.getName());
			sectionPractise.setSectionCode(form.getSectionCode());
		}
		sectionPractise.setDifficulty(form.getDifficulty());
		sectionPractise.setHomeworkTime(form.getHomeworkTime());
		sectionPractise.setQuestionCount(form.getQuestionIds().size());
		sectionPractise.setUserId(form.getUserId());

		// 对题目进行批改,并保存学生答案
		List<Map<String, Object>> results = zyCorrectingService.simpleCorrect(form.getQuestionIds(),
				form.getAnswerList());
		List<SectionPractiseQuestionForm> questionForms = new ArrayList<SectionPractiseQuestionForm>(form
				.getQuestionIds().size());
		List<HomeworkAnswerResult> answerResults = new ArrayList<HomeworkAnswerResult>(results.size());
		List<Boolean> dones = new ArrayList<Boolean>(results.size());
		List<Long> questionIds = new ArrayList<Long>(results.size());
		Map<String, Object> retMap = new HashMap<String, Object>(7);
		int rightCount = 0;
		int doCount = 0;
		GrowthLog retGrowthLog = null;
		for (Map<String, Object> map : results) {
			SectionPractiseQuestionForm questionForm = new SectionPractiseQuestionForm();
			boolean done = (Boolean) map.get("done");
			dones.add(done);
			questionForm.setDone(done);
			questionForm.setQuestionId((Long) map.get("qId"));
			questionForm.setAnswers((Map<Long, List<String>>) map.get("answer"));
			HomeworkAnswerResult result = (HomeworkAnswerResult) map.get("result");
			answerResults.add(result);
			questionIds.add((Long) map.get("qId"));
			if (HomeworkAnswerResult.RIGHT == result) {
				rightCount++;
			}
			if (done) {
				doCount++;
				/*
				 * GrowthLog growthLog =
				 * growthService.grow(GrowthAction.DOING_DAILY_EXERCISE,
				 * form.getUserId(), true);
				 * coinsService.earn(CoinsAction.DOING_DAILY_EXERCISE,
				 * form.getUserId());
				 * 
				 * if (growthLog.getHonor() != null) { getGrowth++; earnCoin++;
				 * 
				 * retGrowthLog = growthLog; }
				 */
			}
			questionForm.setResult(result);

			questionForms.add(questionForm);
		}

		sectionPractise.setDoCount(doCount);
		sectionPractise.setRightCount(rightCount);
		sectionPractise.setWrongCount(sectionPractise.getDoCount() - rightCount);
		sectionPractise.setRightRate(BigDecimal.valueOf((rightCount * 100f) / sectionPractise.getQuestionCount())
				.setScale(2, BigDecimal.ROUND_HALF_UP));

		repo.save(sectionPractise);

		// 对章节练习的题目进行保存
		sectionPractiseQuestionService.commit(questionForms, sectionPractise.getId());

		// 更新历史练习列表
		PracticeHistoryForm historyForm = new PracticeHistoryForm();
		historyForm.setBiz(Biz.SECTION_EXERCISE);
		historyForm.setBizId(sectionPractise.getId());
		historyForm.setCompletionRate(BigDecimal.valueOf((doCount * 100d) / sectionPractise.getQuestionCount())
				.setScale(2, BigDecimal.ROUND_HALF_UP));
		historyForm.setCreateAt(now);
		historyForm.setRightRate(sectionPractise.getRightRate());
		historyForm.setUserId(sectionPractise.getUserId());
		historyForm.setName(sectionPractise.getName());
		practiceHistoryService.updateHistory(historyForm);

		/*
		 * if (retGrowthLog != null && retGrowthLog.getHonor() != null) {
		 * retMap.put("growthLog", retGrowthLog); retMap.put("getGrowth",
		 * getGrowth); retMap.put("earnCoin", earnCoin); }
		 */

		retMap.put("rightRate", sectionPractise.getRightRate());
		retMap.put("dones", dones);
		retMap.put("results", answerResults);
		retMap.put("qIds", questionIds);
		retMap.put("practiseId", sectionPractise.getId());
		return retMap;
	}

	@Override
	public SectionPractise get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public SectionPractise draft(SectionPractiseForm form) {
		SectionPractise sectionPractise = null;
		if (form.getId() == null) {
			sectionPractise = new SectionPractise();
			sectionPractise.setCreateAt(new Date());
			sectionPractise.setName(form.getName());
			sectionPractise.setSectionCode(form.getSectionCode());
		} else {
			sectionPractise = repo.get(form.getId());
			sectionPractise.setUpdateAt(new Date());
		}
		sectionPractise.setDifficulty(form.getDifficulty());
		sectionPractise.setHomeworkTime(form.getHomeworkTime());
		sectionPractise.setQuestionCount(form.getQuestionIds().size());
		sectionPractise.setUserId(form.getUserId());

		List<SectionPractiseQuestionForm> forms = new ArrayList<SectionPractiseQuestionForm>(form.getQuestionIds()
				.size());
		int doCount = 0;
		for (int i = 0; i < form.getQuestionIds().size(); i++) {
			SectionPractiseQuestionForm questionForm = new SectionPractiseQuestionForm();
			Map<Long, List<String>> one = form.getAnswerList().get(i);
			long qId = form.getQuestionIds().get(i);
			List<String> stuAnswers = one.get(qId) == null ? one.get(String.valueOf(qId)) : one.get(qId);
			boolean done = CollectionUtils.isNotEmpty(stuAnswers);
			if (done) {
				doCount++;
			}
			questionForm.setDone(done);
			questionForm.setAnswers(form.getAnswerList().get(i));
			questionForm.setQuestionId(qId);

			forms.add(questionForm);
		}
		sectionPractise.setDoCount(doCount);
		repo.save(sectionPractise);

		// 暂存章节练习的结果
		sectionPractiseQuestionService.draft(forms, sectionPractise.getId());

		// 更新历史练习列表
		PracticeHistoryForm historyForm = new PracticeHistoryForm();
		historyForm.setBiz(Biz.SECTION_EXERCISE);
		historyForm.setBizId(sectionPractise.getId());
		historyForm.setCompletionRate(BigDecimal.valueOf((doCount * 100d) / sectionPractise.getQuestionCount())
				.setScale(2, BigDecimal.ROUND_HALF_UP));
		historyForm.setCreateAt(new Date());
		historyForm.setUserId(sectionPractise.getUserId());
		historyForm.setName(sectionPractise.getName());

		practiceHistoryService.updateHistory(historyForm);

		return sectionPractise;
	}
}
