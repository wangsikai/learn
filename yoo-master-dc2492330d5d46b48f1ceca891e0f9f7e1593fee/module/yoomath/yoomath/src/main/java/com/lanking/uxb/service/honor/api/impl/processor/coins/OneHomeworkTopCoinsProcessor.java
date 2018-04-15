package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

/**
 * 单次作业TOP5
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月14日 下午12:01:55
 */
@Component
public class OneHomeworkTopCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private ZyStudentHomeworkService studentHomeworkService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.ONE_HOMEWORK_TOP5;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		Long hkId = coinsLog.getBizId();
		if (homeworkService.get(hkId).getCommitCount() < Env.getInt("holiday.homework.commit.numLimit")) {
			return coinsLog;
		}
		List<StudentHomework> shs = studentHomeworkService.listTop5ByHomework(hkId, 5);
		List<Integer> ruleList = JSONArray.parseArray(getCoinsRule().getRule(), Integer.class);
		Date now = new Date();
		for (int i = 0; i < shs.size(); i++) {
			CoinsLog log = new CoinsLog();
			String todayKey = coinsCacheService.getTodayKey(getAction(), shs.get(i).getStudentId());
			long count = coinsCacheService.get(todayKey);
			if (count == -1) {
				log.setBiz(coinsLog.getBiz());
				log.setBizId(coinsLog.getBizId());
				log.setUserId(shs.get(i).getStudentId());
				log.setCreateAt(now);
				log.setRuleCode(getCoinsRule().getCode());
				log.setCoinsValue(ruleList.get(i) == null ? 0 : ruleList.get(i));
				log.setStatus(Status.ENABLED);
				coinsLogService.save(log);
				update(log);
				coinsCacheService.incr(todayKey);
			}
		}
		return null;
	}
}
