package com.lanking.uxb.service.resources.value;

import java.io.Serializable;

/**
 * 习题类型VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public class VQuestionType implements Serializable {

	private static final long serialVersionUID = 3700240808036972265L;

	private int code;
	private int subjectCode;
	private String name;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(int subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
