package com.lanking.uxb.service.mall.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 商品验证码缓存类
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
@SuppressWarnings("unchecked")
@Service
public class GoodsValidCodeCacheService extends AbstractCacheService {
	private ValueOperations<String, String> strOpt;
	private final String EMAIL_CODE_KEY = "ec";
	private final String MOBILE_CODE_KEY = "mc";

	/**
	 * 设置手机验证码
	 *
	 * @param token
	 *            会话信息
	 * @param code
	 *            验证码
	 * @param timeout
	 *            验证码有效时间
	 * @param timeUnit
	 *            时间单位
	 */
	public void setMobileCode(String token, String target, String code, int timeout, TimeUnit timeUnit) {
		strOpt.set(assemblyKey(MOBILE_CODE_KEY, token, target), code, timeout, timeUnit);
	}

	/**
	 * 获得手机验证码
	 *
	 * @param token
	 *            会话信息
	 * @return 验证码
	 */
	public String getMobileCode(String token, String target) {
		return strOpt.get(assemblyKey(MOBILE_CODE_KEY, token, target));
	}

	/**
	 * 删除验证码
	 *
	 * @param token
	 *            会话信息
	 */
	public void invalidMobileCode(String token, String target) {
		getRedisTemplate().delete(assemblyKey(MOBILE_CODE_KEY, token, target));
	}

	/**
	 * 设置邮箱验证码
	 *
	 * @param token
	 *            会话信息
	 * @param code
	 *            验证码
	 * @param timeout
	 *            过期时间
	 * @param timeUnit
	 *            时间单位
	 */
	public void setEmailCode(String token, String target, String code, int timeout, TimeUnit timeUnit) {
		strOpt.set(assemblyKey(EMAIL_CODE_KEY, token, target), code, timeout, timeUnit);
	}

	/**
	 * 得到邮箱验证码
	 *
	 * @param token
	 *            会话信息
	 * @return 验证码
	 */
	public String getEmailCode(String token, String target) {
		return strOpt.get(assemblyKey(EMAIL_CODE_KEY, token, target));
	}

	/**
	 * 删除邮箱验证码
	 *
	 * @param token
	 *            会话信息
	 */
	public void invalidEmailCode(String token, String target) {
		getRedisTemplate().delete(assemblyKey(EMAIL_CODE_KEY, token, target));
	}

	@Override
	public String getNs() {
		return "ym-gm";
	}

	@Override
	public String getNsCn() {
		return "悠数学-验证码";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		strOpt = getRedisTemplate().opsForValue();
	}
}
