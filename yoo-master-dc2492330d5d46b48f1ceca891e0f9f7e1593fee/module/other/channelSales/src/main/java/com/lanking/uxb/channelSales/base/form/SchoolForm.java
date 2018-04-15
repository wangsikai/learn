package com.lanking.uxb.channelSales.base.form;

import java.io.Serializable;

/**
 * 学校管理查询条件/保存项
 * 
 * @author wangsenhao
 *
 */
public class SchoolForm implements Serializable {

	private static final long serialVersionUID = -7287009891459456423L;

	private Integer phaseCode;

	private Long provinceCode;

	private Long cityCode;

	private Long areaCode;

	private String schoolName;

	private Long id;

	private Integer pageSize = 20;
	private Integer page = 1;

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Long getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(Long provinceCode) {
		this.provinceCode = provinceCode;
	}

	public Long getCityCode() {
		return cityCode;
	}

	public void setCityCode(Long cityCode) {
		this.cityCode = cityCode;
	}

	public Long getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(Long areaCode) {
		this.areaCode = areaCode;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

}
