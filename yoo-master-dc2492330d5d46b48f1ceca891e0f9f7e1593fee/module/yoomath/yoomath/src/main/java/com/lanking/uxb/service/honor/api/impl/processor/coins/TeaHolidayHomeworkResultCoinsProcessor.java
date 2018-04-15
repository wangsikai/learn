package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsRule;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.CoinsRuleService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

/**
 * 老师假期作业完成率在80%得100金币
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月30日 上午10:05:14
 */
@Component
public class TeaHolidayHomeworkResultCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private HolidayHomeworkService hdHomeworkService;
	@Autowired
	private ZyHomeworkClassService classService;
	@Autowired
	private CoinsRuleService coinsRuleService;

	@Autowired
	@Override
	public CoinsAction getAction() {
		return CoinsAction.TEA_HOLIDAY_HOMEWORK_RESULT;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		String oneTimeKey = coinsCacheService.getOneTimeKey(getAction(), coinsLog.getBizId());
		long count = coinsCacheService.get(oneTimeKey);
		if (count >= -1) {
			HolidayHomework holidayHomework = hdHomeworkService.get(coinsLog.getBizId());
			int stuNum = classService.get(holidayHomework.getHomeworkClassId()).getStudentNum();
			// holidayHomework.getCompletionRate()为空默认为学生没有提交
			if (stuNum < Env.getInt("holiday.homework.stu.numLimit")
					|| holidayHomework.getCompletionRate() == null
					|| holidayHomework.getCompletionRate().compareTo(
							BigDecimal.valueOf(Env.getInt("holiday.homework.rightRate"))) < 0) {
				return coinsLog;
			}
			// 查看此份作业是否是获得过布置作业的奖励
			CoinsRule coinsRule = coinsRuleService.getByAction(CoinsAction.FIRST_PUBLISH_HOLIDAY_HOMEWORK);
			CoinsLog coinsLogTemp = coinsLogService
					.find(coinsRule.getCode(), coinsLog.getUserId(), coinsLog.getBizId());
			if (coinsLogTemp == null) {
				return coinsLog;
			}
			coinsLog.setCreateAt(new Date());
			coinsLog.setRuleCode(getCoinsRule().getCode());
			coinsLog.setCoinsValue(getCoinsRule().getCoinsValue());
			coinsLog.setStatus(Status.ENABLED);
			coinsLogService.save(coinsLog);
			update(coinsLog);
			coinsCacheService.incr(oneTimeKey);
		}
		return coinsLog;
	}
}
