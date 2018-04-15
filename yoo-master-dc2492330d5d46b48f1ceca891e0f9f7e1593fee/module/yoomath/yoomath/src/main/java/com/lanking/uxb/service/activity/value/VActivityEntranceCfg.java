package com.lanking.uxb.service.activity.value;

import java.io.Serializable;

public class VActivityEntranceCfg implements Serializable {

	private static final long serialVersionUID = -5725056865515511973L;

	private String icon;
	private String uri;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
