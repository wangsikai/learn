package com.lanking.uxb.service.adminSecurity.value;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.bean.Status;

import java.util.Date;
import java.util.List;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class VConsoleMenuEntity {
	private Long id;
	private Long pId;
	private String name;
	private String url;
	private VConsoleSystem system;
	private Date createAt;
	private Status status;
	private List<VConsoleMenuEntity> children = Lists.newArrayList();

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

	public VConsoleSystem getSystem() {
		return system;
	}

	public void setSystem(VConsoleSystem system) {
		this.system = system;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public List<VConsoleMenuEntity> getChildren() {
		return children;
	}

	public void setChildren(List<VConsoleMenuEntity> children) {
		this.children = children;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
