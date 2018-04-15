package com.lanking.uxb.service.counter.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

/**
 * 书本计数.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月27日
 */
@Component
public class BookCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.BOOK;
	}

	@Override
	public Biz getOtherBiz() {
		return null;
	}

	/**
	 * 书本版本中资源数量计数
	 * 
	 * @param productId
	 *            商品ID
	 * @param delta
	 *            数目
	 */
	public void incrResourceCount(long bookVersionId, int delta) {
		this.counter(bookVersionId, Count.COUNTER_1, delta);
	}

	/**
	 * 获取书本版本中的资源的数量
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 */
	public long getResourceCount(long bookVersionId) {
		return this.getCount(bookVersionId, Count.COUNTER_1).longValue();
	}

	/**
	 * 书本版本中资源数量
	 * 
	 * @param bookVersionIds
	 * @return
	 */
	public Map<Long, Long> mgetResourceCount(Collection<Long> bookVersionIds) {
		Map<Long, Long> collectionCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(bookVersionIds);
		for (Long bookVersionId : bookVersionIds) {
			Counter counter = counterMap.get(bookVersionId);
			if (counter == null) {
				collectionCounts.put(bookVersionId, 0L);
			} else {
				collectionCounts.put(bookVersionId, counter.getCount1());
			}
		}
		return collectionCounts;
	}

	/**
	 * 无分组资源计数
	 * 
	 * @param bookVersionId
	 *            商品ID
	 * @param delta
	 *            数目
	 */
	public void incrNoCatalogResourceCount(long bookVersionId, int delta) {
		this.counter(bookVersionId, Count.COUNTER_2, delta);
	}

	/**
	 * 获取书本版本无分组资源数量
	 * 
	 * @param bookVersionId
	 *            商品ID
	 * @return 数目
	 */
	public long getNoCatalogResourceCount(long bookVersionId) {
		return this.getCount(bookVersionId, Count.COUNTER_2).longValue();
	}
}
