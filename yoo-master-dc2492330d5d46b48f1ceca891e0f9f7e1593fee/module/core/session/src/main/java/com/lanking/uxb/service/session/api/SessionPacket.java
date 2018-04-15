package com.lanking.uxb.service.session.api;

import java.io.Serializable;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.base.session.SessionStatus;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.uxb.service.session.api.impl.AttrSession;

/**
 * store 会话信息
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月28日
 *
 */
public class SessionPacket implements Serializable {

	private static final long serialVersionUID = 6081612902619744089L;

	private long id;
	private String token;
	private long accountId;
	private long userId;
	private SessionStatus status;
	private UserType userType;
	/**
	 * 存放其他会话临时数据,现不建议使用,要用跟写这个人讨论一下
	 */
	private AttrSession attrSession;
	/**
	 * 设备类型(每次请求更新)
	 * 
	 * @since 2.1
	 */
	private DeviceType deviceType = DeviceType.WEB;
	/**
	 * 客户端版本(每次请求更新)
	 * 
	 * @since 2.1
	 */
	private String version;
	/**
	 * 登录源<br>
	 * 只有在登录的时候更新
	 * 
	 * @since 2.1
	 */
	private Product loginSource;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public SessionStatus getStatus() {
		return status;
	}

	public void setStatus(SessionStatus status) {
		this.status = status;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public AttrSession getAttrSession() {
		if (attrSession == null) {
			attrSession = new AttrSession();
		}
		return attrSession;
	}

	public void setAttrSession(AttrSession attrSession) {
		this.attrSession = attrSession;
	}

	public void updateAttrSession(AttrSession attrSession) {
		if (attrSession != null) {
			if (this.attrSession == null) {
				this.attrSession = attrSession;
			} else {
				this.attrSession.updateAttr(attrSession.getAttr());
			}
		}
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Product getLoginSource() {
		return loginSource;
	}

	public void setLoginSource(Product loginSource) {
		this.loginSource = loginSource;
	}

}
