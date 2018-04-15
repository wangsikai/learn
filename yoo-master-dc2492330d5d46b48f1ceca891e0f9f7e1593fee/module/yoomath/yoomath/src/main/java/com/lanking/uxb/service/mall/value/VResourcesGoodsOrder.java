package com.lanking.uxb.service.mall.value;

import java.math.BigDecimal;

import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.PayMode;

/**
 * 资源订单VO
 * 
 * @since 2.0.7
 * @author zemin.song
 * @version 2016年9月1日
 */
public class VResourcesGoodsOrder extends VGoodsOrder {

	private static final long serialVersionUID = 5018618545269001107L;
	// 支付方式
	private PayMode payMod;
	// 支付额
	private BigDecimal totalPrice;
	// 订单状态
	private GoodsOrderStatus orderStatus;
	// 支付流水号
	private String paymentCode;

	public PayMode getPayMod() {
		return payMod;
	}

	public void setPayMod(PayMode payMod) {
		this.payMod = payMod;
	}

	public GoodsOrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(GoodsOrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

}
