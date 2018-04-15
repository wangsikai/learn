package com.lanking.uxb.service.adminSecurity.form;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class ConsoleRoleForm {
	private Long id;
	private String name;
	private String code;
	private List<Long> menuIds = Lists.newArrayList();

	public Long getId() {
		return id;
	}

	public List<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
