package com.lanking.uxb.service.account.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 账户安全相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月29日
 */
@SuppressWarnings("unchecked")
@Service
public class ZyMAccountSecurityCacheService extends AbstractCacheService {

	private ValueOperations<String, String> strOpt;
	private final String EMAIL_CODE_KEY = "ec";
	private final String MOBILE_CODE_KEY = "mc";

	public void setMobileCode(String token, String target, String code, int timeout, TimeUnit timeUnit) {
		strOpt.set(assemblyKey(MOBILE_CODE_KEY, token, target), code, timeout, timeUnit);
	}

	public String getMobileCode(String token, String target) {
		return strOpt.get(assemblyKey(MOBILE_CODE_KEY, token, target));
	}

	public void invalidMobileCode(String token, String target) {
		getRedisTemplate().delete(assemblyKey(MOBILE_CODE_KEY, token, target));
	}

	public void setEmailCode(String token, String target, String code, int timeout, TimeUnit timeUnit) {
		strOpt.set(assemblyKey(EMAIL_CODE_KEY, token, target), code, timeout, timeUnit);
	}

	public String getEmailCode(String token, String target) {
		return strOpt.get(assemblyKey(EMAIL_CODE_KEY, token, target));
	}

	public void invalidEmailCode(String token, String target) {
		getRedisTemplate().delete(assemblyKey(EMAIL_CODE_KEY, token, target));
	}

	@Override
	public String getNs() {
		return "ymas";
	}

	@Override
	public String getNsCn() {
		return "移动端-账号安全相关";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		strOpt = getRedisTemplate().opsForValue();
	}
}
