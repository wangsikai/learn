package com.lanking.uxb.service.adminSecurity.cache;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@SuppressWarnings("unchecked")
@Service
public class ConsoleUserCacheService extends AbstractCacheService {

	private ValueOperations<String, Long> longOpt;

	/**
	 * 登录错误次数key
	 */
	private static final String LOGIN_WRONG_TIME_KEY = "w";

	@Override
	public String getNs() {
		return "con-u";
	}

	@Override
	public String getNsCn() {
		return "管控台-账户";
	}

	private String getLoginWrongTimeKey(String token) {
		return assemblyKey(LOGIN_WRONG_TIME_KEY, token);
	}

	public void incrLoginWrongTime(String token) {
		String key = getLoginWrongTimeKey(token);
		Long time = longOpt.get(key);
		if (time == null) {
			longOpt.set(key, 1L);
		} else {
			longOpt.set(key, time + 1L);
		}

	}

	public Long getLoginWrongTime(String token) {
		Long time = longOpt.get(getLoginWrongTimeKey(token));
		if (time == null) {
			return 0L;
		}
		return time;
	}

	public void invalidLoginWrongTime(String token) {
		getRedisTemplate().delete(getLoginWrongTimeKey(token));
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		longOpt = getRedisTemplate().opsForValue();
	}
}
