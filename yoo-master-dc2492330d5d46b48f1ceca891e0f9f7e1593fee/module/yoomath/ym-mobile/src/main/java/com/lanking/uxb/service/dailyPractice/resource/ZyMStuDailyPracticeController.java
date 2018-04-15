package com.lanking.uxb.service.dailyPractice.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeDifficulty;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeSettings;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractiseQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.form.ExerciseCommitForm;
import com.lanking.uxb.service.base.resource.ZyMBaseController;
import com.lanking.uxb.service.base.type.CommonSettings;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.resource.ZyMStuUserCenterController;
import com.lanking.uxb.service.web.api.DailyPractiseGenerateService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPracticeSettingsService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseService;
import com.lanking.uxb.service.zuoye.convert.ZyDailyPractiseConvert;
import com.lanking.uxb.service.zuoye.convert.ZyDailyPractiseQuestionConvert;
import com.lanking.uxb.service.zuoye.value.VDailyPractise;
import com.lanking.uxb.service.zuoye.value.VDailyPractiseQuestion;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;

/**
 * 每日练接口
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月23日
 */
@RestController
@RequestMapping("zy/m/s/dp")
public class ZyMStuDailyPracticeController extends ZyMBaseController {

	@Autowired
	private ZyDailyPracticeSettingsService dailyPracticeSettingsService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ZyDailyPractiseQuestionService dailyPractiseQuestionService;
	@Autowired
	private ZyDailyPractiseQuestionConvert dailyPractiseQuestionConvert;
	@Autowired
	private ZyDailyPractiseService dailyPractiseService;
	@Autowired
	private ZyDailyPractiseConvert dailyPractiseConvert;
	@Autowired
	private ZyCorrectingService zyCorrectingService;
	@Autowired
	private StudentQuestionAnswerService zyStuQaService;
	@Autowired
	private DailyPractiseGenerateService dailyPractiseGenerateService;
	@Autowired
	private ZyMStuUserCenterController stuUserCenterController;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 获取可选难度列表
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "listSettings", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listSettings() {
		DailyPracticeDifficulty[] arr = DailyPracticeDifficulty.values();
		List<Map<String, String>> difficulties = new ArrayList<Map<String, String>>(arr.length);
		for (DailyPracticeDifficulty one : arr) {
			Map<String, String> oneMap = new HashMap<String, String>(3);
			oneMap.put("code", one.name());
			oneMap.put("name", one.getName());
			oneMap.put("difficulty", one.getDifficult());
			difficulties.add(oneMap);
		}
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("list", difficulties);
		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		if (student.getTextbookCode() != null) {
			DailyPracticeSettings settings = dailyPracticeSettingsService.findByTextbookCode(Security.getUserId(),
					student.getTextbookCode());
			if (settings != null && settings.getDifficulty() != null) {
				data.put("code", settings.getDifficulty().name());
			} else {
				data.put("code", ZyDailyPracticeSettingsService.DEF.name());
			}
		}
		return new Value(data);
	}

