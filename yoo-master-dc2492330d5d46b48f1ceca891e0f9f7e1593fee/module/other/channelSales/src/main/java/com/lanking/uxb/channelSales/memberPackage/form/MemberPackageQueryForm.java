package com.lanking.uxb.channelSales.memberPackage.form;

/**
 * 查询套餐/套餐组
 * 
 * @author zemin.song
 * @version 2016年9月26日
 */
public class MemberPackageQueryForm {

	private Integer pageSize = 20;
	private Integer page = 1;
	private Integer userType;
	private Integer memberType;
	private boolean orderByMonth = false;

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getMemberType() {
		return memberType;
	}

	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public boolean isOrderByMonth() {
		return orderByMonth;
	}

	public void setOrderByMonth(boolean orderByMonth) {
		this.orderByMonth = orderByMonth;
	}

}
