package com.lanking.uxb.service.honor.api.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthRule;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.GrowthRuleService;
import com.lanking.uxb.service.honor.cache.GrowthCacheService;

@Transactional(readOnly = true)
@Service
public class GrowthLogServiceImpl implements GrowthLogService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	@Qualifier("GrowthLogRepo")
	private Repo<GrowthLog, Long> growthLogRepo;

	@Autowired
	private GrowthRuleService growthRuleService;
	@Autowired
	private GrowthCacheService growthCacheService;

	@Override
	public GrowthLog find(GrowthAction action, long userId) {
		GrowthRule gr = growthRuleService.getByAction(action);
		return find(gr.getCode(), userId);
	}

	@Override
	public GrowthLog find(int ruleCode, long userId) {
		return growthLogRepo.find("$find", Params.param("ruleCode", ruleCode).put("userId", userId)).get();
	}

	@Transactional
	@Override
	public GrowthLog save(GrowthLog growthLog) {
		// 单日自主练习+错题练习，每做1题，获得1成长值，每天最高可得20成长值。合并成一条记录
		if (GrowthAction.DOING_DAILY_EXERCISE == growthRuleService.getByCode(growthLog.getRuleCode()).getAction()) {
			String todayKey = growthCacheService.getTodayKey(GrowthAction.DOING_DAILY_EXERCISE, growthLog.getUserId());
			long count = growthCacheService.get(todayKey);
			// 有过记录，直接更新之前成长值
			if (count > 0) {
				updateGrowthValue(growthLog);
				return growthLog;
			}
		}
		return growthLogRepo.save(growthLog);
	}

	@Override
	public boolean getCheck(GrowthAction action, long userId) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = df.format(new Date());
		Params params = Params.param();
		try {
			GrowthRule gr = growthRuleService.getByAction(action);
			String beginTime = dateStr + " 00:00:00";
			String endTime = dateStr + " 23:59:59";
			params.put("userId", userId);
			params.put("ruleCode", gr.getCode());
			params.put("beginTime", sdf.parse(beginTime));
			params.put("endTime", sdf.parse(endTime));
		} catch (Exception e) {
			logger.error("GrowthLogServiceImpl getCheck error", e);
		}
		return growthLogRepo.find("$queryCheck", params).count() > 0 ? true : false;
	}

	@Override
	public Page<GrowthLog> queryGrowLog(Pageable p, long userId) {
		Date date = new Date();
		long time = date.getTime();
		// 往前推30天
		long begin = time - (86400 * 1000L * 30);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Params params = Params.param();
		try {
			Date startTime = sdf.parse(sdf.format(new Date(begin)));
			Date endTime = sdf.parse(sdf.format(date));
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			params.put("userId", userId);
		} catch (Exception e) {
			logger.error("GrowthLogServiceImpl queryGrowLog error!", e);
		}
		return growthLogRepo.find("$queryGrowLog", params).fetch(p);
	}

	@Transactional
	@Override
	public void updateGrowthValue(GrowthLog growthLog) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = df.format(new Date());
		Params params = Params.param();
		try {
			String beginTime = dateStr + " 00:00:00";
			String endTime = dateStr + " 23:59:59";
			params.put("userId", growthLog.getUserId());
			params.put("ruleCode", growthLog.getRuleCode());
			params.put("beginTime", sdf.parse(beginTime));
			params.put("endTime", sdf.parse(endTime));
			growthLogRepo.execute("$updateGrowthValue", params);
		} catch (Exception e) {
			logger.error("GrowthLogServiceImpl updateGrowthValue error", e);
		}
	}

	@Override
	public Integer countActionByUser(int code, long userId, Date start, Date end) {
		Params params = Params.param();
		params.put("code", code);
		params.put("userId", userId);
		if (start != null) {
			params.put("start", start);
		}
		if (end != null) {
			params.put("end", end);
		}
		return growthLogRepo.find("$countActionByUser", params).get(Integer.class);
	}

	@Override
	public GrowthLog getLastestCheckIn(long userId, int ruleCode) {
		return growthLogRepo.find("$getLastestCheckIn", Params.param("userId", userId).put("ruleCode", ruleCode)).get();
	}

}
