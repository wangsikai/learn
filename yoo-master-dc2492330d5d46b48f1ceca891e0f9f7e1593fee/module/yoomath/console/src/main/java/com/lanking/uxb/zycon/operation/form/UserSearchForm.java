package com.lanking.uxb.zycon.operation.form;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.base.session.SessionStatus;

/**
 * 在线用户搜索form
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月28日 下午7:24:39
 */
public class UserSearchForm {

	private Long loginAtBegin;
	private Long loginAtEnd;
	private Long activeAtBegin;
	private Long activeAtEnd;
	private String name;
	private DeviceType type;
	private SessionStatus status;

	public Long getLoginAtBegin() {
		return loginAtBegin;
	}

	public void setLoginAtBegin(Long loginAtBegin) {
		this.loginAtBegin = loginAtBegin;
	}

	public Long getLoginAtEnd() {
		return loginAtEnd;
	}

	public void setLoginAtEnd(Long loginAtEnd) {
		this.loginAtEnd = loginAtEnd;
	}

	public Long getActiveAtBegin() {
		return activeAtBegin;
	}

	public void setActiveAtBegin(Long activeAtBegin) {
		this.activeAtBegin = activeAtBegin;
	}

	public Long getActiveAtEnd() {
		return activeAtEnd;
	}

	public void setActiveAtEnd(Long activeAtEnd) {
		this.activeAtEnd = activeAtEnd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	public SessionStatus getStatus() {
		return status;
	}

	public void setStatus(SessionStatus status) {
		this.status = status;
	}

}
