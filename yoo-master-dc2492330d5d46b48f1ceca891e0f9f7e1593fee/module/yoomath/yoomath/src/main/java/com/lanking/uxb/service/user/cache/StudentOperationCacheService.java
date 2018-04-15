package com.lanking.uxb.service.user.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 记录学生操作的相关缓存
 * 
 * @since 2.0.3
 * @author peng.zhao
 * @version 2016年4月19日
 */
@Service
public class StudentOperationCacheService extends AbstractCacheService {

	private ValueOperations<String, String> stringOpt;
	// 记录上次布置作业选择的班级ID
	private static final String HOMEWORK_FALLIBLE_PULLED_ID = "hfpid";

	@Override
	public String getNs() {
		return "soc";
	}

	@Override
	public String getNsCn() {
		return "学生端操作的相关缓存";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		stringOpt = getRedisTemplate().opsForValue();
	}

	public List<Long> getHomeworkFalliblePulledIds(long userId) {
		String value = stringOpt.get(assemblyKey(HOMEWORK_FALLIBLE_PULLED_ID, String.valueOf(userId)));
		List<Long> ids = new ArrayList<>();
		if (StringUtils.isBlank(value)) {
			return ids;
		} else {
			String[] values = value.split(",");
			for (int i = 0; i < values.length; i++) {
				ids.add(Long.parseLong(values[i]));
			}

			return ids;
		}
	}

	public void setHomeworkFalliblePulledIds(long userId, List<Long> pulledIds) {
		StringBuilder builder = new StringBuilder();
		for (Long id : pulledIds) {
			builder.append(id);
			builder.append(",");
		}
		stringOpt.set(assemblyKey(HOMEWORK_FALLIBLE_PULLED_ID, String.valueOf(userId)), builder.toString());
	}
}
