package com.lanking.uxb.rescon.account.value;

import java.io.Serializable;

import com.lanking.cloud.domain.support.resources.vendor.VendorUserStatus;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.auth.api.DataPermission;

/**
 * 供应商管理员VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年3月3日
 */
public class VVendorUser implements Serializable {
	private static final long serialVersionUID = -5234941851881054246L;

	private long id;
	private long vendorId;
	private String name;
	private UserType type;
	private String typeName;
	private VendorUserStatus status;
	private Long avatarId;
	private String avatarUrl;
	private String midAvatarUrl;
	private String pwd = StringUtils.EMPTY;
	private String realName;
	private DataPermission permission;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getVendorId() {
		return vendorId;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public VendorUserStatus getStatus() {
		return status;
	}

	public void setStatus(VendorUserStatus status) {
		this.status = status;
	}

	public Long getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(Long avatarId) {
		this.avatarId = avatarId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getMidAvatarUrl() {
		return midAvatarUrl;
	}

	public void setMidAvatarUrl(String midAvatarUrl) {
		this.midAvatarUrl = midAvatarUrl;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public DataPermission getPermission() {
		return permission;
	}

	public void setPermission(DataPermission permission) {
		this.permission = permission;
	}
}
