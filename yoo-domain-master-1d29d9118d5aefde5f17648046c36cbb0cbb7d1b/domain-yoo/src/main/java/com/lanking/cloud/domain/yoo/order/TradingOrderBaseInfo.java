package com.lanking.cloud.domain.yoo.order;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 交易订单基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public abstract class TradingOrderBaseInfo extends OrderBaseInfo {

	private static final long serialVersionUID = -7653078857022739944L;

	/**
	 * 数量
	 */
	@Column(name = "amount")
	private Integer amount;

	/**
	 * 总价
	 */
	@Column(name = "total_price", scale = 2)
	private BigDecimal totalPrice;

	/**
	 * 支付方式
	 */
	@Column(name = "pay_mod", precision = 3)
	private PayMode payMod;

	/**
	 * 虚拟卡类型
	 */
	@Column(name = "virtual_card_type", precision = 3, columnDefinition = "tinyint default 0")
	private VirtualCardType virtualCardType = VirtualCardType.DEFAULT;

	/**
	 * 虚拟卡号
	 */
	@Column(name = "virtual_card_code", length = 16)
	private String virtualCardCode;

	/**
	 * 支付平台
	 */
	@Column(name = "payment_platform_code")
	private Integer paymentPlatformCode;

	/**
	 * 第三方平台支付方式
	 */
	@Column(name = "third_payment_method", precision = 3, columnDefinition = "tinyint default 0")
	private ThirdPaymentMethod thirdPaymentMethod = ThirdPaymentMethod.DEFAULT;

	/**
	 * 支付平台订单号（对应不同的平台，有可能没有）
	 */
	@Column(name = "payment_platform_order_code", length = 200)
	private String paymentPlatformOrderCode;

	/**
	 * 交易流水号（对应不同的平台，有可能没有）
	 */
	@Column(name = "payment_code", length = 200)
	private String paymentCode;

	/**
	 * 支付时间
	 */
	@Column(name = "pay_time", columnDefinition = "datetime(3)")
	private Date payTime;

	/**
	 * 买家备注
	 */
	@Column(name = "buyer_notes", length = 512)
	private String buyerNotes;

	/**
	 * 卖家备注
	 */
	@Column(name = "seller_notes", length = 512)
	private String sellerNotes;

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public PayMode getPayMod() {
		return payMod;
	}

	public void setPayMod(PayMode payMod) {
		this.payMod = payMod;
	}

	public VirtualCardType getVirtualCardType() {
		return virtualCardType;
	}

	public void setVirtualCardType(VirtualCardType virtualCardType) {
		this.virtualCardType = virtualCardType;
	}

	public String getVirtualCardCode() {
		return virtualCardCode;
	}

	public void setVirtualCardCode(String virtualCardCode) {
		this.virtualCardCode = virtualCardCode;
	}

	public Integer getPaymentPlatformCode() {
		return paymentPlatformCode;
	}

	public void setPaymentPlatformCode(Integer paymentPlatformCode) {
		this.paymentPlatformCode = paymentPlatformCode;
	}

	public ThirdPaymentMethod getThirdPaymentMethod() {
		return thirdPaymentMethod;
	}

	public void setThirdPaymentMethod(ThirdPaymentMethod thirdPaymentMethod) {
		this.thirdPaymentMethod = thirdPaymentMethod;
	}

	public String getPaymentPlatformOrderCode() {
		return paymentPlatformOrderCode;
	}

	public void setPaymentPlatformOrderCode(String paymentPlatformOrderCode) {
		this.paymentPlatformOrderCode = paymentPlatformOrderCode;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getBuyerNotes() {
		return buyerNotes;
	}

	public void setBuyerNotes(String buyerNotes) {
		this.buyerNotes = buyerNotes;
	}

	public String getSellerNotes() {
		return sellerNotes;
	}

	public void setSellerNotes(String sellerNotes) {
		this.sellerNotes = sellerNotes;
	}

}
