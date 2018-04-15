package com.lanking.uxb.service.mall.cache;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotteryGoodsLevel;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 金币抽奖中奖记录
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Component
public class CoinsLotteryRecordCacheService extends AbstractCacheService {
	private static final String RECORD_KEY = "r";

	private ValueOperations<String, List<String>> valueOps;

	private String getKey(CoinsLotteryGoodsLevel level, Long seasonId) {
		return assemblyKey(RECORD_KEY, level.getValue(), seasonId);
	}

	/**
	 * 设置不同级别的中奖记录
	 *
	 * @param level
	 *            中奖级别
	 * @param records
	 *            中奖励记录
	 */
	public void setLevelRecords(CoinsLotteryGoodsLevel level, List<String> records, Long seasonId) {
		String key = getKey(level, seasonId);
		valueOps.set(key, records);
	}

	/**
	 * 查询不同级别的中奖记录
	 *
	 * @param level
	 *            中奖级别
	 * @return 中奖记录
	 */
	public List<String> getLevelRecords(CoinsLotteryGoodsLevel level, Long seasonId) {
		return valueOps.get(getKey(level, seasonId));
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		valueOps = getRedisTemplate().opsForValue();
	}

	@Override
	public String getNs() {
		return "cld-r";
	}

	@Override
	public String getNsCn() {
		return "抽奖中奖记录缓存";
	}
}
