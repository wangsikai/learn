package com.lanking.uxb.service.honor.api.impl.processor.growth;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.GrowthRuleService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractGrowthProcessor;

/**
 * 30天内首次布置假期作业相关
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月21日 下午4:00:18
 */
@Component
public class FirstPublishHolidayHomeworkGrowthProcessor extends AbstractGrowthProcessor {

	@Autowired
	private GrowthRuleService growthRuleService;
	@Autowired
	private GrowthLogService growthLogService;

	@Override
	public GrowthAction getAction() {
		return GrowthAction.FIRST_PUBLISH_HOLIDAY_HOMEWORK;
	}

	@Transactional
	@Override
	public GrowthLog process(GrowthLog growthLog, boolean isUpgrade) {
		int code = growthRuleService.getByAction(GrowthAction.FIRST_PUBLISH_HOLIDAY_HOMEWORK).getCode();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// 30天以前
		cal.add(Calendar.DAY_OF_YEAR, -Env.getInt("holiday.homework.dayRange"));
		Integer countAction = growthLogService.countActionByUser(code, growthLog.getUserId(),
				new Date(cal.getTimeInMillis()), new Date());
		if (countAction >= 1) {
			return growthLog;
		}
		growthLog.setCreateAt(new Date());
		growthLog.setRuleCode(getGrowthRule().getCode());
		growthLog.setGrowthValue(getGrowthRule().getGrowthValue());
		growthLog.setStatus(Status.ENABLED);
		growthLogService.save(growthLog);
		UserHonor userHonor = update(growthLog);
		if (isUpgrade) {
			growthLog.setHonor(userHonor);
		}
		return growthLog;
	}
}
