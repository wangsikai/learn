package com.lanking.uxb.service.zuoye.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 格言缓存
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月19日
 */
@Service
public final class MottoCacheService extends AbstractCacheService {

	private ValueOperations<String, List<String>> valOpt;

	private String getAllKey() {
		return "list";
	}

	public List<String> get() {
		return valOpt.get(getAllKey());
	}

	public void set(List<String> all) {
		valOpt.set(getAllKey(), all, 1, TimeUnit.DAYS);
	}

	@Override
	public String getNs() {
		return "mo";
	}

	@Override
	public String getNsCn() {
		return "格言";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		valOpt = getRedisTemplate().opsForValue();
	}

}
