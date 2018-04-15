package com.lanking.uxb.service.sys.cache;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author xinyu.zhou
 * @since 3.9.7
 */
@Service
public class ZyMClientVersionCacheService extends AbstractCacheService {

	private ValueOperations<String, String> valOps;

	private static final String CACHE_KEY = "v";

	private static final int EXPIRE_DAYS = 100;

	/**
	 * 设置通知时间
	 *
	 * @param deviceId
	 *            设备id
	 * @param app
	 *            {@link YooApp}
	 * @param deviceType
	 *            {@link DeviceType}
	 * @return 上次提醒的时间
	 */
	public Long getNoticeTime(String deviceId, YooApp app, DeviceType deviceType, String version) {
		if (StringUtils.isBlank(deviceId)) {
			return 0L;
		}
		String key = assemblyKey(CACHE_KEY, deviceId, app.getValue(), deviceType.getValue(), version.replace(".", ""));

		String value = valOps.get(key);
		return value == null ? 0L : Long.valueOf(value);
	}

	/**
	 * 设置通知时间
	 *
	 * @param deviceId
	 *            设备id
	 * @param app
	 *            {@link YooApp}
	 * @param deviceType
	 *            {@link DeviceType}
	 */
	public void setNoticeTime(String deviceId, YooApp app, DeviceType deviceType, String version) {
		String key = assemblyKey(CACHE_KEY, deviceId, app.getValue(), deviceType.getValue(), version.replace(".", ""));
		valOps.set(key, String.valueOf(System.currentTimeMillis()));
		getRedisTemplate().expire(key, EXPIRE_DAYS, TimeUnit.DAYS);
	}

	@Override
	public String getNs() {
		return "zym-cv";
	}

	@Override
	public String getNsCn() {
		return "悠数学客户端版本升级缓存";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.valOps = getRedisTemplate().opsForValue();
	}
}
