package com.lanking.uxb.rescon.basedata.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.resource.question.QuestionCategoryType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 各种题目标签类型对应的题目id列表
 *
 * @author xinyu.zhou
 * @since 2.1.0
 * @since 教师端 v1.3.0 2017-8-2 废弃
 */
@Service
@Deprecated
public class ResconQuestionTypeCacheService extends AbstractCacheService {
	private ValueOperations<String, List<Long>> ops;

	private static final String CACHE_KEY = "c";

	/**
	 * 获得缓存key -> 题目标签对应id列表
	 *
	 * @param type
	 * @return
	 */
	private String getKey(QuestionCategoryType type) {
		return assemblyKey(CACHE_KEY, type.getValue());
	}

	/**
	 * 添加某个题目至某个标签题目列表中
	 *
	 * @param type
	 *            {@link QuestionCategoryType}
	 * @param questionId
	 *            题目id
	 */
	public void add(QuestionCategoryType type, Long questionId) {
		String key = getKey(type);
		List<Long> questionIds = ops.get(key);
		if (questionIds == null) {
			questionIds = new ArrayList<Long>(1);
		}
		questionIds.add(questionId);
		ops.set(key, questionIds);
	}

	/**
	 * 添加多个题目至题目列表中
	 *
	 * @param type
	 *            {@link QuestionCategoryType}
	 * @param ids
	 *            题目id列表
	 */
	public void add(QuestionCategoryType type, List<Long> ids) {
		String key = getKey(type);
		List<Long> questionIds = ops.get(key);
		if (questionIds == null) {
			questionIds = new ArrayList<Long>(ids.size());
		}

		questionIds.addAll(ids);
		ops.set(key, questionIds);
	}

	/**
	 * 移除某标签类型的题目列表中的数据
	 *
	 * @param type
	 *            {@link QuestionCategoryType}
	 * @param id
	 *            题目id
	 * @return 移除后如果当前列表中还存在返回true 若已经不存在则返回false
	 */
	public boolean remove(QuestionCategoryType type, Long id) {
		String key = getKey(type);
		List<Long> questionIds = ops.get(key);
		if (questionIds != null) {
			questionIds.remove(id);

			ops.set(key, questionIds);

			return questionIds.contains(id);
		}
		return false;
	}

	/**
	 * 批量移除题目
	 *
	 * @param type
	 *            {@link QuestionCategoryType}
	 * @param ids
	 *            题目id列表
	 * @return 题目id对应该还存在标签中
	 */
	public List<Long> remove(QuestionCategoryType type, Collection<Long> ids) {
		Map<Long, Boolean> retMap = new HashMap<Long, Boolean>(ids.size());
		String key = getKey(type);
		List<Long> questionIds = ops.get(key);
		if (questionIds == null) {
			return Collections.EMPTY_LIST;
		}

		List<Long> removedIds = new ArrayList<Long>(ids.size());
		for (Long id : ids) {
			questionIds.remove(id);

			if (!questionIds.contains(id)) {
				removedIds.add(id);
			}
		}

		ops.set(key, questionIds);
		return removedIds;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.ops = getRedisTemplate().opsForValue();
	}

	@Override
	public String getNs() {
		return "rqt";
	}

	@Override
	public String getNsCn() {
		return "题目标签列表缓存";
	}

}
