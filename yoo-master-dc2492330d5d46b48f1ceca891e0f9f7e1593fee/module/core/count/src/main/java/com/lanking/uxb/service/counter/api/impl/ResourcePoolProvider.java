package com.lanking.uxb.service.counter.api.impl;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

/**
 * 资源池计数（使用供应商的ID）.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月17日
 */
@Component
public class ResourcePoolProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.RESOURCE_POOL;
	}

	@Override
	public Biz getOtherBiz() {
		return null;
	}

	/**
	 * 习题数量计数
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @param c
	 *            数目
	 */
	public void incrQuestionCount(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_1, c);
	}

	/**
	 * 流媒体数量计数
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @param c
	 *            数目
	 */
	public void incrStreamCount(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_2, c);
	}

	/**
	 * 智单数量计数
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @param c
	 *            数目
	 */
	public void incrBlendCount(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_3, c);
	}

	/**
	 * 应用数量计数
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @param c
	 *            数目
	 */
	public void incrAppCount(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_4, c);
	}

	/**
	 * 通过校验的习题计数
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @param c
	 *            数目
	 */
	public void incrQuestionPassCount(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_5, c);
	}

	/**
	 * 未通过校验的习题计数
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @param c
	 *            数目
	 */
	public void incrQuestionNoPassCount(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_6, c);
	}

	/**
	 * 通过校验的流媒体计数
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @param c
	 *            数目
	 */
	public void incrStreamPassCount(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_7, c);
	}

	/**
	 * 未通过校验的流媒体计数
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @param c
	 *            数目
	 */
	public void incrStreamNoPassCount(long vendorId, int c) {
		this.counter(vendorId, Count.COUNTER_8, c);
	}
}
