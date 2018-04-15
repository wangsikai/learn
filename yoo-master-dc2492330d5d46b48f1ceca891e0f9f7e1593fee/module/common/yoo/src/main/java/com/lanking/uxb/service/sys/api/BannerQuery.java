package com.lanking.uxb.service.sys.api;

import java.util.List;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.BannerLocation;

public class BannerQuery {

	private YooApp app;
	private BannerLocation location;
	private List<BannerLocation> locations;

	public List<BannerLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<BannerLocation> locations) {
		this.locations = locations;
	}

	public BannerQuery() {
		super();
	}

	public BannerQuery(BannerLocation location) {
		super();
		this.location = location;
	}

	public BannerQuery(YooApp app, BannerLocation location) {
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

	public BannerLocation getLocation() {
		return location;
	}

	public void setLocation(BannerLocation location) {
		this.location = location;
	}

}
