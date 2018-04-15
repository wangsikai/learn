package com.lanking.cloud.domain.yoo.version;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.UpgradeType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * yoomath client 版本更新日志
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "yoomath_client_version")
public class YoomathClientVersion implements Serializable {

	private static final long serialVersionUID = 3453571889659786361L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 终端设备类型
	 * 
	 * <pre>
	 * 1.只能为DeviceType.ANDROID、DeviceType.IOS
	 * </pre>
	 */
	@Column(name = "device_type", precision = 3, nullable = false)
	private DeviceType deviceType;

	/**
	 * 所属APP
	 * 
	 * @see YooApp
	 */
	@Column(name = "app", columnDefinition = "tinyint default 0")
	private YooApp app = YooApp.MATH_STUDENT;

	/**
	 * 版本更新类型
	 */
	@Column(name = "type", precision = 3)
	private UpgradeType type;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 版本描述
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000)
	private List<String> description = Lists.newArrayList();

	/**
	 * 下载URL
	 */
	@Column(name = "download_url", length = 500)
	private String downloadUrl;

	/**
	 * 版本（X.X.X）
	 */
	@Column(name = "version")
	private String version;

	/**
	 * 版本号,对应version字段,去掉小数点X.X.X->XXX
	 */
	@Column(name = "version_num")
	private int versionNum;

	/**
	 * 版本大小，10M
	 */
	@Column(name = "size")
	private String size;

	/**
	 * 此版本更新是否发布过,默认为0,只要发布过，则一直为1
	 */
	@Column(name = "upgrade_flag")
	private Boolean upgradeFlag = false;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.DISABLED;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
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

	public int getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Boolean getUpgradeFlag() {
		return upgradeFlag;
	}

	public void setUpgradeFlag(Boolean upgradeFlag) {
		this.upgradeFlag = upgradeFlag;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
