package com.lanking.cloud.component.searcher.api;

import java.io.Serializable;

public class Document implements Serializable {

	private static final long serialVersionUID = 3169844654579154383L;

	public Document(String type, String id, String value, Float score) {
		this.type = type;
		this.id = id;
		this.value = value;
		this.score = score;
	}

	public Document(String type, String id, String value) {
		this.type = type;
		this.id = id;
		this.value = value;
	}

	private String type;

	private Float score;

	private String id;

	private String value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}
}
