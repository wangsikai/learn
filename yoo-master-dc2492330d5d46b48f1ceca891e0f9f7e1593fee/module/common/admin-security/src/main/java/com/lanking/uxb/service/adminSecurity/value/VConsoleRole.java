package com.lanking.uxb.service.adminSecurity.value;

import java.util.Date;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class VConsoleRole {

	private Long id;
	private String name;
	private Date createAt;
	private String code;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
