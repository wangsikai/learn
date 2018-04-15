package com.lanking.uxb.zycon.mall.form;

import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.domain.yoo.user.UserType;

public class OrderForm {
	private String accountName;
	private GoodsOrderStatus status;
	private String startTime;
	private String endTime;
	private UserType userType;
	private Integer orderType;
	private CoinsGoodsOrderSource source;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public GoodsOrderStatus getStatus() {
		return status;
	}

	public void setStatus(GoodsOrderStatus status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public CoinsGoodsOrderSource getSource() {
		return source;
	}

	public void setSource(CoinsGoodsOrderSource source) {
		this.source = source;
	}

}