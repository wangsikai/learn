package com.lanking.uxb.channelSales.order.form;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.ThirdPaymentMethod;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 订单查询form
 *
 * @author zemin.song
 * @since 2.5.0
 */
public class OrderQueryForm {
	// 当前页
	private int page = 1;
	// 分页大小
	private int size = 20;
	// 关键字
	private String key;
	// 订单号
	private String orderCode;
	// 支付单号
	private String payCode;
	// 渠道code
	private Integer channelCode;
	// 渠道
	private String channelName;
	// 账户名字
	private String accountName;
	// 开通方式
	private MemberPackageOrderType type;
	// 查询用户类型
	private UserType userType;
	// 会员类型
	private MemberType memberType;
	// 开始时间
	private String startDate;
	// 结束时间
	private String endDate;
	// 支付方式
	private PayMode payMod;
	// 支付平台
	private Integer paymentPlatformCode;
	// 支付方式（微信）
	private ThirdPaymentMethod thirdPaymentMethod;
	// 状态
	private MemberPackageOrderStatus status;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public MemberPackageOrderType getType() {
		return type;
	}

	public void setType(MemberPackageOrderType type) {
		this.type = type;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public PayMode getPayMod() {
		return payMod;
	}

	public void setPayMod(PayMode payMod) {
		this.payMod = payMod;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public MemberPackageOrderStatus getStatus() {
		return status;
	}

	public void setStatus(MemberPackageOrderStatus status) {
		this.status = status;
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

	public Integer getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(Integer channelCode) {
		this.channelCode = channelCode;
	}

}
