package com.lanking.uxb.service.adminSecurity.form;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class ConsoleMenuForm {
	private Long id;
	private String name;
	private String url;
	private Long systemId;
	private Long pId;

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}
}
