package com.lanking.uxb.operation.questionSection.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@SuppressWarnings("unchecked")
@Component
public class OpQuestionSectionConvertCacheService extends AbstractCacheService {
	private static final String CONVERTING_COUNTER = "cc";

	private ValueOperations<String, String> spOpt;
	private ValueOperations<String, Long> counterOpt;

	public void set(int textbookCode, long questionId) {
		spOpt.set(assemblyKey(textbookCode), String.valueOf(questionId), 365, TimeUnit.DAYS);
	}

	public String get(int textbookCode) {
		return spOpt.get(assemblyKey(textbookCode));
	}

	public void expire(int textbookCode) {
		getRedisTemplate().expire(assemblyKey(textbookCode), 1, TimeUnit.MILLISECONDS);
	}

	public void incrConvertingCount(int count) {
		counterOpt.increment(assemblyKey(CONVERTING_COUNTER), count);
	}

	public int getCount() {
		return counterOpt.increment(assemblyKey(CONVERTING_COUNTER), 0).intValue();
	}

	@Override
	public String getNs() {
		return "op-qsc";
	}

	@Override
	public String getNsCn() {
		return "operation-题目章节关系表处理";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		spOpt = getRedisTemplate().opsForValue();
		counterOpt = getRedisTemplate().opsForValue();
	}
}