	/**
	 * 设置难度
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param difficulty
	 *            难度
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "setDifficulty", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setDifficulty(DailyPracticeDifficulty difficulty) {
		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		if (student.getTextbookCode() == null) {
			return new Value(new ServerException());
		}
		dailyPracticeSettingsService.set(Security.getUserId(), student.getTextbookCode(), difficulty, null);
		return new Value();
	}

	/**
	 * 设置每日练进度(章节代码)
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param sectionCode
	 *            章节代码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "setProgress", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setProgress(long sectionCode) {
		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		if (student.getTextbookCode() == null) {
			return new Value(new ServerException());
		}
		dailyPracticeSettingsService.set(Security.getUserId(), student.getTextbookCode(), null, sectionCode);
		return new Value();
	}

	/**
	 * 查询学生每日一练记录（当前用户设置的教材版本）
	 *
	 * @param cursor
	 *            cursor
	 * @param size
	 *            分页大小
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "queryDailyPractise", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryDailyPractise(@RequestParam(value = "cursor", required = false) Long cursor,
			@RequestParam(value = "size", defaultValue = "20") int size) {
		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		if (student.getTextbookCode() == null) {
			return new Value(new ServerException());
		}
		Map<String, Object> rtnMap = new HashMap<String, Object>(3);
		CursorPageable<Long> cursorPageable = CP.cursor(cursor, size);
		CursorPage<Long, DailyPractise> cursorPage = dailyPractiseService.query(Security.getUserId(),
				student.getTextbookCode(), cursorPageable);
		List<VDailyPractise> dailyPractises = dailyPractiseConvert.to(cursorPage.getItems());
		VCursorPage<VDailyPractise> vPage = new VCursorPage<VDailyPractise>();
		vPage.setCursor(cursorPage.getNextCursor() == null ? 0 : cursorPage.getNextCursor());
		vPage.setItems(dailyPractises);

		rtnMap.put("totalDays",
				dailyPractiseService.getTotalPractiseDays(Security.getUserId(), student.getTextbookCode(), false));
		rtnMap.put("totalQuestions",
				dailyPractiseQuestionService.countStudentQuestion(student.getTextbookCode(), Security.getUserId()));
		rtnMap.put("practises", vPage);

		Map<String, Object> genMap = dailyPractiseGenerateService.generate(student, CommonSettings.QUESTION_PULL_COUNT);
		boolean finish = (boolean) genMap.get("finish");
		rtnMap.put("finish", finish);

		return new Value(rtnMap);
	}

	/**
	 * 首页得到当天的练习
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "findDailyPractise", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findDailyPractise(@RequestParam(value = "size", defaultValue = "10") int size) {
		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		if (student == null || student.getTextbookCode() == null) {
			// 为什么学生没有设置教材也可以访问此接口？
			Map<String, Object> rtnMap = new HashMap<String, Object>(2);
			rtnMap.put("needSetting", true);
			rtnMap.put("finish", false);
			rtnMap.put("practises", new VCursorPage());

			return new Value(rtnMap);
		}

		Map<String, Object> rtnMap = new HashMap<String, Object>(4);

		// 判断学生今日是否有每日练习,如果没有则生成
		Map<String, Object> genMap = dailyPractiseGenerateService.generate(student, CommonSettings.QUESTION_PULL_COUNT);
		boolean finish = (boolean) genMap.get("finish");
		boolean setting = (boolean) genMap.get("setting");

		CursorPageable<Long> cursorPageable = CP.cursor(null, Math.min(size, 20));
		CursorPage<Long, DailyPractise> page = dailyPractiseService.query(Security.getUserId(),
				student.getTextbookCode(), cursorPageable);

		VCursorPage<VDailyPractise> vPage = new VCursorPage<VDailyPractise>();
		List<VDailyPractise> vs = dailyPractiseConvert.to(page.getItems());

		vPage.setItems(vs);

		if (!setting) {
			if (dailyPractiseService.getTotalPractiseDays(Security.getUserId(), student.getTextbookCode(),
					true) >= CommonSettings.NOT_SETTING_SHOW_DAY) {
				rtnMap.put("needSetting", false);
			} else {
				rtnMap.put("needSetting", true);
			}
		} else {
			rtnMap.put("needSetting", false);
		}

		rtnMap.put("totalQuestions",
				dailyPractiseQuestionService.countStudentQuestion(student.getTextbookCode(), Security.getUserId()));
		rtnMap.put("practises", vPage);
		// 如果练完了则返回true,没有则返回false
		rtnMap.put("finish", finish);

		return new Value(rtnMap);
	}

	/**
	 * 查询每日练习的题目列表
	 *
	 * @param practiseId
	 *            每日练习的id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "queryPractiseQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryPractiseQuestions(long practiseId) {
		List<DailyPractiseQuestion> questions = dailyPractiseQuestionService.findByPractise(practiseId);
		List<VDailyPractiseQuestion> vquestions = dailyPractiseQuestionConvert.to(questions);
		return new Value(vquestions);
	}

	/**
	 * 新版接口返回此次作业的答题时间
	 *
	 * @since yoomath(mobile) V1.1
	 * @param practiseId
	 *            每日练习id
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "queryPractiseQuestions2", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryPractiseQuestions2(long practiseId) {
		DailyPractise dailyPractise = dailyPractiseService.get(practiseId);
		if (dailyPractise == null) {
			return new Value();
		}

		Map<String, Object> retMap = new HashMap<String, Object>(4);
		List<VDailyPractiseQuestion> qs = dailyPractiseQuestionConvert
				.to(dailyPractiseQuestionService.findByPractise(practiseId));

		if (dailyPractise.getRightRate() == null || dailyPractise.getRightRate().intValue() <= 0) {
			int predictTime = 0;
			for (VDailyPractiseQuestion vq : qs) {
				VQuestion v = vq.getQuestion();
				predictTime += questionService.calPredictTime(v.getType(), v.getDifficulty(), v.getSubject().getCode());
			}

			retMap.put("predictTime", predictTime);
		}
		retMap.put("items", qs);

		// 此两处为练习历史 查看详情产生练习报告所需的数据
		retMap.put("homeworkTime", dailyPractise.getHomeworkTime());
		retMap.put("difficulty", dailyPractise.getDifficulty());

		return new Value(retMap);
	}

	/**
	 * 提交练习
	 *
	 * @param form
	 *            {@link ExerciseCommitForm}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "commit", method = { RequestMethod.GET, RequestMethod.POST })
	public Value commit(ExerciseCommitForm form) {
		if (form.getType() != 2 || CollectionUtils.isEmpty(form.getqIds())
				|| CollectionUtils.isEmpty(form.getAnswerList()) || form.getqIds().size() != form.getCount()
				|| form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}
		try {
			List<Map<String, Object>> results = zyCorrectingService.simpleCorrect(form.getqIds(), form.getAnswerList());
			List<HomeworkAnswerResult> rets = new ArrayList<HomeworkAnswerResult>(form.getCount());
			List<Boolean> dones = new ArrayList<Boolean>(form.getCount());
			VExerciseResult result = new VExerciseResult(2);
			result.setqIds(form.getqIds());
			int rightCount = 0;
			int doneCount = 0;
			List<Map<String, Object>> updateResults = Lists.newArrayList();
			for (Map<String, Object> map : results) {
				HomeworkAnswerResult ret = (HomeworkAnswerResult) map.get("result");
				boolean done = (Boolean) map.get("done");
				rets.add(ret);
				if (ret == HomeworkAnswerResult.RIGHT) {
					rightCount++;
				}
				if (done) {
					doneCount++;
				}
				dones.add(done);
				Map<String, Object> oneRet = Maps.newHashMap();
				oneRet.put("questionId", map.get("qId"));
				oneRet.put("done", done);
				oneRet.put("result", ret);
				oneRet.put("answer", map.get("answer"));

				updateResults.add(oneRet);
			}

			DailyPractise dailyPractise = dailyPractiseService.get(form.getPaperId());
			if (dailyPractise.getPractiseId() > 0) {
				dailyPractise = dailyPractiseService.get(dailyPractise.getPractiseId());
			}

			dailyPractise.setRightRate(
					BigDecimal.valueOf((rightCount * 100f) / form.getCount()).setScale(2, BigDecimal.ROUND_HALF_UP));
			dailyPractise.setDoCount(doneCount);
			dailyPractise.setRightCount(rightCount);
			dailyPractise.setWrongCount(form.getCount() - rightCount);
			dailyPractise.setHomeworkTime(form.getHomeworkTime());

			// 更新每日一练
			dailyPractise = dailyPractiseService.update(dailyPractise);
			dailyPractiseQuestionService.updateResults(updateResults, dailyPractise.getId(), Security.getUserId());

			result.setDones(dones);
			result.setRightRate(dailyPractise.getRightRate());
			result.setResults(rets);
			// 记录答案、统计学情、记录错题
			zyStuQaService.asynCreate(Security.getUserId(), form.getqIds(), form.getAnswerList(), form.getAnswerList(),
					null, null, null, rets, StudentQuestionAnswerSource.DAILY_PRACTICE, new Date());

			// 用户行为动作
			if (form.getType() == 1) {
				// 章节练习
				userActionService.action(UserAction.SUBMIT_SECTION_PRACTISE, Security.getUserId(), null);
			} else if (form.getType() == 2) {
				// 每日练
				userActionService.action(UserAction.SUBMIT_DAILY_PRACTISE, Security.getUserId(), null);
			}

			JSONObject messageObj = new JSONObject();
			messageObj.put("userId", Security.getUserId());
			Map<String, Object> params = new HashMap<String, Object>(1);
			params.put("rightRate", dailyPractise.getRightRate().intValue());
			messageObj.put("params", params);
			messageObj.put("isClient", Security.isClient());
			messageObj.put("taskCode", 101010004);
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
					MQ.builder().data(messageObj).build());

			// 每日练，题量任务
			JSONObject obj = new JSONObject();
			obj.put("taskCode", 101020016);
			obj.put("userId", Security.getUserId());
			obj.put("isClient", true);
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
	 * 再次练习接口
	 *
	 * @param practiseId
	 *            每日一练记录id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "doPractiseAgain", method = { RequestMethod.GET, RequestMethod.POST })
	public Value doPractiseAgain(@RequestParam(value = "id") long practiseId) {
		DailyPractise dailyPractise = dailyPractiseService.get(practiseId);
		// 备份原练习题目，备份原每日练习
		DailyPractise copyDailyPractise = dailyPractiseService.copy(dailyPractise);
		dailyPractiseQuestionService.copy(dailyPractise.getId(), copyDailyPractise.getId());

		// 清空原有的进度，正确率
		dailyPractise.setDoCount(0);
		dailyPractise.setRightCount(0);
		dailyPractise.setWrongCount(0);
		dailyPractise.setRightRate(new BigDecimal(0));

		dailyPractiseService.update(dailyPractise);

		return new Value();
	}

	/**
	 * 再次练习
	 *
	 * @since yoomath(mobile) V1.1
	 * @param practiseId
	 *            练习id
	 * @return 是否存在未完成的练习，如果有未完成的情况则返回未完成中的题目
	 */

	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "doPractiseAgain2", method = { RequestMethod.GET, RequestMethod.POST })
	public Value doPractiseAgain2(@RequestParam(value = "id") long practiseId) {
		DailyPractise dailyPractise = dailyPractiseService.get(practiseId);
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		if (dailyPractise.getPractiseId() > 0) {
			dailyPractise = dailyPractiseService.get(dailyPractise.getPractiseId());
			if (dailyPractise.getRightRate() == null) {
				List<DailyPractiseQuestion> questions = dailyPractiseQuestionService
						.findByPractise(dailyPractise.getId());
				List<VDailyPractiseQuestion> vquestions = dailyPractiseQuestionConvert.to(questions);
				retMap.put("questions", vquestions);
				retMap.put("hasFinish", false);
			} else {
				retMap.put("hasFinish", true);
			}
		} else {
			retMap.put("hasFinish", true);
		}

		return new Value(retMap);
	}

	/**
	 * 学生未做完保存答题历史
	 *
	 * @param form
	 *            {@link ExerciseCommitForm}
	 * @return 保存过后的练习id(其实是固定的跟首页的id相同，但存在从历史已完成列表中点进去此时的BizId跟首页上的列表记录是不同的。)
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "draft", method = { RequestMethod.GET, RequestMethod.POST })
	public Value draft(ExerciseCommitForm form) {
		if (form.getType() != 2 || CollectionUtils.isEmpty(form.getqIds())
				|| CollectionUtils.isEmpty(form.getAnswerList()) || form.getqIds().size() != form.getCount()
				|| form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}

		DailyPractise dailyPractise = dailyPractiseService.draft(form.getAnswerList(), form.getqIds(),
				form.getPaperId(), form.getHomeworkTime());
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		retMap.put("practiseId", dailyPractise.getId());

		// 每日练
		userActionService.action(UserAction.SUBMIT_DAILY_PRACTISE, Security.getUserId(), null);
		return new Value(retMap);
	}

	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "settingsData", method = { RequestMethod.GET, RequestMethod.POST })
	public Value settingsData() {
		return new Value(ValueMap.value("difficulty", listSettings().getRet()).put("sectionTree",
				stuUserCenterController.sectionTree(true).getRet()));
	}

	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "set", method = { RequestMethod.GET, RequestMethod.POST })
	public Value set(DailyPracticeDifficulty difficulty, long sectionCode) {
		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		if (student.getTextbookCode() == null || student.getTextbookCode() == null) {
			return new Value(new ServerException());
		}
		dailyPracticeSettingsService.set(Security.getUserId(), student.getTextbookCode(), difficulty, sectionCode);
		return new Value();
	}

}
