package com.lanking.uxb.service.counter.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

/**
 * 书本库计数.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月27日
 */
@Component
public class BooksCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.BOOKS;
	}

	@Override
	public Biz getOtherBiz() {
		return null;
	}

	/**
	 * 书本库中书本数量计数
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @param delta
	 *            数目
	 */
	public void incrTotalCount(long vendorId, int delta) {
		this.counter(vendorId, Count.COUNTER_1, delta);
	}

	/**
	 * 获取书本库中的书本数量
	 * 
	 * @param vendorId
	 *            资源商ID
	 */
	public long getTotalCount(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_1).longValue();
	}

	/**
	 * 书本库中的书本数量
	 * 
	 * @param vendorIds
	 * @return
	 */
	public Map<Long, Long> mgetTotalCount(Collection<Long> vendorIds) {
		Map<Long, Long> collectionCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(vendorIds);
		for (Long vendorId : vendorIds) {
			Counter counter = counterMap.get(vendorId);
			if (counter == null) {
				collectionCounts.put(vendorId, 0L);
			} else {
				collectionCounts.put(vendorId, counter.getCount1());
			}
		}
		return collectionCounts;
	}

	/**
	 * 已发布书本计数
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @param delta
	 *            数目
	 */
	public void incrPublishCount(long vendorId, int delta) {
		this.counter(vendorId, Count.COUNTER_2, delta);
	}

	/**
	 * 获取已发布书本数量
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @return 数目
	 */
	public long getPublishCount(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_2).longValue();
	}

	/**
	 * 待发布书本计数
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @param delta
	 *            数目
	 */
	public void incrNoPublishCount(long vendorId, int delta) {
		this.counter(vendorId, Count.COUNTER_3, delta);
	}

	/**
	 * 获取待发布书本数量
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @return 数目
	 */
	public long getNoPublishCount(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_3).longValue();
	}

	/**
	 * 录入中书本计数
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @param delta
	 *            数目
	 */
	public void incrEditCount(long vendorId, int delta) {
		this.counter(vendorId, Count.COUNTER_4, delta);
	}

	/**
	 * 获取待发布书本数量
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @return 数目
	 */
	public long getEditCount(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_4).longValue();
	}
}
