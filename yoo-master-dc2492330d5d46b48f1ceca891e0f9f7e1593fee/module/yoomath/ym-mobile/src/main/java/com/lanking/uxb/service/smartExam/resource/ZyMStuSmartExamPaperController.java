package com.lanking.uxb.service.smartExam.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaper;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaperDifficulty;
import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaperQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.form.ExerciseCommitForm;
import com.lanking.uxb.service.base.type.CommonSettings;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.smartExam.convert.PaperConvert;
import com.lanking.uxb.service.smartExam.value.VPaper;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.web.api.PullQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;
import com.lanking.uxb.service.zuoye.api.ZySmartPaperService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZySmartPaperConvert;
import com.lanking.uxb.service.zuoye.form.PaperPullForm;
import com.lanking.uxb.service.zuoye.form.PullQuestionType;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;
import com.lanking.uxb.service.zuoye.value.VPaperQuestion;

/**
 * 智能出卷接口
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月23日
 */
@RestController
@RequestMapping("zy/m/s/sep")
public class ZyMStuSmartExamPaperController {
	@Autowired
	private StudentService studentService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private ZySmartPaperService zyStuSmartPaperService;
	@Autowired
	private ZySmartPaperConvert zyStuSmartPaperConvert;
	@Autowired
	private ZyCorrectingService zyCorrectingService;
	@Autowired
	private PullQuestionService pullQuestionService;
	@Autowired
	private PaperConvert paperConvert;
	@Autowired
	private StudentQuestionAnswerService zyStuQaService;
	@Autowired
	private ZyStudentFallibleQuestionService zyStudentFallibleQuestionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private MqSender mqSender;

