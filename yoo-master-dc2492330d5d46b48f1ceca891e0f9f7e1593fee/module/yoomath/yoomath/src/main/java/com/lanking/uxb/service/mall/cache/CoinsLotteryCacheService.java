package com.lanking.uxb.service.mall.cache;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 抽奖相关缓存
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
@Service
public class CoinsLotteryCacheService extends AbstractCacheService {
	private static final String USER_LOTTERY_KEY = "l";

	private ZSetOperations<String, String> ops;

	/**
	 * 获得缓存key
	 *
	 * @param userId
	 *            用户id
	 * @return 组装后的key
	 */
	private String getKey(long userId) {
		return assemblyKey(USER_LOTTERY_KEY, userId);
	}

	/**
	 * 获得用户一天内的抽奖情况数据
	 *
	 * @param userId
	 *            用户id
	 * @return 用户抽奖时间及中奖情况(true 代表中, false 代表未中)
	 */
	public List<Boolean> getUserLottery(long userId) {
		String key = getKey(userId);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date date = calendar.getTime();
		long currentDayMillis = date.getTime();

		Set<ZSetOperations.TypedTuple<String>> values;
		values = ops.rangeByScoreWithScores(key, 0, System.currentTimeMillis(), 0, 10000);
		List<Boolean> list = Lists.newArrayList();
		for (ZSetOperations.TypedTuple<String> v : values) {
			if (v.getScore().longValue() < currentDayMillis) {
				// 不是当天的数据则干掉
				ops.remove(key, v.getValue());
			} else {
				// 是当天的数据进行判断是否是成功了
				long orderId = Long.valueOf(v.getValue());
				list.add(orderId > 0L);
			}
		}

		return list;
	}

	/**
	 * 表明已经抽奖过一次
	 *
	 * @param userId
	 *            用户id
	 * @param orderId
	 *            表示是的订单的id
	 */
	public void push(long userId, Long orderId) {
		String key = getKey(userId);
		ops.add(key, String.valueOf(orderId == null ? 0L : orderId), System.currentTimeMillis());
	}

	@Override
	public String getNs() {
		return "ym-cl";
	}

	@Override
	public String getNsCn() {
		return "用户抽奖计数缓存";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		ops = getRedisTemplate().opsForZSet();
	}
}
