package com.lanking.uxb.zycon.homework.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.base.session.Device;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLogType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppeal;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppealStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkService;
import com.lanking.uxb.service.correct.vo.CorrectorType;
import com.lanking.uxb.service.correct.vo.QuestionCorrectObject;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.resources.api.QuestionAppealService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;
import com.lanking.uxb.zycon.homework.api.ZycAutoCorrectingService;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkClazzService;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkService;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkStudentClazzService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkService;
import com.lanking.uxb.zycon.homework.convert.ZycHomeworkClazzConvert;
import com.lanking.uxb.zycon.homework.convert.ZycHomeworkConvert;
import com.lanking.uxb.zycon.homework.convert.ZycHomeworkStudentClazzConvert;
import com.lanking.uxb.zycon.homework.convert.ZycQuestionConvert;
import com.lanking.uxb.zycon.homework.convert.ZycStudentHomeworkConvert;
import com.lanking.uxb.zycon.homework.value.VZycHomework;
import com.lanking.uxb.zycon.homework.value.VZycHomeworkStudentClazz;
import com.lanking.uxb.zycon.homework.value.VZycQuestion;
import com.lanking.uxb.zycon.homework.value.VZycStudentHomework;

import httl.util.CollectionUtils;
import httl.util.StringUtils;

/**
 * 后台查看学生的作业情况
 *
 * @author xinyu.zhou
 * @since yoomath V1.5
 */
@RestController
@RequestMapping(value = "zyc/qs/hk")
public class ZycHomeworkController {
	@Autowired
	private ZycHomeworkService homeworkService;
	@Autowired
	private ZycHomeworkConvert homeworkConvert;
	@Autowired
	private ZycHomeworkClazzService homeworkClazzService;
	@Autowired
	private ZycHomeworkClazzConvert homeworkClazzConvert;
	@Autowired
	private ZycStudentHomeworkService studentHomeworkService;
	@Autowired
	private ZycStudentHomeworkConvert studentHomeworkConvert;
	@Autowired
	private ZycHomeworkStudentClazzConvert homeworkStudentClazzConvert;
	@Autowired
	private ZycHomeworkStudentClazzService homeworkStudentClazzService;
	@Autowired
	private ZycHomeworkQuestionService homeworkQuestionService;
	@Autowired
	private ZycStudentHomeworkQuestionService studentHomeworkQuestionService;
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycQuestionConvert questionConvert;
	@Autowired
	private ZycAutoCorrectingService zycAutoCorrectingService;
	@Autowired
	private CorrectProcessor correctProcessor;
	@Autowired
	private CorrectStudentHomeworkService correctStudentHomeworkService;
	