	/**
	 * @since yoomath(mobile) V1.0.0
	 * @param flag
	 *            是否是换一批
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@MemberAllowed(memberType = "VIP")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryPaperList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryPaperList(@RequestParam(required = false, defaultValue = "false") Boolean flag) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		Long doNum = zyStudentFallibleQuestionService.countDoNum(Security.getUserId());
		boolean match = doNum >= CommonSettings.MIN_PAPER_QUESTION_NUM;
		data.put("match", match);
		if (match) {
			Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
			List<VPaper> pList = new ArrayList<VPaper>();
			if (student.getTextbookCode() != null) {
				String textBookName = textbookService.get(student.getTextbookCode()).getName();
				for (SmartExamPaperDifficulty sd : SmartExamPaperDifficulty.values()) {
					VPaper pl = new VPaper();
					pl.setDifficult(sd.getDifficult());
					pl.setStar(sd.getStar());
					pl.setTitle(String.format(sd.getTitle(), textBookName));
					pl.setValue(sd.getValue());
					pList.add(pl);
				}
			}
			data.put("paperList", pList);
			if (flag) {// 更新试卷状态
				zyStuSmartPaperService.updateStatus(Security.getUserId(), student.getTextbookCode());
			}
		} else {
			data.put("progress", (doNum.intValue() * 100) / CommonSettings.MIN_PAPER_QUESTION_NUM);
		}
		return new Value(data);
	}

	/**
	 * 获取试卷题目
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param type
	 *            1 最新，2历史
	 * @param value
	 *            难度系数或试卷id
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryQuestionList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryQuestionList(int type, Long value) {
		List<SmartExamPaperQuestion> paperQuestions = null;
		Long paperId = 0L;
		if (type == 1) {
			PaperPullForm pForm = new PaperPullForm();
			Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
			pForm.setTextBookCode(student.getTextbookCode());
			pForm.setUserId(Security.getUserId());
			pForm.setSmartExamPaperDifficulty(SmartExamPaperDifficulty.findByValue(value.intValue()));
			paperQuestions = zyStuSmartPaperService.queryPaperQuestion(pForm);
			if (paperQuestions == null) {
				pForm.setCount(CommonSettings.QUESTION_PULL_COUNT);
				String difficult = SmartExamPaperDifficulty.findByValue(value.intValue()).getDifficult();
				BigDecimal minDifficulty = BigDecimal
						.valueOf(Double.parseDouble(difficult.substring(1, difficult.indexOf("-"))));
				BigDecimal maxDifficulty = BigDecimal.valueOf(
						Double.parseDouble(difficult.substring(difficult.indexOf("-") + 1, difficult.length() - 1)));
				pForm.setMinDifficulty(minDifficulty);
				pForm.setMaxDifficulty(maxDifficulty);
				pForm.setType(PullQuestionType.SMART_PAPER);
				List<Long> weakKnowpoints = zyStuSmartPaperService.queryWeakKnowpoints(student.getId(),
						student.getPhaseCode(), CommonSettings.WEAK_KNOWPOINT_COUNT);
				// 学生如果成绩比较好，没有薄弱知识点，就查找全部题目里面的
				if (weakKnowpoints.size() > 0) {
					pForm.setKnowledgePoints(weakKnowpoints);
				}
				// TODO: 根据当前知识点版本。拉取题目 (暂时未完成)
				List<Long> questionIds = pullQuestionService.pull(pForm);
				pForm.setqIds(questionIds);
				if (CollectionUtils.isNotEmpty(questionIds)) {
					paperQuestions = zyStuSmartPaperService.savePaper(pForm);
				}
			}
			if (CollectionUtils.isNotEmpty(paperQuestions)) {
				paperId = paperQuestions.get(0).getPaperId();
			}
		} else if (type == 2) {
			paperQuestions = zyStuSmartPaperService.queryPaperQuestion(value);
			paperId = value;
		}
		Map<String, Object> data = new HashMap<String, Object>(4);
		List<VPaperQuestion> vquestions = zyStuSmartPaperConvert.to(paperQuestions);
		if (paperId > 0) {
			SmartExamPaper paper = zyStuSmartPaperService.get(paperId);
			data.put("homeworkTime", paper.getHomeworkTime());
			data.put("difficulty", paper.getDifficulty());

			if (paper.getRightRate() == null) {
				int predictTime = 0;
				for (VPaperQuestion vq : vquestions) {
					predictTime += questionService.calPredictTime(vq.getQuestion().getType(),
							vq.getQuestion().getDifficulty(), vq.getQuestion().getSubject().getCode());
				}

				data.put("predictTime", predictTime);
			}
		} else {
			data.put("homeworkTime", 0);
		}

		data.put("paperQuestions", vquestions);
		if (CollectionUtils.isNotEmpty(paperQuestions)) {
			data.put("count", paperQuestions.size());
			data.put("paperId", paperQuestions.get(0).getPaperId());
		} else {
			data.put("count", 0);
		}
		return new Value(data);
	}

	/**
	 * 试卷提交
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param form
	 *            提交参数
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "commit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value commit(ExerciseCommitForm form) {
		if (form.getType() != 3 || CollectionUtils.isEmpty(form.getqIds())
				|| CollectionUtils.isEmpty(form.getAnswerList()) || form.getqIds().size() != form.getCount()
				|| form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}
		try {
			List<Map<String, Object>> results = zyCorrectingService.simpleCorrect(form.getqIds(), form.getAnswerList());
			VExerciseResult result = new VExerciseResult(3);
			result.setCommitAt(new Date());
			result.setqIds(form.getqIds());
			List<HomeworkAnswerResult> rets = new ArrayList<HomeworkAnswerResult>(form.getCount());
			List<Boolean> dones = new ArrayList<Boolean>(form.getCount());
			int rightCount = 0;
			for (Map<String, Object> map : results) {
				HomeworkAnswerResult ret = (HomeworkAnswerResult) map.get("result");
				boolean done = (Boolean) map.get("done");
				rets.add(ret);
				if (ret == HomeworkAnswerResult.RIGHT) {
					rightCount++;
				}
				dones.add(done);
			}
			result.setDones(dones);
			result.setRightRate(
					BigDecimal.valueOf((rightCount * 100f) / form.getCount()).setScale(2, BigDecimal.ROUND_HALF_UP));
			result.setResults(rets);
			// 记录答案、统计学情、记录错题
			zyStuQaService.asynCreate(Security.getUserId(), form.getqIds(), form.getAnswerList(), form.getAnswerList(),
					null, null, null, rets, StudentQuestionAnswerSource.SMART_PAPER, new Date());
			VUserReward vUserReward = zyStuSmartPaperService.savePaperResult(result, rightCount, form.getPaperId(),
					form.getAnswerList(), form.getHomeworkTime() == null ? 0 : form.getHomeworkTime(),
					Security.getUserId());
			result.setUserReward(vUserReward);

			// 智能出卷练习，题量任务
			JSONObject obj = new JSONObject();
			obj.put("taskCode", 101020016);
			obj.put("userId", Security.getUserId());
			obj.put("isClient", Security.isClient());
			Map<String, Object> pms = new HashMap<String, Object>(1);
			pms.put("questionCount", form.getCount());
			obj.put("params", pms);
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
					MQ.builder().data(obj).build());

			return new Value(result);
		} catch (AbstractException e) {
			return new Value(new IllegalArgException());
		}
	}

	/**
	 * 查询历史试卷列表
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param cursor
	 *            游标
	 * @param count
	 *            获取数量
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryHistoryPaperList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHistoryPaperList(@RequestParam(defaultValue = "0", required = false) long cursor,
			@RequestParam(defaultValue = "20", required = false) int count) {
		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		CursorPage<Long, SmartExamPaper> cp = zyStuSmartPaperService.queryHistoryPaperList(Security.getUserId(),
				student.getTextbookCode(), CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(count, 20)));
		VCursorPage<VPaper> vCursePage = new VCursorPage<VPaper>();
		if (cp.isNotEmpty()) {
			vCursePage.setCursor(cp.getNextCursor());
			vCursePage.setItems(paperConvert.to(cp.getItems()));
		} else {
			vCursePage.setCursor(cursor);
			vCursePage.setItems(Collections.EMPTY_LIST);
		}
		Map<String, Object> data = new HashMap<String, Object>(3);
		data.put("paperList", vCursePage);
		data.put("count", zyStuSmartPaperService.getHistoryPaperCount(Security.getUserId(), student.getTextbookCode()));
		if (cursor == 0) {
			BigDecimal b = zyStuSmartPaperService.getHistoryPaperAvg(Security.getUserId(), student.getTextbookCode());
			data.put("avgRightRate", b.setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		return new Value(data);
	}

	/**
	 * 重新练习
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "rePractice", method = { RequestMethod.POST, RequestMethod.GET })
	public Value rePractice(Long paperId) {
		zyStuSmartPaperService.rePractice(paperId);
		return new Value();
	}

	/**
	 * 重新练习
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "2/rePractice", method = { RequestMethod.POST, RequestMethod.GET })
	public Value rePractice2(Long paperId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		SmartExamPaper paper = zyStuSmartPaperService.get(paperId);
		if (paper.getPaperId() != 0) {
			SmartExamPaper paper1 = zyStuSmartPaperService.get(paper.getPaperId());
			if (paper1.getRightRate() == null) {
				List<SmartExamPaperQuestion> questions = zyStuSmartPaperService.queryPaperQuestion(paper.getPaperId());
				data.put("paperId", paper.getPaperId());
				data.put("hasFinish", false);
				data.put("paperQuestions", zyStuSmartPaperConvert.to(questions));
			} else {
				data.put("hasFinish", true);
			}

		} else {
			data.put("hasFinish", true);
		}
		return new Value(data);
	}

	/**
	 * 用户没有提交退出保存<br>
	 * 需求变更
	 * 
	 * @since yoomath(mobile) V1.0.1
	 * @param form
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "notFinishSave", method = { RequestMethod.POST, RequestMethod.GET })
	public Value notFinishSave(ExerciseCommitForm form) {
		if (form.getType() != 3 || CollectionUtils.isEmpty(form.getqIds())
				|| CollectionUtils.isEmpty(form.getAnswerList()) || form.getqIds().size() != form.getCount()
				|| form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}
		zyStuSmartPaperService.saveNotCommitPaper(form.getPaperId(), form.getAnswerList(), form.getqIds(),
				form.getHomeworkTime() == null ? 0 : form.getHomeworkTime());
		return new Value();
	}

}