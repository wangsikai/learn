package com.lanking.uxb.zycon.operation.form;

import java.util.List;

public class VersionForm {

	private Long id;

	private Long publishDate;

	private String version;

	private List<String> description;

	public Long getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Long publishDate) {
		this.publishDate = publishDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
