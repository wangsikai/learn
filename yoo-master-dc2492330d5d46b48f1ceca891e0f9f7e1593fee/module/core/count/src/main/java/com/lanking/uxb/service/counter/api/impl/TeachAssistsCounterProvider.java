package com.lanking.uxb.service.counter.api.impl;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

/**
 * 教辅库计数.
 * 
 * @author zemin.song
 * @version 2016年7月27日
 */
@Component
public class TeachAssistsCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.TEACHASSISTS;
	}

	@Override
	public Biz getOtherBiz() {
		return null;
	}

	/**
	 * 教辅库数量计数
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
	 * 教辅库中教辅总量
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @return
	 */
	public long getTotalCount(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_1).longValue();
	}

	/**
	 * 教辅数量已发布
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
	 * 获取已发布教辅数量
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @return 数目
	 */
	public long getPublishCount(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_2).longValue();
	}

	/**
	 * 待发布教辅计数
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
	 * 获取待发布教辅数量
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @return 数目
	 */
	public long getNoPublishCount(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_3).longValue();
	}

	/**
	 * 录入中教辅计数
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
	 * 获取录入中教辅数量
	 * 
	 * @param vendorId
	 *            资源商ID
	 * @return 数目
	 */
	public long getEditCount(long vendorId) {
		return this.getCount(vendorId, Count.COUNTER_4).longValue();
	}

}
