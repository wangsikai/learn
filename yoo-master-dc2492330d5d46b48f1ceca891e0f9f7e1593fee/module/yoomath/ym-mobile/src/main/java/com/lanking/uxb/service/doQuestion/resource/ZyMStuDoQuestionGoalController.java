package com.lanking.uxb.service.doQuestion.resource;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoal;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoalCount;
import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoalLevel;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.doQuestion.api.DoQuestionGoalService;
import com.lanking.uxb.service.doQuestion.convert.DoQuestionGoalConvert;
import com.lanking.uxb.service.doQuestion.convert.DoQuestionGoalLevelConvert;
import com.lanking.uxb.service.doQuestion.value.VDoQuestionGoal;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 做题目标相关
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月28日
 */
@RestController
@RequestMapping("zy/m/s/doQuestion")
public class ZyMStuDoQuestionGoalController {

	@Autowired
	private DoQuestionGoalLevelConvert doQuestionGoalLevelConvert;
	@Autowired
	private DoQuestionGoalService doQuestionGoalService;
	@Autowired
	private DoQuestionGoalConvert doQuestionGoalConvert;
	@Autowired
	private MqSender mqSender;

	/**
	 * 获取设置页面数据的接口
	 * 
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index() {
		Map<String, Object> data = new HashMap<String, Object>(2);
		DoQuestionGoalLevel[] levels = DoQuestionGoalLevel.values();
		List<DoQuestionGoalLevel> levelList = Arrays.asList(levels);
		Collections.reverse(levelList);
		data.put("goals", doQuestionGoalLevelConvert.to(levelList));
		DoQuestionGoal goal = doQuestionGoalService.findByUserId(Security.getUserId());
		if (goal != null) {
			VDoQuestionGoal vmyGoal = doQuestionGoalConvert.to(goal);
			long date0 = Long.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date()));
			DoQuestionGoalCount myGoalCount = doQuestionGoalService.findByUserId(Security.getUserId(), date0);
			vmyGoal.setCompletedGoal(myGoalCount != null ? myGoalCount.getGoal() : 0);
			data.put("myGoal", vmyGoal);
		}
		return new Value(data);
	}

	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "setGoal", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setGoal(DoQuestionGoalLevel level, Integer goal) {
		if (level == null) {
			return new Value(new IllegalArgException());
		}
		if (level == DoQuestionGoalLevel.LEVEL_0 && (goal == null || goal <= 0)) {
			return new Value(new IllegalArgException());
		}
		doQuestionGoalService.setDoQuestionGoal(Security.getUserId(), level,
				level == DoQuestionGoalLevel.LEVEL_0 ? goal : level.getGoal());

		// 用户任务
		JSONObject messageObj = new JSONObject();
		messageObj.put("taskCode", 101000002);
		messageObj.put("userId", Security.getUserId());
		messageObj.put("isClient", Security.isClient());
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(messageObj).build());

		return new Value();
	}
}
