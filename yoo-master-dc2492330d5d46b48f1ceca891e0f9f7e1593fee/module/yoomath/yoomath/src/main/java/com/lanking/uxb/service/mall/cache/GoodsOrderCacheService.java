package com.lanking.uxb.service.mall.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 悠数学-订单相关缓存
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
@Service
public class GoodsOrderCacheService extends AbstractCacheService {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
	private final String ORDER_CODE_COUNTER_KEY = "c";
	// 最新兑换记录缓存key
	private final String LATEST_ORDER_KEY = "lor";

	private ValueOperations<String, Long> longOpt;
	private ValueOperations<String, List<String>> latestOrderOpt;

	@Override
	public String getNs() {
		return "ym-gr";
	}

	@Override
	public String getNsCn() {
		return "悠数学-订单";
	}

	public long getOrderCounter(Date date) {
		return longOpt.increment(assemblyKey(ORDER_CODE_COUNTER_KEY, sdf.format(date)), 1);
	}

	public List<String> getLatestOrder(UserType userType) {
		// return latestOrderOpt.get(assemblyKey(userType.name(),
		// LATEST_ORDER_KEY));
		return null;
	}

	public void addLatestOrder(UserType userType, List<String> values) {
		latestOrderOpt.set(assemblyKey(userType.name(), LATEST_ORDER_KEY), values, 10, TimeUnit.MINUTES);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		longOpt = getRedisTemplate().opsForValue();
		latestOrderOpt = getRedisTemplate().opsForValue();
	}

}
