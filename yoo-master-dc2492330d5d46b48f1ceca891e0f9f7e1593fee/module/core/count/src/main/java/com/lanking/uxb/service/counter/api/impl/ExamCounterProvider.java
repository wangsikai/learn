package com.lanking.uxb.service.counter.api.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

@Component
public class ExamCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.EXAM;
	}

	@Override
	public Biz getOtherBiz() {
		return null;
	}

	/**
	 * 试卷总数计数
	 * 
	 * @param vendorId
	 *            供应商Id
	 * @param c
	 */
	public void incrExams(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_1, c);
	}

	/**
	 * 获取试卷总数
	 * 
	 * @param vendorId
	 * @return
	 */
	public long getExams(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_1).longValue();
	}

	/**
	 * 批量获取试卷总数
	 * 
	 * @param vendorIds
	 *            供应商册ID集合
	 * @return 试卷总数
	 */
	public Map<Long, Long> mgetgetExams(Set<Long> vendorIds) {
		Map<Long, Long> memberCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(vendorIds);
		for (Long vendorId : vendorIds) {
			Counter counter = counterMap.get(vendorId);
			memberCounts.put(vendorId, counter == null ? 0 : counter.getCount1());
		}
		return memberCounts;
	}

	/**
	 * 已发布试卷总数计数
	 * 
	 * @param vendorId
	 *            供应商Id
	 * @param c
	 */
	public void incrPublishedExams(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_2, c);
	}

	/**
	 * 获取已发布试卷总数
	 * 
	 * @param vendorId
	 * @return
	 */
	public long getPublishedExams(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_2).longValue();
	}

	/**
	 * 批量获取已发布试卷总数
	 * 
	 * @param vendorIds
	 *            供应商册ID集合
	 * @return 试卷总数
	 */
	public Map<Long, Long> mgetgetPublishedExams(Set<Long> vendorIds) {
		Map<Long, Long> memberCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(vendorIds);
		for (Long vendorId : vendorIds) {
			Counter counter = counterMap.get(vendorId);
			memberCounts.put(vendorId, counter == null ? 0 : counter.getCount2());
		}
		return memberCounts;
	}

	/**
	 * 待发布试卷总数计数
	 * 
	 * @param vendorId
	 *            供应商Id
	 * @param c
	 */
	public void incrNoCheckExams(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_3, c);
	}

	/**
	 * 获取待发布试卷总数
	 * 
	 * @param vendorId
	 * @return
	 */
	public long getNoCheckExams(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_3).longValue();
	}

	/**
	 * 批量获取待发布试卷总数
	 * 
	 * @param vendorIds
	 *            供应商册ID集合
	 * @return 试卷总数
	 */
	public Map<Long, Long> mgetgetNoCheckExams(Set<Long> vendorIds) {
		Map<Long, Long> memberCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(vendorIds);
		for (Long vendorId : vendorIds) {
			Counter counter = counterMap.get(vendorId);
			memberCounts.put(vendorId, counter == null ? 0 : counter.getCount3());
		}
		return memberCounts;
	}

	/**
	 * 录入中试卷总数计数
	 * 
	 * @param vendorId
	 *            供应商Id
	 * @param c
	 */
	public void incrEditingExams(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_4, c);
	}

	/**
	 * 获取待发布试卷总数
	 * 
	 * @param vendorId
	 * @return
	 */
	public long getEditingExams(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_4).longValue();
	}

	/**
	 * 批量获取待发布试卷总数
	 * 
	 * @param vendorIds
	 *            供应商册ID集合
	 * @return 试卷总数
	 */
	public Map<Long, Long> mgetgetEditingExams(Set<Long> vendorIds) {
		Map<Long, Long> memberCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(vendorIds);
		for (Long vendorId : vendorIds) {
			Counter counter = counterMap.get(vendorId);
			memberCounts.put(vendorId, counter == null ? 0 : counter.getCount4());
		}
		return memberCounts;
	}
}