	/**
	 * 作业下学生情况及作业信息
	 *
	 * @param hkId
	 *            Homework id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "stuhks", method = { RequestMethod.GET, RequestMethod.POST })
	public Value studentHomeworks(long hkId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Homework homework = homeworkService.get(hkId);

		VZycHomework vHomework = homeworkConvert.to(homework);
		vHomework.setClazz(homeworkClazzConvert.to(homeworkClazzService.get(homework.getHomeworkClassId())));
		data.put("homework", vHomework);
		List<StudentHomework> shs = studentHomeworkService.listByHomework(hkId);
		List<VZycStudentHomework> vshs = studentHomeworkConvert.to(shs);
		if (homework.getHomeworkClassId() != null || homework.getHomeworkClassId() > 0) {
			List<VZycHomeworkStudentClazz> vhkStuClazzs = homeworkStudentClazzConvert
					.to(homeworkStudentClazzService.list(homework.getHomeworkClassId()));
			Map<Long, VZycHomeworkStudentClazz> vhkStuClazzMap = new HashMap<Long, VZycHomeworkStudentClazz>(
					vhkStuClazzs.size());
			for (VZycStudentHomework v : vshs) {
				v.setStudentClazz(vhkStuClazzMap.get(v.getStudentId()));
			}
		}
		data.put("studentHomeworks", vshs);
		return new Value(data);
	}

	/**
	 * 获得学的做题情况
	 *
	 * @param stuHomeworkId
	 *            StudentHomework -> id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "stuhk_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value studentHomeworkQuestions(@RequestParam(value = "stuHomeworkId") long stuHomeworkId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		StudentHomework studentHomework = studentHomeworkService.get(stuHomeworkId);
		data.put("studentHomework", studentHomeworkConvert.to(studentHomework));
		List<Long> qid = homeworkQuestionService.getQuestion(studentHomework.getHomeworkId());
		List<Long> correctQIds = studentHomeworkQuestionService.getCorrectQuestions(stuHomeworkId);
		qid.addAll(correctQIds);
		List<Question> qs = new ArrayList<Question>(qid.size());
		Map<Long, Question> qsMap = questionService.mget(qid);
		for (Long id : qid) {
			if (correctQIds.contains(id)) {
				qsMap.get(id).setCorrectQuestion(true);
			}
			qs.add(qsMap.get(id));
		}
		List<VZycQuestion> vqs = questionConvert.to(qs, stuHomeworkId);
		data.put("questions", vqs);

		return new Value(data);
	}

	/**
	 * 新版修正接口
	 *
	 * @since yoomath V1.9.2
	 *
	 * @param stuHkId
	 *            学生作业id
	 * @param stuHkQId
	 *            学生作业题目id
	 * @param result
	 *            题目批改结果
	 * @param rightRate
	 *            题目的正确率
	 * @param answerResultsStr
	 *            答案批改结果
	 * @param type
	 *            {@link com.lanking.cloud.domain.common.resource.question.Question.Type}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "correct2", method = { RequestMethod.GET, RequestMethod.POST })
	public Value correct2(long stuHkId, long stuHkQId, HomeworkAnswerResult result,
			@RequestParam(value = "rightRate", required = false) Long rightRate,
			@RequestParam(value = "confirm", required = false) Boolean confirm,
			@RequestParam(value = "appealConfirm", required = false) Boolean appealConfirm,
			@RequestParam(value = "creator", required = false) Long creator,
			@RequestParam(value = "answerResults") String answerResultsStr, Question.Type type) {
		if (StringUtils.isEmpty(answerResultsStr)) {
			return new Value(new IllegalArgException());
		}

		StudentHomework stuHK = studentHomeworkService.get(stuHkId);
		if (stuHK == null || stuHK.getStatus() == StudentHomeworkStatus.NOT_SUBMIT) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.HOMEWORK_ISSUED));
		}
		if (type == Question.Type.FILL_BLANK && rightRate == null) {
			return new Value(new IllegalArgException());
		}

		Map<Long, HomeworkAnswerResult> answerResults = new HashMap<Long, HomeworkAnswerResult>();
		JSONObject jsonObject = JSON.parseObject(answerResultsStr);
		for (String key : jsonObject.keySet()) {
			answerResults.put(Long.valueOf(key), HomeworkAnswerResult.valueOf(jsonObject.getString(key)));
		}

		QuestionCorrectObject questionCorrectObject = new QuestionCorrectObject();
		questionCorrectObject.setStudentHomeworkId(stuHkId);
		questionCorrectObject.setStuHomeworkQuestionId(stuHkQId);
		questionCorrectObject.setQuestionType(type);
		questionCorrectObject.setQuestionResult(result);
		if (rightRate != null) {
			questionCorrectObject.setQuestionRightRate(rightRate.intValue());
		}
		questionCorrectObject.setAnswerResultMap(answerResults);

		// 调用新的批改流程
		correctProcessor.correctStudentHomeworkQuestion(Security.getUserId(), CorrectorType.PG_USER,
				questionCorrectObject);

		if (type == Question.Type.FILL_BLANK) {
			// 答案归档
			zycAutoCorrectingService.asyncAutoCheck(stuHkId, stuHkQId, result);
		}
		
//		//如果是确认的话，把问题的确认位给修改掉
//		if (confirm) {
//			studentHomeworkQuestionService.updateConfirmStatus(stuHkQId, HomeworkConfirmStatus.HAD_CONFIRM);
//		}
		
		// 获取学生作业正确率
		StudentHomework studentHomework = correctStudentHomeworkService.getStudentHomework(stuHkId);
		
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("rightRate", studentHomework.getRightRate());
		data.put("rightRateCorrect", studentHomework.getRightRateCorrect());
		
		return new Value(data);
	}

	/**
	 * 移除已经批改完成的作业。在后台作业列表不再显示
	 *
	 * @param homeworkId
	 *            作业id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "remove", method = { RequestMethod.GET, RequestMethod.POST })
	public Value remove(long homeworkId) {
		Homework homework = homeworkService.get(homeworkId);
		if (homework == null) {
			return new Value(new IllegalArgException());
		}
		boolean isLastCommit = false;
		if (homework.getLastCommitAt() != null) {
			isLastCommit = System.currentTimeMillis() > homework.getLastCommitAt().getTime()
					+ Env.getInt("homework.allcommit.then") * 60 * 1000;
		} else {
			// 距离最后期限也过了5分钟则也可以移除
			if (homework.getDeadline().getTime() + Env.getInt("homework.allcommit.then") * 60 * 1000 < System
					.currentTimeMillis()) {
				isLastCommit = true;
			}
		}
		// 距离最后一个学生提交还没有5分钟也不可以移除
		if (!isLastCommit) {
			return new Value(new NoPermissionException());
		}

		homeworkService.remove(homeworkId);

		return new Value();
	}

}
