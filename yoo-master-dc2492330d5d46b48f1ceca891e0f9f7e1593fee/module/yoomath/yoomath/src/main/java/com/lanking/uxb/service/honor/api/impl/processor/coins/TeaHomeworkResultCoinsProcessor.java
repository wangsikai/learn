package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;
import com.lanking.uxb.service.resources.api.HomeworkService;

/**
 * 教师作业完成率 完成率80%以上得10金币、90%以上得20金币、100%得30金币
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月14日 上午9:42:19
 */
@Component
public class TeaHomeworkResultCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private HomeworkService homeworkService;

	@Autowired
	@Override
	public CoinsAction getAction() {
		return CoinsAction.TEA_HOMEOWORK_RESULT;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		String timeKey = coinsCacheService.getTodayKey(getAction(), coinsLog.getUserId());
		long count = coinsCacheService.get(timeKey);
		List<Integer> ruleList = JSONArray.parseArray(getCoinsRule().getRule(), Integer.class);
		if (count < ruleList.size()) {
			Homework homework = homeworkService.get(coinsLog.getBizId());
			if (homework.getCommitCount() < Env.getInt("holiday.homework.commit.numLimit")) {
				return coinsLog;
			}
			Double finshRate = homework.getCommitCount() * 1.0 / homework.getDistributeCount();
			int coinsValue = 0;
			if (finshRate == 1.0) {
				coinsValue = ruleList.get(0) == null ? 0 : ruleList.get(0);
			} else if (finshRate >= 0.9) {
				coinsValue = ruleList.get(1) == null ? 0 : ruleList.get(1);
			} else if (finshRate >= 0.8) {
				coinsValue = ruleList.get(2) == null ? 0 : ruleList.get(2);
			} else {
				return coinsLog;
			}
			coinsLog.setCreateAt(new Date());
			coinsLog.setRuleCode(getCoinsRule().getCode());
			coinsLog.setCoinsValue(coinsValue);
			coinsLog.setStatus(Status.ENABLED);
			coinsLogService.save(coinsLog);
			update(coinsLog);
			coinsCacheService.incr(timeKey);
		}
		return coinsLog;
	}
}
