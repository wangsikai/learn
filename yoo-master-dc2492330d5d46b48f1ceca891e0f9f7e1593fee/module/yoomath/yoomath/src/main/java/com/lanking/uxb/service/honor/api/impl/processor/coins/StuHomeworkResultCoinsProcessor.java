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
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;

/**
 * 学生作业完成效果 正确率80%以上得10金币、90%以上得20金币、100%得30金币
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月14日 上午9:42:19
 */
@Component
public class StuHomeworkResultCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private StudentHomeworkService shkService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.STU_HOMEWORK_RESULT;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		Long homeworkId = coinsLog.getBizId();
		List<StudentHomework> shList = shkService.listByHomework(homeworkId);
		for (StudentHomework studentHomework : shList) {
			CoinsLog log = new CoinsLog();
			String timeKey = coinsCacheService.getTodayKey(getAction(), studentHomework.getStudentId());
			long count = coinsCacheService.get(timeKey);
			List<Integer> ruleList = JSONArray.parseArray(getCoinsRule().getRule(), Integer.class);
			if (count < ruleList.size()) {
				if (studentHomework.getRightRate() == null) {
					continue;
				}
				Integer rightRate = Integer.valueOf(studentHomework.getRightRate().toString());
				int coinsValue = 0;
				if (rightRate == 100) {
					coinsValue = ruleList.get(0) == null ? 0 : ruleList.get(0);
				} else if (rightRate >= 90) {
					coinsValue = ruleList.get(1) == null ? 0 : ruleList.get(1);
				} else if (rightRate >= 80) {
					coinsValue = ruleList.get(2) == null ? 0 : ruleList.get(2);
				} else {
					continue;
				}
				log.setUserId(studentHomework.getStudentId());
				log.setBiz(coinsLog.getBiz());
				log.setBizId(coinsLog.getBizId());
				log.setCreateAt(new Date());
				log.setRuleCode(getCoinsRule().getCode());
				log.setCoinsValue(coinsValue);
				log.setStatus(Status.ENABLED);
				coinsLogService.save(log);
				update(log);
				coinsCacheService.incr(timeKey);
			}
		}
		return null;
	}
}
