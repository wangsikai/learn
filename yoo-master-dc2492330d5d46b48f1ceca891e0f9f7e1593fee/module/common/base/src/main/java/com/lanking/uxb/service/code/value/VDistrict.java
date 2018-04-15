package com.lanking.uxb.service.code.value;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class VDistrict implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3543603053209249836L;

	private long code;

	private String name;

	private int level;

	private long pcode;

	private List<VDistrict> children = Lists.newArrayList();

	public List<VDistrict> getChildren() {
		return children;
	}

	public void setChildren(List<VDistrict> children) {
		this.children = children;
	}

	public long getPcode() {
		return pcode;
	}

	public void setPcode(long pcode) {
		this.pcode = pcode;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}