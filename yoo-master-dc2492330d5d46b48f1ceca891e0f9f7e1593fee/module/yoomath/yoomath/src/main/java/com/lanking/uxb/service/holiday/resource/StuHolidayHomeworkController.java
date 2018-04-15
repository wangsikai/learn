package com.lanking.uxb.service.holiday.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHolidayHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.common.convert.QuestionBaseConvert;
import com.lanking.uxb.service.common.convert.QuestionBaseConvertOption;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemAnswerService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayHomeworkConvert;
import com.lanking.uxb.service.holiday.convert.HolidayQuestionConvert;
import com.lanking.uxb.service.holiday.convert.HolidayQuestionConvertOption;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkItemConvert;
import com.lanking.uxb.service.holiday.form.StuItemAnswerForm;
import com.lanking.uxb.service.holiday.value.VHolidayQuestion;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 学生假日作业相关rest API(除查看以外的接口)
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月23日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/holiday")
public class StuHolidayHomeworkController {
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;
	@Autowired
	private HolidayHomeworkItemQuestionService holidayHomeworkItemQuestionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionBaseConvert<VHolidayQuestion> questionBaseConvert;
	@Autowired
	private HolidayStuHomeworkItemAnswerService holidayStuHomeworkItemAnswerService;
	@Autowired
	private HolidayStuHomeworkItemService itemService;
	@Autowired
	private HolidayStuHomeworkItemConvert holidayStuHomeworkItemConvert;
	@Autowired
	private HolidayQuestionConvert holidayQuestionConvert;
	@Autowired
	private HolidayHomeworkConvert holidayHomeworkConvert;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private ZyStudentFallibleQuestionService zyStudentFallibleQuestionService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 学生做寒假作业过程中的数据获取
	 * 
	 * @param holidayStuHomeworkItemId
	 *            学生假期专项作业ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "doing_view", method = { RequestMethod.POST, RequestMethod.GET })
	public Value doingView(long holidayStuHomeworkItemId) {
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(holidayStuHomeworkItemId);
		if (null == holidayStuHomeworkItem) {
			return new Value(new EntityNotFoundException());
		}
		Map<String, Object> data = new HashMap<String, Object>(3);
		HolidayHomework holidayHomework = holidayHomeworkService.get(holidayStuHomeworkItem.getHolidayHomeworkId());
		List<Long> questionIds = holidayHomeworkItemQuestionService
				.queryQuestions(holidayStuHomeworkItem.getHolidayHomeworkItemId());
		List<Question> qs = questionService.mgetList(questionIds);
		List<VHolidayQuestion> vqs = questionBaseConvert.to(qs,
				new QuestionBaseConvertOption(false, false, false, false));
		data.put("questions",
				holidayQuestionConvert.to(vqs, new HolidayQuestionConvertOption(holidayStuHomeworkItemId)));
		Date d = holidayHomework.getDeadline();
		long deadline = d.getTime() - new Date().getTime();
		data.put("deadline", deadline < 0 ? 0 : deadline);
		data.put("holidayStuHomeworkItem", holidayStuHomeworkItemConvert.to(holidayStuHomeworkItem));
		return new Value(data);
	}

	/**
	 * 做题,保存
	 * 
	 * @param stuItemAnswerForm
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "do_one", method = { RequestMethod.POST, RequestMethod.GET })
	public Value doOne(StuItemAnswerForm stuItemAnswerForm) {
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService
				.get(stuItemAnswerForm.getHolidayStuHomeworkItemId());
		if (holidayStuHomeworkItem == null
				|| holidayStuHomeworkItem.getStudentId().longValue() != Security.getUserId()) {
			// 不能做别人的作业
			return new Value(new NoPermissionException());
		}
		if (holidayStuHomeworkItem.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_STUDENT_COMMITED));
		}
		Map<Long, List<String>> answerData = Maps.newHashMap();
		Map<Long, List<String>> answerAsciiData = Maps.newHashMap();
		JSONArray answerArray = JSONArray.parseArray(stuItemAnswerForm.getAnswer());
		for (int i = 0; i < answerArray.size(); i++) {
			JSONObject answerObject = (JSONObject) answerArray.get(i);
			List<String> answerList = Lists.newArrayList();
			List<String> answerAsciiList = Lists.newArrayList();
			JSONArray answers = answerObject.getJSONArray("answers");
			for (Object object : answers) {
				answerList.add(((JSONObject) object).getString("content"));
				answerAsciiList.add(((JSONObject) object).getString("contentAscii"));
			}
			answerData.put(answerObject.getLong("stuQuestionId"), answerList);
			answerAsciiData.put(answerObject.getLong("stuQuestionId"), answerAsciiList);
		}
		stuItemAnswerForm.setAnswerData(answerData);
		stuItemAnswerForm.setAnswerAsciiData(answerAsciiData);
		stuItemAnswerForm.setStudentId(Security.getUserId());
		holidayStuHomeworkItemAnswerService.saveAnswer(stuItemAnswerForm);
		if (stuItemAnswerForm.getUpdateData()) {
			Question qs = questionService.get(stuItemAnswerForm.getQuestionId());
			VHolidayQuestion vqs = questionBaseConvert.to(qs, new QuestionBaseConvertOption(false, true, true, true));
			return new Value(holidayQuestionConvert.to(vqs,
					new HolidayQuestionConvertOption(stuItemAnswerForm.getHolidayStuHomeworkItemId())));
		}
		return new Value();
	}

	/**
	 * 学生提交专项作业
	 * 
	 * @param holidayStuHomeworkItemId
	 *            学生假期作业专项Id
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "commit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value commit(long holidayStuHomeworkItemId) {
		VUserReward vUserReward = null;
		HolidayStuHomeworkItem hsh = itemService.get(holidayStuHomeworkItemId);
		// 如果学生作业已提交则直接结束
		if (hsh.getStatus() == StudentHomeworkStatus.SUBMITED) {
			return new Value(vUserReward);
		}
		itemService.updateStudentHkStatus(holidayStuHomeworkItemId, 100);
		itemService.correctHolidayStuHk(holidayStuHomeworkItemId);
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(holidayStuHomeworkItemId);
		holidayStuHomeworkService.uptStuHomeworkCompleteRate(holidayStuHomeworkItem.getHolidayStuHomeworkId());
		// 更新假期作业表总的完成率
		holidayHomeworkService
				.uptHolidayHomeworkCompleRate(itemService.get(holidayStuHomeworkItemId).getHolidayHomeworkId());

		// GrowthLog growthlog =
		// growthService.grow(GrowthAction.FINISH_HOLIDAY_HOMEWORK,
		// Security.getUserId(), true);
		// CoinsLog coinslog =
		// coinsService.earn(CoinsAction.FINISH_HOLIDAY_HOMEWORK,
		// Security.getUserId());
		// if (growthlog.getHonor() != null) {
		// vUserReward = new
		// VUserReward(growthlog.getHonor().getUpRewardCoins(),
		// growthlog.getHonor().isUpgrade(),
		// growthlog.getHonor().getLevel(), growthlog.getGrowthValue(),
		// coinslog.getCoinsValue());
		// }

		// 用户任务处理
		JSONObject messageObj = new JSONObject();
		messageObj.put("taskCode", 101010002);
		messageObj.put("userId", Security.getUserId());
		messageObj.put("isClient", Security.isClient());
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(messageObj).build());

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("studentId", Security.getUserId());
		jsonObject.put("holidayStuHomeworkItemId", holidayStuHomeworkItemId);

		mqSender.send(MqYoomathHolidayHomeworkRegistryConstants.EX_YM_HOLIDAYHOMEWORK,
				MqYoomathHolidayHomeworkRegistryConstants.RK_YM_HOLIDAYHOMEWORK_COMMIT,
				MQ.builder().data(jsonObject).build());
		return new Value(vUserReward);
	}

	/**
	 * 查看当前假期作业左侧专项列表
	 * 
	 * @param holidayHomeworkId
	 *            假期作业ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryHolidayHkItems", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHolidayHkItems(long holidayStuHomeworkItemId) {
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(holidayStuHomeworkItemId);
		List<HolidayStuHomeworkItem> itemList = itemService
				.queryStuHkItems(holidayStuHomeworkItem.getHolidayHomeworkId(), Security.getUserId(), null, null);
		HolidayHomework holidayHomework = holidayHomeworkService.get(holidayStuHomeworkItem.getHolidayHomeworkId());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("itemList", holidayStuHomeworkItemConvert.to(itemList));
		data.put("holidayHomework", holidayHomeworkConvert.to(holidayHomework));
		return new Value(data);
	}

	/**
	 * 查询专项对应的题目情况
	 * 
	 * @param holidayHomeworkItemId
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryQuestionsByItem", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryQuestionsByItem(long holidayStuHomeworkItemId) {
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(holidayStuHomeworkItemId);
		List<Long> questionIds = holidayHomeworkItemQuestionService
				.queryQuestions(holidayStuHomeworkItem.getHolidayHomeworkItemId());
		Map<Long, Question> questionMap = questionService.mget(questionIds);
		List<Question> qs = new ArrayList<Question>();
		for (Long questionId : questionIds) {
			if (questionMap.get(questionId) != null) {
				qs.add(questionMap.get(questionId));
			}
		}
		List<VHolidayQuestion> vqs = questionBaseConvert.to(qs,
				new QuestionBaseConvertOption(false, true, true, false));
		Map<Long, StudentFallibleQuestion> fallMap = zyStudentFallibleQuestionService.mgetQuestion(questionIds,
				Security.getUserId());
		for (VHolidayQuestion v : vqs) {
			if (fallMap.get(v.getId()) != null) {
				// 删除掉的就不显示
				v.setInStuFallQuestion(true);
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("questions",
				holidayQuestionConvert.to(vqs, new HolidayQuestionConvertOption(holidayStuHomeworkItemId)));
		data.put("holidayStuHomeworkItem", holidayStuHomeworkItemConvert.to(holidayStuHomeworkItem));
		return new Value(data);
	}

	/**
	 * 获取班级每个习题的正确率情况
	 * 
	 * @param homeworkId
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "getClazzQuestionStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getClazzQuestionStat(Long holidayStuHomeworkItemId) {
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(holidayStuHomeworkItemId);
		List<Double> list = holidayHomeworkItemQuestionService
				.getRateStat(holidayStuHomeworkItem.getHolidayHomeworkItemId());
		return new Value(list);
	}

	/**
	 * 获取班级相关的专项统计
	 * 
	 * @param holidayStuHomeworkItemId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "getClazzStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getClazzStat(Long holidayStuHomeworkId) {
		List<Map> statistic = itemService.getClazzStat(holidayStuHomeworkId);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("statistic", statistic);
		return new Value(data);
	}

	/**
	 * 更新完成率
	 * 
	 * @param holidayStuHomeworkItemId
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "updateComplete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateComplete(Long holidayStuHomeworkItemId, Double completeRate) {
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(holidayStuHomeworkItemId);
		itemService.updateStuItemCompleteRate(holidayStuHomeworkItemId, completeRate);
		holidayStuHomeworkService.uptStuHomeworkCompleteRate(holidayStuHomeworkItem.getHolidayStuHomeworkId());
		return new Value();
	}
}
