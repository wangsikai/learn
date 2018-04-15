package com.lanking.uxb.service.code.value;

import java.io.Serializable;
import java.util.List;

public class VResourceCategory implements Serializable {

	private static final long serialVersionUID = 2422146376837392002L;
	private int code;
	private String name;
	private int pCode;
	private List<VResourceCategory> childrenList;

	public List<VResourceCategory> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<VResourceCategory> childrenList) {
		this.childrenList = childrenList;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getpCode() {
		return pCode;
	}

	public void setpCode(int pCode) {
		this.pCode = pCode;
	}

}
