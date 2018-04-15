package com.lanking.uxb.service.counter.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

/**
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version V1.0.0,2015年3月7日 上午9:33:39
 *
 */
@Component
public class ProductCatalogCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.PRODUCT_CATALOG;
	}

	@Override
	public Biz getOtherBiz() {
		return Biz.NULL;
	}

	/**
	 * 记录目录下的资源数量
	 * 
	 * @param id
	 *            目录ID
	 * @param c
	 *            数目
	 */
	public void incrResourceCount(long id, int c) {
		this.counter(id, Count.COUNTER_1, c);
	}

	/**
	 * 通过校验计数
	 * 
	 * @param productId
	 * @param num
	 */
	public void incrPassChecked(long catalogId, int num) {
		this.counter(catalogId, Count.COUNTER_2, num);
	}

	/**
	 * 未通过校验计数
	 * 
	 * @param productId
	 * @param num
	 */
	public void incrNoPassChecked(long catalogId, int num) {
		this.counter(catalogId, Count.COUNTER_3, num);
	}

	/**
	 * 获取目录下的资源数量
	 * 
	 * @param id
	 *            目录ID
	 * @return 资源数量
	 */
	public long getResourceCount(long id) {
		Counter counter = this.getCounter(id);
		return counter == null ? 0 : counter.getCount1();
	}

	/**
	 * 批量获取目录下的资源数量
	 * 
	 * @param ids
	 *            目录ID集合
	 * @return 资源数量
	 */
	public Map<Long, Long> mgetResourceCount(Collection<Long> ids) {
		Map<Long, Long> map = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(ids);
		for (Long id : ids) {
			Counter counter = counterMap.get(id);
			map.put(id, counter == null ? 0 : counter.getCount1());
		}
		return map;
	}

	/**
	 * 重置目录下的资源数量
	 * 
	 * @param id
	 *            目录ID
	 * @param resetCount
	 *            重置数量
	 */
	public void resetResourceCount(long id, int resetCount) {
		counterReset(id, Count.COUNTER_1, resetCount);
	}

}
