package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.CoinsRuleService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;

/**
 * 30天内首次布置假期作业
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月21日 下午5:04:40
 */
@Component
public class FirstPublishHolidayHomeworkCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsRuleService coinsRuleService;
	@Autowired
	private CoinsLogService coinsLogService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.FIRST_PUBLISH_HOLIDAY_HOMEWORK;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		int code = coinsRuleService.getByAction(CoinsAction.FIRST_PUBLISH_HOLIDAY_HOMEWORK).getCode();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		// 30天以前
		cal.add(Calendar.DAY_OF_YEAR, -Env.getInt("holiday.homework.dayRange"));
		Integer countAction = coinsLogService.countActionByUser(code, coinsLog.getUserId(),
				new Date(cal.getTimeInMillis()), new Date());
		if (countAction >= 1) {
			return coinsLog;
		}
		coinsLog.setBiz(coinsLog.getBiz());
		coinsLog.setBizId(coinsLog.getBizId());
		coinsLog.setCreateAt(new Date());
		coinsLog.setRuleCode(getCoinsRule().getCode());
		coinsLog.setCoinsValue(getCoinsRule().getCoinsValue());
		coinsLog.setStatus(Status.ENABLED);
		coinsLogService.save(coinsLog);
		update(coinsLog);
		return coinsLog;
	}
}
