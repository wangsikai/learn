package com.lanking.uxb.service.honor.cache;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.honor.point.PointAction;
import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 积分相关缓存
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年6月1日
 */
@Component
public class PointCacheService extends AbstractCacheService {

	private ValueOperations<String, String> stringOpt;

	public String getYesterdayKey(PointAction action, long userId) {
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Date now = new Date();
		return assemblyKey(action.toString(), fdf.format(DateUtils.addDays(now, -1)), userId);
	}

	public String getTodayKey(PointAction action, long userId) {
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Date now = new Date();
		return assemblyKey(action.toString(), fdf.format(now), userId);
	}

	public String getTodayKey(PointAction action, long userId, long bizId) {
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Date now = new Date();
		return assemblyKey(action.toString(), fdf.format(now), userId, bizId);
	}

	public String getOneTimeKey(PointAction action, long userId) {
		return assemblyKey(action.toString(), userId);
	}

	public String getTimesKey(PointAction action, long bizId) {
		return assemblyKey(action.toString(), bizId);
	}

	public long incr(String key) {
		return stringOpt.increment(key, 1);
	}

	public long incr(String key, int count) {
		return stringOpt.increment(key, count);
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
		return "pt";
	}

	@Override
	public String getNsCn() {
		return "积分";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		stringOpt = getRedisTemplate().opsForValue();
	}
}
