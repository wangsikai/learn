package com.lanking.uxb.rescon.question.value;

import java.io.Serializable;

import com.lanking.cloud.sdk.bean.Status;

public class VQuestionCategory implements Serializable {
	private static final long serialVersionUID = -8387039374614571896L;
	private long code;
	private String name;
	private Status status;
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
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

}
