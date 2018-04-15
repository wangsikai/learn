package com.lanking.uxb.service.account.cache;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 移动端找回密码相关缓存
 * 
 * @since 2.0.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月2日
 */
@SuppressWarnings("unchecked")
@Service
public class ZyMAccountCacheService extends AbstractCacheService {

	private ValueOperations<String, Long> longOpt;

	// 缓存中存储当前密码错误的次数
	private static final String PASSWORD_WRONG_TIME_KEY = "pw";

	public long incrPasswordWrongTime(String token) {
		String key = assemblyKey(PASSWORD_WRONG_TIME_KEY, token);
		Long time = longOpt.get(key);
		long wrongTime = 1L;
		if (time != null) {
			wrongTime = time + 1;
		}
		longOpt.set(key, wrongTime);
		return wrongTime;

	}

	public Long getPasswordWrongTime(String token) {
		Long time = longOpt.get(assemblyKey(PASSWORD_WRONG_TIME_KEY, token));
		if (time == null) {
			return 0L;
		}
		return time;
	}

	public void invalidPasswordWrongTime(String token) {
		getRedisTemplate().delete(assemblyKey(PASSWORD_WRONG_TIME_KEY, token));
	}

	public String getNs() {
		return "yma";
	}

	@Override
	public String getNsCn() {
		return "移动端-账号相关";
	}

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		longOpt = getRedisTemplate().opsForValue();
	}
}
