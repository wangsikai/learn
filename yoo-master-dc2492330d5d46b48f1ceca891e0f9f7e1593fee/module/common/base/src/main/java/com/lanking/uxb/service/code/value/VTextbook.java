package com.lanking.uxb.service.code.value;

import java.io.Serializable;

/**
 * 教材VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月15日
 */
public class VTextbook implements Serializable {

	private static final long serialVersionUID = -4227789449657576596L;

	private int code;
	private int subjectCode;
	private int phaseCode;
	private int categoryCode;
	private String name;
	private String icon;
	private int sequence;
	private String url;

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

	public int getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(int phaseCode) {
		this.phaseCode = phaseCode;
	}

	public int getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(int categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
