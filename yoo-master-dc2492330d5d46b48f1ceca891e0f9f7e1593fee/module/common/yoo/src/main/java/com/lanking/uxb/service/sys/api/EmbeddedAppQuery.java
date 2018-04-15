package com.lanking.uxb.service.sys.api;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.EmbeddedAppLocation;

public class EmbeddedAppQuery {

	private YooApp app;
	private EmbeddedAppLocation location;

	public EmbeddedAppQuery() {
		super();
	}

	public EmbeddedAppQuery(EmbeddedAppLocation location) {
		super();
		this.location = location;
	}

	public EmbeddedAppQuery(YooApp app, EmbeddedAppLocation location) {
		super();
		this.app = app;
		this.location = location;
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

}
