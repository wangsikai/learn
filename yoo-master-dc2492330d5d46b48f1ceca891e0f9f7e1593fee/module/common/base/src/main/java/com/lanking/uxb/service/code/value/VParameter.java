package com.lanking.uxb.service.code.value;

import java.io.Serializable;

import com.lanking.cloud.sdk.util.StringUtils;

public class VParameter implements Serializable {

	private static final long serialVersionUID = 6167133644900281302L;

	private String value;
	private String key = StringUtils.EMPTY;

	public VParameter() {
		super();
	}

	public VParameter(String key) {
		super();
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
