package com.lanking.uxb.service.code.value;

import java.io.Serializable;

/**
 * 知识点VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月17日
 */
public class VKnowpoint implements Serializable {

	private static final long serialVersionUID = -6951439054858992537L;

	private int code;
	private int subjectCode;
	private int pcode;
	private String name;
	private int level;

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

	public int getPcode() {
		return pcode;
	}

	public void setPcode(int pcode) {
		this.pcode = pcode;
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
