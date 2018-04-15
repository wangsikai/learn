package com.lanking.uxb.zycon.client.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.UpgradeType;
import com.lanking.cloud.sdk.bean.Status;

public class VZycVersion implements Serializable {

	private static final long serialVersionUID = -4119386260563465100L;

	private Long id;

	private UpgradeType type;

	private String name;

	private List<String> description = Lists.newArrayList();

	private String downloadUrl;

	private String version;

	private String size;

	private Status status;

	private Date updateTime;

	private Boolean upgradeFlag;

	private YooApp app;

	private String appTitle;

	private DeviceType deviceType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UpgradeType getType() {
		return type;
	}

	public void setType(UpgradeType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean getUpgradeFlag() {
		return upgradeFlag;
	}

	public void setUpgradeFlag(Boolean upgradeFlag) {
		this.upgradeFlag = upgradeFlag;
	}

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
}
