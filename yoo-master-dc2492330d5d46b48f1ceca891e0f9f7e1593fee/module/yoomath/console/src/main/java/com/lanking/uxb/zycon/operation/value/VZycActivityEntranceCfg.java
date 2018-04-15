package com.lanking.uxb.zycon.operation.value;

import java.io.Serializable;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.sdk.bean.Status;

public class VZycActivityEntranceCfg implements Serializable {

	private static final long serialVersionUID = -5067593814159875340L;

	private YooApp app;
	private Status status;
	private String iconUrl;
	private Long icon;
	private String uri;

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Long getIcon() {
		return icon;
	}

	public void setIcon(Long icon) {
		this.icon = icon;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
