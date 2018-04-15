package com.lanking.uxb.service.code.value;

import java.io.Serializable;

import com.lanking.cloud.domain.common.baseData.SchoolType;

public class VSchool implements Serializable {

	private static final long serialVersionUID = 3505401038493900074L;

	private long id;
	private int modelCode;
	private String name = "未知学校";
	private SchoolType type;

	private long districtCode;
	/**
	 * 地域全称
	 */
	private String districtName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getModelCode() {
		return modelCode;
	}

	public void setModelCode(int modelCode) {
		this.modelCode = modelCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(long districtCode) {
		this.districtCode = districtCode;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public SchoolType getType() {
		return type;
	}

	public void setType(SchoolType type) {
		this.type = type;
	}

}
