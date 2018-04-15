package com.lanking.uxb.zycon.base.value;

import java.io.Serializable;

/**
 * 习题类型VO
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:13:44
 */
public class CQuestionType implements Serializable {

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
