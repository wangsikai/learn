package com.lanking.uxb.service.code.value;

import java.io.Serializable;

/**
 * 元知识点VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月17日
 */
public class VMetaKnowpoint implements Serializable {

	private static final long serialVersionUID = -4421511412034588418L;

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
