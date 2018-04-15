package com.lanking.uxb.service.zuoye.cache;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@SuppressWarnings("unchecked")
@Service
public class CalculateRightRateCacheService extends AbstractCacheService {

	private ValueOperations<String, Long> longOpt;

	public long incrCalculate(long stuHkId) {
		long count = longOpt.increment(assemblyKey(stuHkId), 1);
		return count;
	}

	@Override
	public String getNs() {
		return "cr";
	}

	@Override
	public String getNsCn() {
		return "批改异步计算";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		longOpt = getRedisTemplate().opsForValue();
	}

}
