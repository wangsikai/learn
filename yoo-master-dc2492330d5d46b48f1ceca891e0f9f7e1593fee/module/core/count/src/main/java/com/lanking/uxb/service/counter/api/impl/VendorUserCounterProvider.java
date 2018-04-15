package com.lanking.uxb.service.counter.api.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

/**
 * 资源库用户统计（无detail）.
 * 
 * @author wlche
 *
 */
@Component
public class VendorUserCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.VENDOR_USER;
	}

	@Override
	public Biz getOtherBiz() {
		return null;
	}

	/**
	 * 一校计数.
	 * 
	 * @param vendorUserId
	 * @param c
	 */
	public void incrOneCheck(long vendorUserId, long c) {
		Counter counter = this.getCounter(vendorUserId);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		if (counter == null || format.format(new Date()).equals(format.format(counter.getUpdateAt()))) {
			this.counter(vendorUserId, Count.COUNTER_1, c);
		} else {
			this.counterReset(vendorUserId, Count.COUNTER_1, 1);
		}
	}

	/**
	 * 二校计数.
	 * 
	 * @param vendorUserId
	 * @param c
	 */
	public void incrTwoCheck(long vendorUserId, long c) {
		Counter counter = this.getCounter(vendorUserId);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		if (counter == null || format.format(new Date()).equals(format.format(counter.getUpdateAt()))) {
			this.counter(vendorUserId, Count.COUNTER_2, c);
		} else {
			this.counterReset(vendorUserId, Count.COUNTER_2, 1);
		}
	}

	/**
	 * 获得当日统计.
	 */
	public Counter getTodayCounter(long bizId) {
		Counter counter = super.getCounter(bizId);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		if (counter != null && format.format(today).equals(format.format(counter.getUpdateAt()))) {
			return counter;
		} else {
			super.counterReset(bizId, Count.COUNTER_1, 0);
			super.counterReset(bizId, Count.COUNTER_2, 0);
			counter = new Counter();
			counter.setBiz(getBiz().getValue());
			counter.setBizId(bizId);
			counter.setCount1(0);
			counter.setCount2(0);
			counter.setCreateAt(today);
			counter.setUpdateAt(today);
		}

		return counter;
	}
}
