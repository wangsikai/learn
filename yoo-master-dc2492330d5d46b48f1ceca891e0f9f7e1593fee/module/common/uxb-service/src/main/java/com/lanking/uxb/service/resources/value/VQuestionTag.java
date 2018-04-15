package com.lanking.uxb.service.resources.value;

import java.io.Serializable;

/**
 * 习题标签VO
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年7月24日
 */
public class VQuestionTag implements Serializable {

	private static final long serialVersionUID = -3271289861779404343L;

	private String name;
	private String shortName;
	private String url;
	private Integer sequence;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
