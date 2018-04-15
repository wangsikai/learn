package com.lanking.uxb.service.honor.cache;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 金币缓存相关
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月9日 下午3:04:33
 */
@Component
public class CoinsCacheService extends AbstractCacheService {

	private ValueOperations<String, String> stringOpt;

	public String getYesterdayKey(CoinsAction action, long userId) {
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Date now = new Date();
		return assemblyKey(action.toString(), fdf.format(DateUtils.addDays(now, -1)), userId);
	}

	public String getTodayKey(CoinsAction action, long userId) {
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Date now = new Date();
		return assemblyKey(action.toString(), fdf.format(now), userId);
	}

	public String getTodayKey(CoinsAction action, long userId, long bizId) {
		FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd");
		Date now = new Date();
		return assemblyKey(action.toString(), fdf.format(now), userId, bizId);
	}

	public String getOneTimeKey(CoinsAction action, long userId) {
		return assemblyKey(action.toString(), userId);
	}

	public String getTimesKey(CoinsAction action, long bizId) {
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
		return "cs";
	}

	@Override
	public String getNsCn() {
		return "金币";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		stringOpt = getRedisTemplate().opsForValue();
	}
}
