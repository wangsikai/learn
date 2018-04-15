package com.lanking.uxb.channelSales.channel.api;

public class CsHomeworkQuery {

	private Long classId;
	private Boolean isCommon;
	private Integer pageSize = 20;
	private Integer page = 1;

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
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

	public Boolean getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(Boolean isCommon) {
		this.isCommon = isCommon;
	}

}
