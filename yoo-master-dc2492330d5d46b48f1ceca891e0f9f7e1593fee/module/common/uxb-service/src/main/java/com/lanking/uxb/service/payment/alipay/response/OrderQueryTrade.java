package com.lanking.uxb.service.payment.alipay.response;

import javax.xml.bind.annotation.XmlElement;

public class OrderQueryTrade {

	/**
	 * 买家支付宝帐号.
	 */
	private String buyer_email;

	/**
	 * 交易状态.
	 * <p>
	 * WAIT_BUYER_PAY：等待买家付款<br>
	 * WAIT_SELLER_SEND_GOODS：买家已付款，等待卖家发货<br>
	 * WAIT_BUYER_CONFIRM_GOODS：卖家已发货，等待买家确认<br>
	 * TRADE_FINISHED：交易成功结束<br>
	 * TRADE_CLOSED：交易中途关闭（已结束，未成功完成）<br>
	 * TRADE_REFUSE：立即支付交易拒绝<br>
	 * TRADE_REFUSE_DEALING：立即支付交易拒绝中<br>
	 * TRADE_CANCEL：立即支付交易取消<br>
	 * TRADE_PENDING：等待卖家收款<br>
	 * TRADE_SUCCESS：支付成功<br>
	 * </p>
	 */
	private String trade_status;

	/**
	 * 本地商户订单号.
	 */
	private String out_trade_no;

	/**
	 * 支付宝流水号.
	 */
	private String trade_no;

	/**
	 * 交易总金额.
	 */
	private String total_fee;

	/**
	 * 付款时间（格式为 yyyy-MM-dd HH:mm:ss）.
	 */
	private String gmt_payment;

	@XmlElement(name = "buyer_email")
	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	@XmlElement(name = "trade_status")
	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	@XmlElement(name = "out_trade_no")
	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	@XmlElement(name = "total_fee")
	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	@XmlElement(name = "gmt_payment")
	public String getGmt_payment() {
		return gmt_payment;
	}

	public void setGmt_payment(String gmt_payment) {
		this.gmt_payment = gmt_payment;
	}

	@XmlElement(name = "trade_no")
	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
}
