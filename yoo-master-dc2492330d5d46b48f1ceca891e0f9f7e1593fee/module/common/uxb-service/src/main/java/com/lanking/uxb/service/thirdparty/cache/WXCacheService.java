package com.lanking.uxb.service.thirdparty.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 微信相关数据缓存.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月29日
 */
@Component
public class WXCacheService extends AbstractCacheService {
	private static final String WX_KEY = "wx";
	private static final String ACCESS_TOKENT_KEY = WX_KEY + "-at";
	private ValueOperations<String, String> tokenOpt;

	@Override
	public String getNs() {
		return WX_KEY;
	}

	@Override
	public String getNsCn() {
		return "微信";
	}

	/**
	 * 获取应用Access Token key.
	 * 
	 * @param appId
	 *            应用ID
	 * @return
	 */
	private String getServiceAccessTokenKey(String appId) {
		return assemblyKey(ACCESS_TOKENT_KEY, appId);
	}

	/**
	 * 设置Access Token.
	 * 
	 * @param appId
	 *            应用ID
	 * @param accessToken
	 *            token值
	 * @param timeout
	 *            超时时间
	 * @param timeunit
	 *            超时单位
	 */
	public void setServiceAccessToken(String appId, String accessToken, long timeout, TimeUnit timeunit) {
		tokenOpt.set(getServiceAccessTokenKey(appId), accessToken, timeout, timeunit);
	}

	/**
	 * 获取Access Token.
	 * 
	 * @param appId
	 *            应用ID
	 * @return
	 */
	public String getServiceAccessToken(String appId) {
		return tokenOpt.get(getServiceAccessTokenKey(appId));
	}

	/**
	 * 设置Access Token失效.
	 * 
	 * @param appId
	 *            应用ID
	 */
	@SuppressWarnings("unchecked")
	public void invalidServiceAccessToken(String appId) {
		getRedisTemplate().delete(getServiceAccessTokenKey(appId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		tokenOpt = getRedisTemplate().opsForValue();
	}
}
