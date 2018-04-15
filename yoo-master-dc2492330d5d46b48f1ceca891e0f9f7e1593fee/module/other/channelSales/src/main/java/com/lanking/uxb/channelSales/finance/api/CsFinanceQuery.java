package com.lanking.uxb.channelSales.finance.api;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.VirtualCardType;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSource;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 财务统计--查询条件
 * 
 * @author wangsenhao
 *
 */
public class CsFinanceQuery {
	/**
	 * 页面返回年份
	 */
	private String year;
	/**
	 * 页面返回月份
	 */
	private String month;

	private String startTime;
	private String endTime;
	/**
	 * 页面返回柱状图类型<br>
	 * all,全年统计图<br>
	 * channel,渠道商<br>
	 * card,会员卡<br>
	 * stuAuto,学生自主<br>
	 * teaAuto,老师自主
	 */
	private String typeStr = "all";
	// 以下几个统计需要的条件，不是来自于页面的
	/**
	 * 是否会员卡
	 */
	private VirtualCardType cardType;
	/**
	 * 会员卡订单来源
	 */
	private MemberPackageOrderSource source;
	/**
	 * 订单类型
	 */
	private MemberPackageOrderType orderType;
	/**
	 * 用户类型，用于区分老师会员还是学生会员
	 */
	private UserType userType;
	/**
	 * 是否是渠道商用户
	 */
	private boolean isChannel = false;

	/**
	 * 会员类型
	 */
	private MemberType memberType;

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

	public VirtualCardType getCardType() {
		return cardType;
	}

	public void setCardType(VirtualCardType cardType) {
		this.cardType = cardType;
	}

	public MemberPackageOrderSource getSource() {
		return source;
	}

	public void setSource(MemberPackageOrderSource source) {
		this.source = source;
	}

	public MemberPackageOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(MemberPackageOrderType orderType) {
		this.orderType = orderType;
	}

	public boolean isChannel() {
		return isChannel;
	}

	public void setChannel(boolean isChannel) {
		this.isChannel = isChannel;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

}
