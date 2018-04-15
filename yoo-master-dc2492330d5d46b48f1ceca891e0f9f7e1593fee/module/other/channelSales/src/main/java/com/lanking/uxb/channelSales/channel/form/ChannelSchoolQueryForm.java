package com.lanking.uxb.channelSales.channel.form;

import com.lanking.cloud.domain.common.baseData.SchoolType;

/**
 * 渠道学校查询form
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public class ChannelSchoolQueryForm {
	// 学校名
	private String schoolName;
	// 当前页
	private int page = 1;
	// 当前分页大小
	private int size = 10;
	// 渠道code
	private String channelCode;
	// 渠道商名
	private String channelName;
	// 学校类型
	private SchoolType schoolType;

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public SchoolType getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(SchoolType schoolType) {
		this.schoolType = schoolType;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

}
