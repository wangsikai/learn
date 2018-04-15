package com.lanking.uxb.service.activity.form;

public class HolidayActivityOrderSaveForm {

	private Long orderId;

	/**
	 * 商品类型
	 */
	private String goodsType;

	/**
	 * 收货人
	 */
	private String contactName;

	/**
	 * 收货人电话
	 */
	private String contactPhone;

	/**
	 * 详细地址
	 */
	private String contactAddress;

	/**
	 * 特殊参数0
	 * 
	 * <pre>
	 * 	 1.当兑换的为手机话费的话此字段存被充值的手机号
	 * 	 2.当兑换的qq特权的话此字段存被充值的qq号
	 * 	 3.当为现金红包的时候,此字段存储账户名
	 * 	 4.当为手机流量的时候,此字段存储手机号
	 * 	 5.当为卡券的时候,此字段存储手机号
	 * </pre>
	 */
	private String p0;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getP0() {
		return p0;
	}

	public void setP0(String p0) {
		this.p0 = p0;
	}

}
