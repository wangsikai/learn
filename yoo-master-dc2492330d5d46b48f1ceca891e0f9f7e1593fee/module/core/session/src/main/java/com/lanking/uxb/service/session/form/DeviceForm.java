package com.lanking.uxb.service.session.form;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.Product;

public class DeviceForm {
	// 设备类型
	private DeviceType type;
	// 手机型号
	private String model;
	// 客户端版本
	private String clientVersion;
	// 网络通道
	private String channel;
	// 操作系统
	private String os;
	private int screenWidth;
	private int screenHeight;
	// 通信运营商
	private String isp;
	// 唯一设备号
	private String imei;
	// SIM卡的电子串号
	private String imsi;
	// 推送token
	private String token;
	private Long userId;
	// 产品
	private Product product = Product.YOOMATH;

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
