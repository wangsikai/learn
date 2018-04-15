package com.lanking.uxb.zycon.operation.api;

import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.common.BannerStatus;

/**
 * banner 查询条件
 * 
 * @author wangsenhao
 *
 */
public class ZycBannerQuery {
	private YooApp app;
	private BannerLocation location;
	private BannerStatus status;

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

	public BannerStatus getStatus() {
		return status;
	}

	public void setStatus(BannerStatus status) {
		this.status = status;
	}

}
