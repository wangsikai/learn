package com.lanking.uxb.service.adminSecurity.value;

import java.util.Date;

import com.lanking.cloud.sdk.bean.Status;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class VConsoleSystem {
	private Long id;
	private String name;
	private Date createAt;
	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
