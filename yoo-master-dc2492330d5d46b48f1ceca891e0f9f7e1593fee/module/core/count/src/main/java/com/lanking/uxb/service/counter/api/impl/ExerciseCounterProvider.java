package com.lanking.uxb.service.counter.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

@Component
public class ExerciseCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.EXERCISE;
	}

	@Override
	public Biz getOtherBiz() {
		return null;
	}

	/**
	 * 记录习题页中习题数量
	 * 
	 * @param exerciseId
	 *            习题页ID
	 * @param c
	 *            数量
	 */
	public void incrQuestionCount(long exerciseId, int c) {
		this.counter(exerciseId, Count.COUNTER_1, c);
	}

	/**
	 * 获取习题页里面的习题数量
	 * 
	 * @param exerciseId
	 *            习题页ID
	 * @return 习题数量
	 */
	public long getQuestionCount(long exerciseId) {
		return this.getCount(exerciseId, Count.COUNTER_1).longValue();
	}

	/**
	 * 批量获取习题页中的习题数量
	 * 
	 * @param exerciseIds
	 *            习题IDs
	 * @return 数量集合
	 */
	public Map<Long, Long> mgetQuestionCount(Collection<Long> exerciseIds) {
		Map<Long, Long> questionCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(exerciseIds);
		for (Long exerciseId : exerciseIds) {
			Counter counter = counterMap.get(exerciseId);
			if (counter == null) {
				questionCounts.put(exerciseId, 0L);
			} else {
				questionCounts.put(exerciseId, counter.getCount1());
			}
		}
		return questionCounts;
	}

	/**
	 * 获取习题页被收藏数
	 * 
	 * @since 2.1
	 * @param exerciseId
	 *            习题页ID
	 * @return 收藏数
	 */
	public long getCollectionCount(long exerciseId) {
		return this.getCount(exerciseId, Count.COUNTER_2).longValue();
	}

	/**
	 * 更新习题页收藏数
	 * 
	 * @since 2.1
	 * @param exerciseId
	 *            习题页ID
	 * @param num
	 *            数量
	 */
	public void incrCollectionCount(long exerciseId, int num) {
		this.counter(exerciseId, Count.COUNTER_2, num);
	}

	/**
	 * 批量获取收藏数
	 * 
	 * @since 2.1
	 * @param exerciseIds
	 *            习题页ID
	 * @return 计数Map
	 */
	public Map<Long, Long> mgetCollectionCount(Collection<Long> exerciseIds) {
		Map<Long, Long> collectionCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(exerciseIds);
		for (Long exerciseId : exerciseIds) {
			Counter counter = counterMap.get(exerciseId);
			if (counter == null) {
				collectionCounts.put(exerciseId, 0L);
			} else {
				collectionCounts.put(exerciseId, counter.getCount2());
			}
		}
		return collectionCounts;
	}
}
