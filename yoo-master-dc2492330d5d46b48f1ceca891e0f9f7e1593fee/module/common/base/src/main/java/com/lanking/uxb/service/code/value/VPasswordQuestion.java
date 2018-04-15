package com.lanking.uxb.service.code.value;

import java.io.Serializable;

public class VPasswordQuestion implements Serializable {

	private static final long serialVersionUID = -1237738839193739882L;

	private int code;
	private String name;

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

}
