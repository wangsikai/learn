package com.lanking.uxb.channelSales.memberPackage.form;

import com.lanking.cloud.domain.yoo.member.MemberPackageCardStatus;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;

/**
 * 会员兑换卡查询form
 *
 * @author zemin.song
 * @version 2016年9月26日
 */
public class MemberPackageCardQueryForm {
	// 当前页
	private int page = 1;
	// 分页大小
	private int size = 20;
	// 卡号
	private String code;
	// 批量卡号
	private String codes;
	// 筛选去除卡号
	private String noCodes;
	// 备注
	private String memo;
	// 用户类型
	private UserType userType;
	// 卡类型
	private MemberType memberType;
	// 卡时长
	private Integer month;
	// 创建者
	private Long createId;
	// 卡状态
	private MemberPackageCardStatus status;
	// 开始
	private String startDate;
	// 结束
	private String endDate;
	// 查询类型
	private Integer orderType;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public MemberPackageCardStatus getStatus() {
		return status;
	}

	public void setStatus(MemberPackageCardStatus status) {
		this.status = status;
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

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getCodes() {
		return codes;
	}

	public void setCodes(String codes) {
		this.codes = codes;
	}

	public String getNoCodes() {
		return noCodes;
	}

	public void setNoCodes(String noCodes) {
		this.noCodes = noCodes;
	}

}
