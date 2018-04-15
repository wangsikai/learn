package com.lanking.uxb.service.honor.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsRule;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.CoinsRuleService;
import com.lanking.uxb.service.honor.cache.CoinsCacheService;

@Transactional(readOnly = true)
@Service
public class CoinsLogServiceImpl implements CoinsLogService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("CoinsLogRepo")
	private Repo<CoinsLog, Long> coinsLogRepo;

	@Autowired
	private CoinsRuleService coinsRuleService;
	@Autowired
	private CoinsCacheService coinsCacheService;

	@Override
	public Page<CoinsLog> queryCoinsLog(Pageable p, long userId) {
		Date date = new Date();
		long time = date.getTime();
		// 往前推一年
		long begin = time - (86400 * 1000L * 365);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Params params = Params.param();
		try {
			Date startTime = sdf.parse(sdf.format(new Date(begin)));
			Date endTime = sdf.parse(sdf.format(date));
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			params.put("userId", userId);
		} catch (Exception e) {
			logger.error("CoinsLogServiceImpl queryCoinsLog error!", e);
		}
		return coinsLogRepo.find("$queryCoinsLog", params).fetch(p);
	}

	@Override
	public Integer coinsCountByAction(CoinsAction coinsAction, Date date, Long userId) {
		int code = coinsRuleService.getByAction(coinsAction).getCode();
		Params params = Params.param();
		// 获取传入时间当天的 开始 和结束 时间戳
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy-MM-dd");
		Long start = 0L;
		Long end = 0L;
		try {
			start = formater.parse(formater2.format(date) + " 00:00:00").getTime();
			end = formater.parse(formater2.format(date) + " 23:59:59").getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		params.put("ruleCode", code);
		params.put("startTime", new Date(start));
		params.put("endTime", new Date(end));
		params.put("userId", userId);
		return coinsLogRepo.find("$getCoinsCountByAction", params).get(Integer.class);
	}

	@Override
	public CoinsLog find(CoinsAction action, long userId) {
		CoinsRule cr = coinsRuleService.getByAction(action);
		return find(cr.getCode(), userId, null);
	}

	@Override
	public CoinsLog find(int ruleCode, long userId, Long bizId) {
		Params params = Params.param();
		params.put("ruleCode", ruleCode);
		params.put("userId", userId);
		if (bizId != null) {
			params.put("bizId", bizId);
		}
		return coinsLogRepo.find("$find", params).get();
	}

	@Override
	public List<CoinsLog> findLogs(int ruleCode, long userId, Long bizId) {
		Params params = Params.param();
		params.put("ruleCode", ruleCode);
		params.put("userId", userId);
		if (bizId != null) {
			params.put("bizId", bizId);
		}
		return coinsLogRepo.find("$find", params).list();
	}

	@Transactional
	@Override
	public CoinsLog save(CoinsLog coinsLog) {
		// 单日自主练习+错题练习，每做1题，获得1枚金币，每天最高可得20枚。合并成一条记录
		if (coinsLog.getRuleCode() != 0 && CoinsAction.DOING_DAILY_EXERCISE == coinsRuleService.getByCode(coinsLog.getRuleCode()).getAction()) {
			String todayKey = coinsCacheService.getTodayKey(CoinsAction.DOING_DAILY_EXERCISE, coinsLog.getUserId());
			long count = coinsCacheService.get(todayKey);
			// 有过记录，直接更新之前金币数
			if (count > 0) {
				updateCoinsValue(coinsLog);
				return coinsLog;
			}
		}
		return coinsLogRepo.save(coinsLog);
	}

	@Transactional
	@Override
	public void updateCoinsValue(CoinsLog coinsLog) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = df.format(new Date());
		Params params = Params.param();
		try {
			String beginTime = dateStr + " 00:00:00";
			String endTime = dateStr + " 23:59:59";
			params.put("userId", coinsLog.getUserId());
			params.put("ruleCode", coinsLog.getRuleCode());
			params.put("beginTime", sdf.parse(beginTime));
			params.put("endTime", sdf.parse(endTime));
			coinsLogRepo.execute("$updateCoinsValue", params);
		} catch (Exception e) {
			logger.error("CoinsLogServiceImpl updateCoinsValue error", e);
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
		return coinsLogRepo.find("$countActionByUser", params).get(Integer.class);
	}
}
