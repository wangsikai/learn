package com.lanking.uxb.service.web.resource;

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
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractiseQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.web.form.PractiseCommitForm;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyDailyPractiseConvert;
import com.lanking.uxb.service.zuoye.convert.ZyDailyPractiseQuestionConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.value.VDailyPractise;
import com.lanking.uxb.service.zuoye.value.VDailyPractiseQuestion;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;

/**
 * 每日练接口
 *
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@RestController
@RequestMapping("zy/s/dp")
public class ZyStuDailyPractiseController {

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
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 分页查询每日练习记录
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryDailyPractise", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryDailyPractise(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size) {

		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		if (student.getTextbookCode() == null) {
			return new Value();
		}

		Pageable pageable = P.index(page, size);
		Page<DailyPractise> result = dailyPractiseService.query(student.getId(), student.getTextbookCode(), pageable);
		VPage<VDailyPractise> retPage = new VPage<VDailyPractise>();
		List<VDailyPractise> items = dailyPractiseConvert.to(result.getItems());
		retPage.setItems(items);
		retPage.setCurrentPage(page);
		retPage.setPageSize(size);
		retPage.setTotal(result.getTotalCount());
		retPage.setTotalPage(result.getPageCount());

		return new Value(retPage);
	}

	/**
	 * 查询每日练习的题目列表
	 *
	 * @param practiseId
	 *            每日练习的id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryPractiseQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryPractiseQuestions(long practiseId) {
		DailyPractise dailyPractise = dailyPractiseService.get(practiseId);
		if (dailyPractise == null) {
			return new Value(new IllegalArgException());
		}

		Map<String, Object> retMap = new HashMap<String, Object>(3);
		List<DailyPractiseQuestion> questions = dailyPractiseQuestionService.findByPractise(practiseId);
		List<VDailyPractiseQuestion> vquestions = dailyPractiseQuestionConvert.to(questions);

		retMap.put("questions", vquestions);
		retMap.put("practise", dailyPractiseConvert.to(dailyPractise));
		List<Long> qIds = new ArrayList<Long>(questions.size());
		for (DailyPractiseQuestion p : questions) {
			qIds.add(p.getQuestionId());
		}

		retMap.put("questionExNum", sfqService.mgetQuestionExerciseNums(qIds, Security.getUserId()));
		return new Value(retMap);
	}

	/**
	 * 提交练习
	 *
	 * @param form
	 *            {@link PractiseCommitForm}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "commit", method = { RequestMethod.GET, RequestMethod.POST })
	public Value commit(PractiseCommitForm form) {
		if (CollectionUtils.isEmpty(form.getqIds()) || CollectionUtils.isEmpty(form.getAnswerList())
				|| form.getqIds().size() != form.getCount() || form.getAnswerList().size() != form.getCount()) {
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

			// 更新题目答案
			dailyPractiseQuestionService.updateResults(updateResults, dailyPractise.getId(), Security.getUserId());

			result.setDones(dones);
			result.setRightRate(dailyPractise.getRightRate());
			result.setResults(rets);
			// 记录答案、统计学情、记录错题
			zyStuQaService.asynCreate(Security.getUserId(), form.getqIds(), form.getAnswerList(), form.getAnswerList(),
					null, null, null, rets, StudentQuestionAnswerSource.DAILY_PRACTICE, new Date());

			JSONObject messageObj = new JSONObject();
			messageObj.put("taskCode", 101010004);
			messageObj.put("userId", Security.getUserId());
			messageObj.put("isClient", Security.isClient());
			Map<String, Object> params = new HashMap<String, Object>(1);
			params.put("rightRate", dailyPractise.getRightRate().intValue());
			messageObj.put("params", params);
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
					MQ.builder().data(messageObj).build());

			// 每日练习，题量任务
			JSONObject obj = new JSONObject();
			obj.put("taskCode", 101020016);
			obj.put("userId", Security.getUserId());
			obj.put("isClient", Security.isClient()); // web端
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
	 * 再次练习
	 *
	 * @since 2.0.3
	 * @param practiseId
	 *            练习id
	 * @return 是否存在未完成的练习，如果有未完成的情况则返回未完成中的题目
	 */

	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "doPractiseAgain", method = { RequestMethod.GET, RequestMethod.POST })
	public Value doPractiseAgain(@RequestParam(value = "id") long practiseId) {
		List<DailyPractiseQuestion> questions = dailyPractiseQuestionService.findByPractise(practiseId);
		List<Map<Long, List<String>>> answerList = new ArrayList<Map<Long, List<String>>>(questions.size());

		List<Long> qIds = new ArrayList<Long>(questions.size());
		for (DailyPractiseQuestion q : questions) {
			qIds.add(q.getQuestionId());
			Map<Long, List<String>> m = new HashMap<Long, List<String>>(1);
			m.put(q.getQuestionId(), null);
			answerList.add(m);
		}

		dailyPractiseService.draft(answerList, qIds, practiseId, 0);

		return new Value();
	}

	/**
	 * 学生未做完保存答题历史
	 *
	 * @param form
	 *            {@link PractiseCommitForm}
	 * @return 保存过后的练习id
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "draft", method = { RequestMethod.GET, RequestMethod.POST })
	public Value draft(PractiseCommitForm form) {
		if (CollectionUtils.isEmpty(form.getqIds()) || CollectionUtils.isEmpty(form.getAnswerList())
				|| form.getqIds().size() != form.getCount() || form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}

		DailyPractise dailyPractise = dailyPractiseService.draft(form.getAnswerList(), form.getqIds(),
				form.getPaperId(), form.getHomeworkTime());
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		retMap.put("practiseId", dailyPractise.getId());

		return new Value(retMap);
	}

	/**
	 * 每日练保存一题
	 * 
	 * @param form
	 *            {@link PractiseCommitForm}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "doOne", method = { RequestMethod.GET, RequestMethod.POST })
	public Value doOne(PractiseCommitForm form) {
		if (CollectionUtils.isEmpty(form.getAnswerList()) || form.getDailyQuestionId() == null
				|| form.getPaperId() == null) {
			return new Value(new IllegalArgException());
		}

		try {
			dailyPractiseService.doOne(form.getAnswerList().get(0), form.getDailyQuestionId(), form.getPaperId(),
					form.getHomeworkTime());
		} catch (ZuoyeException e) {
			return new Value(e);
		}

		return new Value();
	}

}
