package com.lanking.uxb.service.code.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 教材类别VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月15日
 */
public class VTextbookCategory implements Serializable {

	private static final long serialVersionUID = -936461320888555141L;

	private int code;
	private int sequence;
	private String name;
	private List<VTextbook> textbooks = Lists.newArrayList();

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

	public List<VTextbook> getTextbooks() {
		return textbooks;
	}

	public void setTextbooks(List<VTextbook> textbooks) {
		this.textbooks = textbooks;
	}

}
