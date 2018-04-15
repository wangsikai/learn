package com.lanking.uxb.service.sys.value;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class VEmbeddedApp implements Serializable {

	private static final long serialVersionUID = -3488831111366005001L;

	private Long id;
	private String name;
	@JSONField(serialize = false)
	private Long imageId;
	private String imageUrl;
	private String url;
	private Integer sequence;
	private int msg = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public int getMsg() {
		return msg;
	}

	public void setMsg(int msg) {
		this.msg = msg;
	}

}
