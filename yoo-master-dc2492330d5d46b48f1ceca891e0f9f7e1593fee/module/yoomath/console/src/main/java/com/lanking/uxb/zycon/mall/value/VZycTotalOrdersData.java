package com.lanking.uxb.zycon.mall.value;

import java.math.BigDecimal;

/**
 * 商品订单统计
 * 
 * @author zemin.song
 *
 */
public class VZycTotalOrdersData {
	// 订单数目
	private long count;
	// 订单总金币
	private BigDecimal totalPrice;
	// 订单总金额
	private BigDecimal totalPriceRMB;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalPriceRMB() {
		return totalPriceRMB;
	}

	public void setTotalPriceRMB(BigDecimal totalPriceRMB) {
		this.totalPriceRMB = totalPriceRMB;
	}

}
