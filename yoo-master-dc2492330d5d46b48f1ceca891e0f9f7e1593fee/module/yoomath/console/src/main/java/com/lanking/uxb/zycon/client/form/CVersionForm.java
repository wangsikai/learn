package com.lanking.uxb.zycon.client.form;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.UpgradeType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 客户端版本页面提交数据
 * 
 * @author wangsenhao
 *
 */
public class CVersionForm implements Serializable {

	private static final long serialVersionUID = 3427264201548628435L;

	private Long id;
	private UpgradeType type;
	private YooApp app;
	private String name;
	private List<String> description;
	private String downloadUrl;
	private String version;
	private String size;
	private Status status;
	/**
	 * 需不需要验证版本号是否重名，如果是编辑情况，页面版本号没有修改，则不验证
	 */
	private boolean flag = true;
	/**
	 * 设备类型
	 */
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

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
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

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
}
