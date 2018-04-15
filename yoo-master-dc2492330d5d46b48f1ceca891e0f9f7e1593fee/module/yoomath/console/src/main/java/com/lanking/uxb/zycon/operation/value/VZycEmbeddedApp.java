package com.lanking.uxb.zycon.operation.value;

import java.io.Serializable;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.EmbeddedAppLocation;

public class VZycEmbeddedApp implements Serializable {

	private static final long serialVersionUID = 7834497694915349340L;

	private Long id;
	private YooApp app;
	private EmbeddedAppLocation location;
	private String name;
	private Long imageId;
	private String imageUrl;
	private String url;
	private Integer sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
	}

	public EmbeddedAppLocation getLocation() {
		return location;
	}

	public void setLocation(EmbeddedAppLocation location) {
		this.location = location;
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

}
