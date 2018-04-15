package com.lanking.uxb.service.account.cache;

import java.util.concurrent.TimeUnit;

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
public class ZyMFindPasswordCacheService extends AbstractCacheService {

	private ValueOperations<String, String> strOpt;

	// 缓存中存储当前找回密码的账号ID
	private static final String ACCOUNT_KEY = "a";
	// 缓存中存储当前找回密码的验证码
	private static final String AUTHCODE_KEY = "ac";

	// 获取当前会话下找回密码的账号ID
	public long getAccount(String token) {
		String accountId = strOpt.get(assemblyKey(ACCOUNT_KEY, token));
		return accountId == null ? 0 : Long.parseLong(accountId);
	}

	// 获取当前会话下找回密码的账号ID
	public void setAccount(String token, long accountId) {
		strOpt.set(assemblyKey(ACCOUNT_KEY, token), String.valueOf(accountId));
	}

	// 设置当前会话找回密码的验证码
	public void setAuthCode(String token, String target, String code, int timeout, TimeUnit timeUnit) {
		strOpt.set(assemblyKey(AUTHCODE_KEY, token, target), code, timeout, timeUnit);
	}

	// 获取当前会话找回密码的验证码
	public String getAuthCode(String token, String target) {
		return strOpt.get(assemblyKey(AUTHCODE_KEY, token, target));
	}

	// 删除当前会话找回密码的验证码
	public void invalidAuthCode(String token, String target) {
		getRedisTemplate().delete(assemblyKey(AUTHCODE_KEY, token, target));
	}

	@Override
	public String getNs() {
		return "ymfp";
	}

	@Override
	public String getNsCn() {
		return "移动端-找回密码";
	}

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		strOpt = getRedisTemplate().opsForValue();
	}
}
