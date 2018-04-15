package com.lanking.uxb.ycoorect.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.support.resources.question.QuestionError;
import com.lanking.cloud.domain.support.resources.question.QuestionErrorType;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLogType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppeal;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppealStatus;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectLog;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.intercomm.yoocorrect.dto.CorrectErrorQuestionData;
import com.lanking.intercomm.yoocorrect.dto.CorrectQuestionData;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserResponse;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserType;
import com.lanking.intercomm.yoocorrect.service.CorrectQuestionDatawayService;
import com.lanking.uxb.core.annotation.LoadCorrectUser;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.correct.api.CorrectLogService;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkQuestionService;
import com.lanking.uxb.service.correct.vo.CorrectorType;
import com.lanking.uxb.service.correct.vo.QuestionCorrectObject;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.QuestionAppealService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkAnswerConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkQuestionConvert;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.session.api.DeviceService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.ycoorect.ex.YooCorrectException;
import com.lanking.uxb.ycoorect.form.YooCorrectAnswerForm;
import com.lanking.uxb.ycoorect.form.YooCorrectQuestionForm;
import com.lanking.uxb.ycoorect.service.YooCorrectQuestionErrorService;

import lombok.extern.slf4j.Slf4j;

/**
 * 批改相关.
 * 
 * @author wanlong.che
 *
 */
@Slf4j
@RestController
@RequestMapping(value = "ycorrect/question")
public class YooCorrectQuestionController {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private StudentHomeworkQuestionService studentHomeworkQuestionService;
	@Autowired
	private StudentHomeworkQuestionConvert studentHomeworkQuestionConvert;
	@Autowired
	private StudentHomeworkAnswerService studentHomeworkAnswerService;
	@Autowired
	private StudentHomeworkAnswerConvert studentHomeworkAnswerConvert;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private CorrectProcessor correctProcessor;
	@Autowired
	private ParameterService parameterService;

	@Autowired
	private CorrectQuestionDatawayService correctQuestionDatawayService;
	@Autowired
	private QuestionAppealService questionAppealService;
	@Autowired
	private YooCorrectQuestionErrorService correctQuestionErrorService;
	@Autowired
	private CorrectStudentHomeworkQuestionService correctStudentHomeworkQuestionService;
	@Autowired
	private QuestionAppealService qaService;

	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private UserService userService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CorrectLogService correctLogService;

