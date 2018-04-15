package com.lanking.uxb.service.code.cache;

import java.util.List;

import org.springframework.data.redis.core.ValueOperations;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

public class QuestionKnowledgeCacheService extends AbstractCacheService {
	private ValueOperations<Long, List<Long>> questionKnowledgeOpt;

	@Override
	public String getNs() {
		return "qk";
	}

	@Override
	public String getNsCn() {
		return "题目知识点";
	}

}
