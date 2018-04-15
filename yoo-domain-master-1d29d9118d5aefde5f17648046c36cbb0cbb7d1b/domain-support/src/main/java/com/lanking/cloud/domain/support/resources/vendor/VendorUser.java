package com.lanking.cloud.domain.support.resources.vendor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Userable;

/**
 * 供应商管理员用户(目前用来登录中央资源库)
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "vendor_user")
public class VendorUser implements Userable, Serializable {

	private static final long serialVersionUID = 8218094133037030442L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属供应商ID
	 */
	@Column(name = "vendor_id")
	private long vendorId;

	/**
	 * 头像
	 */
	@Column(name = "avatar_id")
	private Long avatarId;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * MD5密码
	 */
	@Column(name = "password1", length = 128, nullable = false)
	private String password1;

	/**
	 * 明文密码
	 */
	@Column(name = "password2", length = 128)
	private String password2;

	/**
	 * 用户类型
	 */
	@Column(name = "type", precision = 3)
	private UserType type;

	/**
	 * 用户状态
	 * 
	 * @see VendorUserStatus
	 */
	@Column(precision = 3)
	private VendorUserStatus status = VendorUserStatus.INIT;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 真实姓名
	 */
	@Column(name = "real_name", length = 40)
	private String realName;

	/**
	 * 中央资源库权限
	 */
	@Column(name = "permissions", length = 500)
	private String permissions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getVendorId() {
		return vendorId;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Long avatarId) {
		this.avatarId = avatarId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public VendorUserStatus getStatus() {
		return status;
	}

	public void setStatus(VendorUserStatus status) {
		this.status = status;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

}
