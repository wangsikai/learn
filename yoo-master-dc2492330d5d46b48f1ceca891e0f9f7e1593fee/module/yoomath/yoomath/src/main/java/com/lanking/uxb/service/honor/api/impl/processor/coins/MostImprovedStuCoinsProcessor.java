package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

/**
 * 进步最快学生 单次作业与上一次作业比较，进步最快的前3名分别得到30、20、10金币
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月14日 下午2:43:47
 */
@Component
public class MostImprovedStuCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private ZyStudentHomeworkService studentHomeworkService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.MOST_IMPROVED_STU;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		Long classId = coinsLog.getBizId();
		List<Long> improveUserIds = Lists.newArrayList();
		improveUserIds = studentHomeworkService.getMostImprovedStu(classId, 3);
		if (improveUserIds == null || improveUserIds.isEmpty()) {
			return null;
		}
		List<Integer> ruleList = JSONArray.parseArray(getCoinsRule().getRule(), Integer.class);
		Date now = new Date();
		for (int i = 0; i < improveUserIds.size(); i++) {
			CoinsLog log = new CoinsLog();
			String todayKey = coinsCacheService.getTodayKey(getAction(), improveUserIds.get(i));
			long count = coinsCacheService.get(todayKey);
			if (count == -1) {
				log.setBiz(coinsLog.getBiz());
				log.setBizId(coinsLog.getBizId());
				log.setUserId(improveUserIds.get(i));
				log.setCreateAt(now);
				log.setRuleCode(getCoinsRule().getCode());
				log.setCoinsValue(ruleList.get(i) == null ? 0 : ruleList.get(i));
				log.setStatus(Status.ENABLED);
				coinsLogService.save(log);
				update(log);
				coinsCacheService.incr(todayKey);
			}
		}
		return coinsLog;
	}
}
