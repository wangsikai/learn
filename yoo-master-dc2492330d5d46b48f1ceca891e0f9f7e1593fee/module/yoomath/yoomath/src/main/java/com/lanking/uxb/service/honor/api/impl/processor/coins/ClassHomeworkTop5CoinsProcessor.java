package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStudentClazzStat;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzStatService;

/**
 * 班级作业TOP5
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月14日 下午12:01:55
 */
@Component
public class ClassHomeworkTop5CoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private StudentHomeworkConvert studentHomeworkConvert;
	@Autowired
	private ZyHomeworkStudentClazzStatService zyHomeworkStudentClazzStatService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.CLASS_HOMEWORK_TOP5;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		Long classId = coinsLog.getBizId();
		List<HomeworkStudentClazzStat> vhks = zyHomeworkStudentClazzStatService.findTopStudent(classId, 5);
		Date now = new Date();
		for (int i = 0; i < vhks.size(); i++) {
			CoinsLog log = new CoinsLog();
			String todayKey = coinsCacheService.getTodayKey(getAction(), vhks.get(i).getStudentId());
			long count = coinsCacheService.get(todayKey);
			if (count == -1) {
				int reward = 0;
				String yesterdayKey = coinsCacheService.getYesterdayKey(getAction(), vhks.get(i).getStudentId());
				long countTemp = coinsCacheService.get(yesterdayKey);
				// 昨天已经加过了，今天就在加1
				if (countTemp > -1) {
					reward = 1;
				}
				log.setBiz(coinsLog.getBiz());
				log.setBizId(coinsLog.getBizId());
				log.setUserId(vhks.get(i).getStudentId());
				log.setCreateAt(now);
				log.setRuleCode(getCoinsRule().getCode());
				log.setCoinsValue(getCoinsRule().getCoinsValue() + reward);
				log.setStatus(Status.ENABLED);
				coinsLogService.save(log);
				update(log);
				coinsCacheService.incr(todayKey);
			}
		}
		return null;
	}
}
