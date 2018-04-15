package com.lanking.uxb.service.payment.alipay.response;

import javax.xml.bind.annotation.XmlElement;

public class OrderQueryResponse {

	private OrderQueryTrade trade;

	@XmlElement(name = "trade")
	public OrderQueryTrade getTrade() {
		return trade;
	}

	public void setTrade(OrderQueryTrade trade) {
		this.trade = trade;
	}
}
