package com.lanking.uxb.service.honor.cache;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 成长值缓存相关
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月9日 下午3:04:33
 */
@Component
public class GrowthCacheService extends AbstractCacheService {

	private ValueOperations<String, String> stringOpt;
	private static final String CONTINUOUS_CHECKIN = "con-c";

	public String getYesterdayKey(GrowthAction action, long userId) {
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Date now = new Date();
		return assemblyKey(action.toString(), fdf.format(DateUtils.addDays(now, -1)), userId);
	}

	public String getTodayKey(GrowthAction action, long userId) {
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Date now = new Date();
		return assemblyKey(action.toString(), fdf.format(now), userId);
	}

	public String getTodayKey(GrowthAction action, long userId, long bizId) {
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Date now = new Date();
		return assemblyKey(action.toString(), fdf.format(now), userId, bizId);
	}

	public String getContinuousCheckInKey(GrowthAction action, long userId) {
		return assemblyKey(action.toString(), CONTINUOUS_CHECKIN, userId);
	}

	public String getOneTimeKey(GrowthAction action, long userId) {
		return assemblyKey(action.toString(), userId);
	}

	public String getTimesKey(GrowthAction action, long bizId) {
		return assemblyKey(action.toString(), bizId);
	}

	public long incr(String key) {
		return stringOpt.increment(key, 1);
	}

	public long incr(String key, int count) {
		return stringOpt.increment(key, count);
	}

	public void setValue(String key, String count) {
		stringOpt.set(key, count);
	}

	public long get(String key) {
		String value = stringOpt.get(key);
		return value == null ? -1L : Long.parseLong(value);
	}

	@Override
	public SerializerType getSerializerType() {
		return SerializerType.STRING;
	}

	@Override
	public String getNs() {
		return "gw";
	}

	@Override
	public String getNsCn() {
		return "成长值";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		stringOpt = getRedisTemplate().opsForValue();
	}
}
