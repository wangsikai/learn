package com.lanking.uxb.service.honor.api.impl.processor.growth;

import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.GrowthRuleService;
import com.lanking.uxb.service.honor.api.impl.processor.AbstractGrowthProcessor;
import com.lanking.uxb.service.honor.cache.GrowthCacheService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 学生人数超过20 上限300 一次100 一天最多三次
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月10日 下午1:28:04
 */
@Component
public class ClassStuNumGrowthProcessor extends AbstractGrowthProcessor {

	@Autowired
	private GrowthCacheService growthCacheService;
	@Autowired
	private GrowthLogService growthLogService;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private GrowthRuleService growthRuleService;

	@Override
	public GrowthAction getAction() {
		return GrowthAction.CLASS_STU_NUM;
	}

	@Transactional
	@Override
	public GrowthLog process(GrowthLog growthLog, boolean isUpgrade) {
		HomeworkClazz clazz = zyHkClassService.get(growthLog.getBizId());
		if (clazz.getStudentNum() < Env.getInt("holiday.homework.stu.numLimit")) {
			return null;
		}
		JSONObject jsonRule = new JSONObject(getGrowthRule().getRule());
		int code = growthRuleService.getByAction(GrowthAction.CLASS_STU_NUM).getCode();
		Integer countAction = growthLogService.countActionByUser(code, growthLog.getUserId(), null, null);
		if (countAction == null) {
			countAction = 0;
		}
		if (countAction >= jsonRule.getInt("max")) {
			return null;
		}
		String oneTimeKey = growthCacheService.getOneTimeKey(getAction(), growthLog.getBizId());
		long count = growthCacheService.get(oneTimeKey);
		if (count == -1) {
			growthLog.setCreateAt(new Date());
			growthLog.setRuleCode(getGrowthRule().getCode());
			if (growthLog.getGrowthValue() == -1) {
				growthLog.setGrowthValue(getGrowthRule().getGrowthValue());
			}
			growthLog.setStatus(Status.ENABLED);
			growthLogService.save(growthLog);
			UserHonor userHonor = update(growthLog);
			if (isUpgrade) {
				growthLog.setHonor(userHonor);
			}
			growthCacheService.incr(oneTimeKey);
		}
		return growthLog;
	}
}
