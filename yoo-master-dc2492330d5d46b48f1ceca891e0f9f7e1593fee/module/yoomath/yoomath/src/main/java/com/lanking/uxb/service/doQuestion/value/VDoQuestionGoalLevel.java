package com.lanking.uxb.service.doQuestion.value;

import java.io.Serializable;

public class VDoQuestionGoalLevel implements Serializable {

	private static final long serialVersionUID = 801824362736683101L;

	private String code;
	private int value;
	private int goal;
	private String name;
	private String url;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
