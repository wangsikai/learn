package com.lanking.uxb.service.honor.api.impl.processor.coins;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.CoinsRuleService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractCoinsProcessor;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 学生人数达到200 金币相关
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月10日 下午1:31:14
 */
@Component
public class ClassStuNumCoinsProcessor extends AbstractCoinsProcessor {

	@Autowired
	private CoinsCacheService coinsCacheService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private CoinsRuleService coinsRuleService;

	@Override
	public CoinsAction getAction() {
		return CoinsAction.CLASS_STU_NUM;
	}

	@Transactional
	@Override
	public CoinsLog process(CoinsLog coinsLog) {
		HomeworkClazz clazz = zyHkClassService.get(coinsLog.getBizId());
		if (clazz.getStudentNum() < Env.getInt("holiday.homework.stu.numLimit")) {
			return null;
		}
		JSONObject jsonRule = new JSONObject(getCoinsRule().getRule());
		int code = coinsRuleService.getByAction(CoinsAction.CLASS_STU_NUM).getCode();
		Integer countAction = coinsLogService.countActionByUser(code, coinsLog.getUserId(), null, null);
		if (countAction == null) {
			countAction = 0;
		}
		if (countAction >= jsonRule.getInt("max")) {
			return null;
		}
		String oneTimekey = coinsCacheService.getOneTimeKey(getAction(), coinsLog.getBizId());

		long count = coinsCacheService.get(oneTimekey);
		if (count == -1) {
			coinsLog.setCreateAt(new Date());
			coinsLog.setRuleCode(getCoinsRule().getCode());
			coinsLog.setCoinsValue(getCoinsRule().getCoinsValue());
			coinsLog.setStatus(Status.ENABLED);
			coinsLogService.save(coinsLog);
			update(coinsLog);
			coinsCacheService.incr(oneTimekey);
		}
		return coinsLog;
	}
}