	/**
	 * 开始批改题目.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "startCorrectQuestion")
	public Value startCorrectQuestion() {

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		try {
			correctQuestionDatawayService.startCorrect(correctUser.getId());
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 获取批改相关题目（接收题目）.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getCorrectQuestion")
	public Value getCorrectQuestion() {

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);
		CorrectQuestionData correctQuestionData = correctQuestionDatawayService.getCorrectQuestion(correctUser.getId());
		if (correctQuestionData == null || correctQuestionData.getBizId() == null) {
			return new Value();
		}
		Long studentHomeworkQuestionId = correctQuestionData.getBizId();

		StudentHomeworkQuestion studentHomeworkQuestion = studentHomeworkQuestionService.get(studentHomeworkQuestionId);
		VStudentHomeworkQuestion vStudentHomeworkQuestion = studentHomeworkQuestionConvert.to(studentHomeworkQuestion);
		List<StudentHomeworkAnswer> studentHomeworkAnswers = studentHomeworkAnswerService
				.find(studentHomeworkQuestionId);
		for (StudentHomeworkAnswer answer : studentHomeworkAnswers) {
			if (answer.getResult() == null) {
				answer.setResult(HomeworkAnswerResult.INIT);
			}
		}
		List<VStudentHomeworkAnswer> vStudentHomeworkAnswers = studentHomeworkAnswerConvert.to(studentHomeworkAnswers);
		Question question = questionService.get(studentHomeworkQuestion.getQuestionId());
		QuestionConvertOption option = new QuestionConvertOption();
		option.setAnalysis(true);
		option.setAnswer(true);
		option.setInitTextbookCategory(false);
		option.setInitMetaKnowpoint(false);
		option.setInitExamination(false);
		option.setInitKnowledgePoint(false);
		option.setInitPhase(false);
		option.setInitSubject(false);
		option.setInitQuestionType(false);
		option.setInitStudentQuestionCount(false);
		VQuestion vQuestion = questionConvert.to(question, option);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("question", vQuestion);
		map.put("studentHomeworkQuestion", vStudentHomeworkQuestion);
		map.put("studentHomeworkAnswers", vStudentHomeworkAnswers);

		// 管理员需要看是否有订正题
		if (correctUser.getCorrectUserType() == CorrectUserType.ADMIN && !studentHomeworkQuestion.isCorrect()
				&& !studentHomeworkQuestion.isNewCorrect()) {
			StudentHomeworkQuestion newCorrectQuestion = correctStudentHomeworkQuestionService.getNewCorrectQuestion(
					studentHomeworkQuestion.getStudentHomeworkId(), studentHomeworkQuestion.getQuestionId());
			if (newCorrectQuestion != null) {
				List<StudentHomeworkAnswer> newCorrectAnswers = studentHomeworkAnswerService
						.find(newCorrectQuestion.getId());
				for (StudentHomeworkAnswer answer : newCorrectAnswers) {
					if (answer.getResult() == null) {
						answer.setResult(HomeworkAnswerResult.INIT);
					}
				}
				List<VStudentHomeworkAnswer> vNewCorrectAnswers = studentHomeworkAnswerConvert.to(newCorrectAnswers);
				VStudentHomeworkQuestion correctQuestion = studentHomeworkQuestionConvert.to(newCorrectQuestion);
				// 是否需要看是否有订正题，如果订正题是学生放弃订正的，不展示
				boolean showCorrectQuestion = false;
				if (newCorrectQuestion.getType() == Question.Type.FILL_BLANK) {
					// 填空题答案如果是空的说明学生没有作答，学生放弃订正
					if (null != vNewCorrectAnswers && vNewCorrectAnswers.size() > 0) {
						for (VStudentHomeworkAnswer answer : vNewCorrectAnswers) {
							if (answer.getResult() == HomeworkAnswerResult.INIT
									&& StringUtils.isBlank(answer.getContent())) {

							} else {
								showCorrectQuestion = true;
								break;
							}
						}
					}
				} else if (newCorrectQuestion.getType() == Question.Type.QUESTION_ANSWERING) {
					// 解答题没有上传图片说明学生没有作答，学生放弃订正
					List<String> answerImgs = correctQuestion.getAnswerImgs();
					if (null != answerImgs && answerImgs.size() > 0) {
						showCorrectQuestion = true;
					}
				}
				if (showCorrectQuestion) {
					map.put("correctHomeworkQuestion", correctQuestion);
					map.put("correctAnswers", vNewCorrectAnswers);
				}
			}
		}

		// 获取申诉记录
		Map<String, Object> questionAppealMap = new HashMap<String, Object>();
		QuestionAppeal questionAppeal = questionAppealService.getLastAppeal(studentHomeworkQuestionId);
		if (questionAppeal != null) {
			questionAppealMap.put("appeal", true);
			questionAppealMap.put("comment", questionAppeal.getComment());
		} else {
			questionAppealMap.put("appeal", false);
		}
		map.put("questionAppealcomment", questionAppealMap);

		// 获取反馈记录
		Map<String, Object> questionErrorMap = new HashMap<String, Object>();
		QuestionError questionError = correctQuestionErrorService.get(studentHomeworkQuestionId);
		if (questionError != null) {
			questionErrorMap.put("error", true);
			questionErrorMap.put("type", questionError.getTypeList().get(0));
			questionErrorMap.put("comment", questionError.getDescription());
		} else {
			questionErrorMap.put("error", false);
		}
		map.put("questionError", questionErrorMap);

		return new Value(map);
	}

	/**
	 * 停止接受批改题目.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "stopGetCorrectQuestion")
	public Value stopGetCorrectQuestion() {
		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		try {
			correctQuestionDatawayService.stopCorrect(correctUser.getId());
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 获得模拟批改题目.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getMockCorrectQuestion")
	public Value getMockCorrectQuestion() {
		Parameter parameter = parameterService.get(Product.YOOMATH, "yoocorrect-mock-questions");
		List<Map<String, Object>> list = Lists.newArrayList();
		if (parameter != null) {
			String shqIds = parameter.getValue();
			if (StringUtils.isNotBlank(shqIds)) {
				List<Long> shqids = Lists.newArrayList();
				String[] shqIdArray = shqIds.split(",");
				for (String shqId : shqIdArray) {
					shqids.add(Long.parseLong(shqId));
				}

				// 全部的学生习题
				List<VStudentHomeworkQuestion> studentHomeworkQuestions = studentHomeworkQuestionConvert
						.to(studentHomeworkQuestionService.mgetList(shqids));
				Map<Long, List<StudentHomeworkAnswer>> answerMap = studentHomeworkAnswerService.find(shqids);
				List<Long> questionIds = new ArrayList<Long>(studentHomeworkQuestions.size());
				for (VStudentHomeworkQuestion shq : studentHomeworkQuestions) {
					questionIds.add(shq.getQuestionId());
				}
				QuestionConvertOption option = new QuestionConvertOption();
				option.setAnalysis(true);
				option.setAnswer(true);
				option.setInitTextbookCategory(false);
				option.setInitMetaKnowpoint(false);
				option.setInitExamination(false);
				option.setInitKnowledgePoint(false);
				option.setInitPhase(false);
				option.setInitSubject(false);
				option.setInitQuestionType(false);
				option.setInitStudentQuestionCount(false);
				Map<Long, VQuestion> questionMap = questionConvert.to(questionService.mget(questionIds), option);

				for (VStudentHomeworkQuestion shq : studentHomeworkQuestions) {
					Map<String, Object> map = new HashMap<String, Object>();

					List<StudentHomeworkAnswer> studentHomeworkAnswers = answerMap.get(shq.getId());
					List<VStudentHomeworkAnswer> vStudentHomeworkAnswers = studentHomeworkAnswerConvert
							.to(studentHomeworkAnswers);
					map.put("question", questionMap.get(shq.getQuestionId()));
					map.put("studentHomeworkQuestion", shq);
					map.put("studentHomeworkAnswers", vStudentHomeworkAnswers);
					list.add(map);
				}
			}
		}

		return new Value(list);
	}

	/**
	 * 结束模拟.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "completeMock")
	public Value completeMock() {
		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);
		try {
			correctQuestionDatawayService.mockComplete(correctUser.getId());
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 批改习题.
	 * 
	 * @param form
	 *            提交表单对象（原题）
	 * @param correctJson
	 *            提交表单对象（订正题）
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "correct")
	public Value correct(@RequestBody String data) {
		JSONObject jsonObject = JSONObject.parseObject(data);
		String json = jsonObject.getString("json");
		String correctJson = jsonObject.getString("correctJson");
		if (StringUtils.isBlank(json)) {
			return new Value(new MissingArgumentException());
		}
		YooCorrectQuestionForm form = JSON.parseObject(json, YooCorrectQuestionForm.class);
		if (form.getStudentHomeworkQuestionId() == null
				|| (CollectionUtils.isEmpty(form.getAnswerResults()) && form.getRightRate() == null)
				|| form.getCostTime() == null) {
			return new Value(new MissingArgumentException());
		}
		if (form.getRightRate() != null && (form.getRightRate() < 0 || form.getRightRate() > 100)) {
			return new Value(new IllegalArgException());
		}

		List<YooCorrectQuestionForm> forms = new ArrayList<YooCorrectQuestionForm>(2);
		forms.add(form);

		// 订正题
		YooCorrectQuestionForm correctForm = null;
		if (StringUtils.isNotBlank(correctJson)) {
			correctForm = JSON.parseObject(correctJson, YooCorrectQuestionForm.class);
			if (correctForm.getStudentHomeworkQuestionId() == null
					|| (CollectionUtils.isEmpty(correctForm.getAnswerResults())
							&& correctForm.getRightRate() == null)) {
				return new Value(new MissingArgumentException());
			}
			if (correctForm.getRightRate() != null
					&& (correctForm.getRightRate() < 0 || correctForm.getRightRate() > 100)) {
				return new Value(new IllegalArgException());
			}
			forms.add(correctForm);
		}

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		if (correctUser.getCorrectUserType() == CorrectUserType.TEACHER) {
			// 普通批改员不可能有直接的订正题批改结果
			if (StringUtils.isNotBlank(correctJson)) {
				return new Value(new IllegalArgException());
			}
		}

		// 获取申诉记录
		QuestionAppeal questionAppeal = questionAppealService.getLastAppeal(form.getStudentHomeworkQuestionId());

		List<CorrectErrorQuestionData> correctErrorQuestionDatas = Lists.newArrayList(); // 错题批改记录
		List<Long> errorCorrectStudentHomeworkQuestionId = Lists.newArrayList(); // 经管理员纠正过的错改习题
		try {
			List<QuestionCorrectObject> questionCorrectObjects = new ArrayList<QuestionCorrectObject>(2);
			StudentHomework studentHomework = null;
			for (YooCorrectQuestionForm fm : forms) {
				StudentHomeworkQuestion shq = studentHomeworkQuestionService.get(fm.getStudentHomeworkQuestionId());

				if (correctUser.getCorrectUserType() == CorrectUserType.TEACHER
						&& (shq.getResult() == HomeworkAnswerResult.RIGHT
								|| shq.getResult() == HomeworkAnswerResult.WRONG)) {
					// 普通批改员批改完成的题目是已经批改过的情况
					return new Value(new YooCorrectException(YooCorrectException.QUESTION_ALLREADY_CORRECT));
				}

				// 管理员批改申诉题
				if (correctUser.getCorrectUserType() == CorrectUserType.ADMIN && questionAppeal != null) {
					if (shq.getType() == Type.QUESTION_ANSWERING && shq.getRightRate() != null
							&& shq.getRightRate() != fm.getRightRate()) {
						// 解答题原有正确率且当前批改结果不一致
						log.info("[-correct err-] AQ shq-id=" + shq.getId());
						errorCorrectStudentHomeworkQuestionId.add(shq.getId());

						QuestionCorrectLog cLog = correctLogService.getNewestLog(shq.getId());
						if (cLog != null && cLog.getCorrectType() == QuestionCorrectType.YOO_CORRECT
								&& cLog.getUserId() != null) {
							CorrectErrorQuestionData errData = new CorrectErrorQuestionData();
							errData.setUxbUserId(cLog.getUserId());
							errData.setStudentHomeworkQuestionId(shq.getId());
							correctErrorQuestionDatas.add(errData);
						}
					} else if (shq.getType() == Type.FILL_BLANK) {
						// 填空题原答案有批改结果且当前批改结果不一致
						List<StudentHomeworkAnswer> studentHomeworkAnswers = studentHomeworkAnswerService
								.find(shq.getId());
						Map<Long, StudentHomeworkAnswer> studentHomeworkAnswerMap = new HashMap<Long, StudentHomeworkAnswer>(
								studentHomeworkAnswers.size());
						for (StudentHomeworkAnswer sha : studentHomeworkAnswers) {
							studentHomeworkAnswerMap.put(sha.getId(), sha);
						}
						for (int i = 0; i < fm.getAnswerResults().size(); i++) {
							YooCorrectAnswerForm af = fm.getAnswerResults().get(i);
							StudentHomeworkAnswer oldAnswer = studentHomeworkAnswerMap.get(af.getId());
							if (oldAnswer != null && oldAnswer.getResult() != null
									&& (oldAnswer.getResult() == HomeworkAnswerResult.RIGHT
											|| oldAnswer.getResult() == HomeworkAnswerResult.WRONG)
									&& oldAnswer.getResult() != af.getResult()) {
								log.info("[-correct err-] FB shq-id=" + shq.getId());
								errorCorrectStudentHomeworkQuestionId.add(shq.getId());

								QuestionCorrectLog cLog = correctLogService.getNewestLog(shq.getId());
								if (cLog != null && cLog.getCorrectType() == QuestionCorrectType.YOO_CORRECT
										&& cLog.getUserId() != null) {
									CorrectErrorQuestionData errData = new CorrectErrorQuestionData();
									errData.setUxbUserId(cLog.getUserId());
									errData.setStudentHomeworkQuestionId(shq.getId());
									correctErrorQuestionDatas.add(errData);
								}
								break;
							}
						}
					}
				}

				if (studentHomework == null) {
					studentHomework = studentHomeworkService.get(shq.getStudentHomeworkId());
				}

				// 调用新的批改流程
				QuestionCorrectObject questionCorrectObject = new QuestionCorrectObject();
				questionCorrectObject.setStudentHomeworkId(studentHomework.getId());
				questionCorrectObject.setStuHomeworkQuestionId(shq.getId());
				questionCorrectObject.setQuestionType(shq.getType());
				questionCorrectObject.setQuestionRightRate(fm.getRightRate());
				questionCorrectObject.setQuestionResult(fm.getResult());

				// 填空题
				if (shq.getType() == Type.FILL_BLANK) {
					HomeworkAnswerResult haresult = HomeworkAnswerResult.RIGHT;
					Map<Long, HomeworkAnswerResult> answerResultMap = Maps.newHashMap();
					for (YooCorrectAnswerForm answerForm : fm.getAnswerResults()) {
						answerResultMap.put(answerForm.getId(), answerForm.getResult());
						if (answerForm.getResult() != HomeworkAnswerResult.RIGHT) {
							haresult = HomeworkAnswerResult.WRONG;
						}
					}
					questionCorrectObject.setQuestionResult(haresult);
					questionCorrectObject.setAnswerResultMap(answerResultMap);
				} else if (shq.getType() == Type.QUESTION_ANSWERING) {
					questionCorrectObject.setQuestionResult(
							fm.getRightRate() == 100 ? HomeworkAnswerResult.RIGHT : HomeworkAnswerResult.WRONG);
				}
				if (CollectionUtils.isNotEmpty(fm.getNotations())) {
					questionCorrectObject.setNotation(fm.getNotations().get(0));
					questionCorrectObject.setNotations(fm.getNotations());
				}
				if (CollectionUtils.isNotEmpty(fm.getNotationImageIds())) {
					questionCorrectObject.setNotationImageId(fm.getNotationImageIds().get(0));
					questionCorrectObject.setNotationImageIds(fm.getNotationImageIds());
				}
				if (CollectionUtils.isNotEmpty(fm.getAnswerImgIds())) {
					questionCorrectObject.setAnswerImgId(fm.getAnswerImgIds().get(0));
					questionCorrectObject.setAnswerImgIds(fm.getAnswerImgIds());
				}

				questionCorrectObjects.add(questionCorrectObject);
			}
			correctProcessor.correctStudentHomeworkQuestions(Security.getUserId(), CorrectorType.Y_CORRECTOR,
					studentHomework.getId(), questionCorrectObjects);

			// 批改完成
			int costTime = 0;
			for (int i = 0; i < forms.size(); i++) {
				YooCorrectQuestionForm fm = forms.get(i);
				if (costTime == 0) {
					costTime = fm.getCostTime();
				}
				StudentHomeworkQuestion shq = studentHomeworkQuestionService.get(fm.getStudentHomeworkQuestionId());
				if (i == 0) {
					correctQuestionDatawayService.correctComplete(correctUser.getId(), shq.getId(), costTime,
							shq.getRightRate(), true);
				}

				// 更新待确认状态
				studentHomeworkQuestionService.setStudentHomeworkQuestionConfirmStatus(shq.getId(),
						HomeworkConfirmStatus.NEED_CONFIRM);
			}

			// 通知错改
			if (correctErrorQuestionDatas.size() > 0) {
				correctQuestionDatawayService.errorCorrect(correctErrorQuestionDatas);
			}

			// 推送、短信
			if (correctUser.getCorrectUserType() == CorrectUserType.ADMIN && questionAppeal != null
					&& questionAppeal.getStatus() == QuestionAppealStatus.INIT) {

				User user = userService.get(studentHomework.getStudentId());
				Homework homework = homeworkService.get(studentHomework.getHomeworkId());

				// 该题为申诉题
				if (errorCorrectStudentHomeworkQuestionId.size() > 0) {
					// 申诉成功的推送、短信,学生的话还要加20金币
					sendSms(questionAppeal.getCreator(), homework.getName(), user.getName(),
							questionAppeal.getUserType(), true);
					pushMessage(questionAppeal.getCreator(), homework.getId(), studentHomework.getId(),
							homework.getName(), user.getName(), questionAppeal.getUserType(), true);
					if (questionAppeal.getUserType() == UserType.STUDENT) {
						// 加金币
						addCoins(questionAppeal);
					}
					qaService.updateStatus(questionAppeal.getId(), QuestionAppealStatus.SUCCESS);
				} else {
					// 申述失败的推送、短信
					pushMessage(questionAppeal.getCreator(), homework.getId(), studentHomework.getId(),
							homework.getName(), user.getName(), questionAppeal.getUserType(), false);
					sendSms(questionAppeal.getCreator(), homework.getName(), user.getName(),
							questionAppeal.getUserType(), false);
					qaService.updateStatus(questionAppeal.getId(), QuestionAppealStatus.FAILURE);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Value(new ServerException());
		}

		return new Value();
	}

	/**
	 * 反馈.
	 * 
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 * @param content
	 *            内容
	 * 
	 * @param 反馈错误类型
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "feedback")
	public Value feedback(Long studentHomeworkQuestionId, String content, QuestionErrorType type) {
		if (studentHomeworkQuestionId == null || type == null) {
			return new Value(new MissingArgumentException());
		}
		StudentHomeworkQuestion studentHomeworkQuestion = studentHomeworkQuestionService.get(studentHomeworkQuestionId);

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		QuestionError questionError = new QuestionError();
		questionError.setCreateAt(new Date());
		questionError.setDescription(content);
		questionError.setQuestionId(studentHomeworkQuestion.getQuestionId());
		questionError.setStatus(Status.ENABLED);
		questionError.setTypeList(Lists.newArrayList(type));
		try {
			correctQuestionErrorService.saveCorrectQuestionError(studentHomeworkQuestionId, questionError);

			// 小悠快批通信
			correctQuestionDatawayService.feedbackQuestion(correctUser.getId(), studentHomeworkQuestionId);
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	// 发短信
	private void sendSms(Long userId, String homeworkName, String studentName, UserType type, Boolean success) {
		// 短信
		String mobile = accountService.getAccountByUserId(userId).getMobile();
		if (StringUtils.isNotBlank(mobile)) {

			Integer code = null;

			ValueMap valueMap = ValueMap.value("homeworkName", homeworkName);

			if (type == UserType.TEACHER) {
				if (success) {
					code = 10000029;
				} else {
					code = 10000030;
				}
			} else if (type == UserType.STUDENT) {
				if (success) {
					code = 10000032;
				} else {
					code = 10000031;
					valueMap.put("studentName", studentName);
				}
			}
			try {
				messageSender.send(new SmsPacket(mobile, code, valueMap));
			} catch (Exception e) {
				log.error("消息发送失败！" + e);
			}

		}
	}

	// 推送消息
	private void pushMessage(Long userId, Long homeworkId, Long stuHkId, String homeworkName, String studentName,
			UserType type, Boolean success) {
		String url = null;

		MessagePacket messagePacket = null;

		// 推送
		List<String> tokens = deviceService.findTokenByUserIds(Lists.newArrayList(userId), Product.YOOMATH);
		if (CollectionUtils.isNotEmpty(tokens)) {
			Integer code = null;
			YooApp app = null;

			ValueMap valueMap = ValueMap.value("homeworkName", homeworkName);

			if (type == UserType.TEACHER) {
				app = YooApp.MATH_TEACHER;

				url = "math_teacher://yoomath.com/homework-detail?id=" + homeworkId;

				valueMap.put("studentName", studentName);

				if (success) {
					code = 12000044;
				} else {
					code = 12000045;
				}

			} else if (type == UserType.STUDENT) {
				app = YooApp.MATH_STUDENT;

				url = "math_student://yoomath.com/homework-detail?id=" + stuHkId;

				if (success) {
					code = 12000042;
				} else {
					code = 12000043;
				}
			}
			messagePacket = new PushPacket(Product.YOOMATH, app, tokens, code, new HashMap<String, Object>(), url,
					valueMap);
		}
		if (messagePacket != null) {
			try {
				messageSender.send(messagePacket);
			} catch (Exception e) {
				log.error("消息推送失败！" + e);
			}
		}
	}

	// 加金币
	private void addCoins(QuestionAppeal appeal) {
		CoinsLog coinsLog = new CoinsLog();
		coinsLog.setUserId(appeal.getCreator());
		coinsLog.setCoinsValue(20);
		coinsLog.setStatus(Status.ENABLED);
		coinsLog.setRuleCode(128);
		coinsLog.setCreateAt(new Date());
		coinsLog.setBiz(Biz.QUESTION_APPEAL); // 申诉
		coinsLog.setBizId(0L);
		coinsLog.setType(CoinsLogType.COINS_RULE);
		coinsLog.setP1(null);

		coinsLogService.save(coinsLog);

		userHonorService.saveOrUpdate(appeal.getCreator());
	}
}
