package com.lanking.uxb.service.activity.cache;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;
import com.taobao.api.internal.util.StringUtils;

/**
 * 假期活动用户选择年级缓存
 * 
 * @author wangsenhao
 *
 */
@Component
public class HolidayActivity01GradeCacheService extends AbstractCacheService {

	private static final String HOLIDAYACTIVITY01_GRADE_KEY = "hag";
	private ValueOperations<String, String> opsVal;

	private String getKey(long teacherId, long code) {
		return assemblyKey(HOLIDAYACTIVITY01_GRADE_KEY, teacherId, code);
	}

	public void update(long teacherId, long code, Integer grade) {
		opsVal.set(getKey(teacherId, code), String.valueOf(grade));
	}

	public Integer getGrade(long teacherId, long code) {
		String grade = opsVal.get(getKey(teacherId, code));
		if (StringUtils.isEmpty(grade)) {
			return -1;
		}
		return Integer.parseInt(grade);
	}

	@Override
	public String getNs() {
		return "holiday-grade";
	}

	@Override
	public String getNsCn() {
		return "假期活动用户选择年级缓存";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.opsVal = getRedisTemplate().opsForValue();
	}

	@Override
	public SerializerType getSerializerType() {
		return SerializerType.STRING;
	}

}
