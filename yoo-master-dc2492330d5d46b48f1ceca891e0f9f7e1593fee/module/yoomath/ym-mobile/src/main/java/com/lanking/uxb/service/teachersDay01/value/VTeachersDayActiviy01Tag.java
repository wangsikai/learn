package com.lanking.uxb.service.teachersDay01.value;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.user.Sex;

public class VTeachersDayActiviy01Tag implements Serializable {

	private static final long serialVersionUID = -3802801823722886330L;

	private Long code;

	private String name;

	private Sex sex;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

}
