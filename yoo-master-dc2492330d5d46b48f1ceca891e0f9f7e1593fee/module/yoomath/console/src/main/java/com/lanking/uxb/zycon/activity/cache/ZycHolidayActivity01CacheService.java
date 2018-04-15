package com.lanking.uxb.zycon.activity.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 2017暑期作业活动缓存.
 * 
 * @author wlche
 *
 */
@Component
public class ZycHolidayActivity01CacheService extends AbstractCacheService {

	private ValueOperations<String, List<Long>> opt;

	private final String KNOWPOINT_QUESTION = "kq";

	@Override
	public String getNs() {
		return "aty18";
	}

	@Override
	public String getNsCn() {
		return "2018寒假作业活动";
	}

	/**
	 * 保存知识点题目.
	 * 
	 * @param knowpointCode
	 *            知识点
	 * @param questionIds
	 *            知识点题目集合
	 */
	public void setKnowpointQuestions(long knowpointCode, List<Long> questionIds) {
		opt.set(assemblyKey(KNOWPOINT_QUESTION, knowpointCode), questionIds, 7, TimeUnit.DAYS);
	}

	/**
	 * 获取知识点题目.
	 * 
	 * @param knowpointCode
	 *            知识点
	 * @return
	 */
	public List<Long> getQuestions(long knowpointCode) {
		return opt.get(assemblyKey(KNOWPOINT_QUESTION, knowpointCode));
	}

	/**
	 * 批量获取知识点题目.
	 * 
	 * @param knowpointCodes
	 *            知识点集合
	 * @return
	 */
	public List<List<Long>> mGetQuestions(List<Long> knowpointCodes) {
		List<String> keys = new ArrayList<String>();
		for (Long knowpointCode : knowpointCodes) {
			keys.add(assemblyKey(KNOWPOINT_QUESTION, knowpointCode));
		}
		return opt.multiGet(keys);
	}

	@SuppressWarnings("unchecked")
	void invalid(List<Long> knowpointCodes) {
		List<String> keys = new ArrayList<String>();
		for (Long knowpointCode : knowpointCodes) {
			keys.add(assemblyKey(KNOWPOINT_QUESTION, knowpointCode));
		}
		getRedisTemplate().delete(keys);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		opt = getRedisTemplate().opsForValue();
	}
}
