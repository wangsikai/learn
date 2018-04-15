package com.lanking.cloud.domain.base.session;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.Product;

/**
 * 终端信息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "device")
public class Device implements Serializable {
	private static final long serialVersionUID = -7230718207792518686L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 终端类型
	 */
	@Column(precision = 3)
	private DeviceType type;

	/**
	 * 手机型号
	 */
	@Column(length = 64)
	private String model;

	/**
	 * 客户端版本
	 */
	@Column(name = "client_version", length = 32)
	private String clientVersion;

	/**
	 * 网络通道
	 */
	@Column(length = 64)
	private String channel;

	/**
	 * 操作系统
	 */
	@Column(name = "os", length = 32)
	private String os;

	/**
	 * 分辨率-宽度
	 */
	@Column(name = "screen_width", precision = 5)
	private int screenWidth;

	/**
	 * 分辨率-高度
	 */
	@Column(name = "screen_height", precision = 5)
	private int screenHeight;

	/**
	 * 通信运营商
	 */
	@Column(length = 32)
	private String isp;

	/**
	 * 唯一设备号
	 */
	@Column(length = 200)
	private String imei;

	/**
	 * SIM卡的电子串号
	 */
	@Column(length = 32)
	private String imsi;

	/**
	 * 推送token
	 */
	@Column(length = 200)
	private String token;

	/**
	 * 关联用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 所属产品
	 */
	@Column(name = "product", precision = 3)
	private Product product;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
