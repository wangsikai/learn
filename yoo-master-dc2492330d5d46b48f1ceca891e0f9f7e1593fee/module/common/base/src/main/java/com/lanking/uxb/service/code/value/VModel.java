package com.lanking.uxb.service.code.value;

import java.io.Serializable;

public class VModel implements Serializable {
	private static final long serialVersionUID = -107408343096661292L;

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
