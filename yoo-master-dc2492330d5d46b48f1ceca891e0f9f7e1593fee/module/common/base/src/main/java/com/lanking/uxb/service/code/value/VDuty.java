package com.lanking.uxb.service.code.value;

import java.io.Serializable;

public class VDuty implements Serializable {

	private static final long serialVersionUID = -4067151847497202547L;

	private int code;
	private int sequence;
	private String name;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
