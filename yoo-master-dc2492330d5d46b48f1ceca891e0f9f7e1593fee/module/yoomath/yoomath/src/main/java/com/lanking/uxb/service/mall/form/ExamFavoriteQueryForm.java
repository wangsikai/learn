package com.lanking.uxb.service.mall.form;

import java.util.List;

/**
 * 试卷查询处理form
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public class ExamFavoriteQueryForm {
	// 分页条件
	private Integer page;
	// 当前页最多条数
	private Integer pageSize;
	// 地区码
	private Long districtCode;
	// 试卷分类
	private List<Long> categoryCodes;
	// 用户
	private Long createId;
	// 年份
	private Integer year;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(Long districtCode) {
		this.districtCode = districtCode;
	}

	public List<Long> getCategoryCodes() {
		return categoryCodes;
	}

	public void setCategoryCodes(List<Long> categoryCodes) {
		this.categoryCodes = categoryCodes;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

}
