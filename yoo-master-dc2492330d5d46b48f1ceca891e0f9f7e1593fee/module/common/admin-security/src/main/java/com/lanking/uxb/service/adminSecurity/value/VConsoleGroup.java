package com.lanking.uxb.service.adminSecurity.value;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class VConsoleGroup {
	private Long id;
	private String name;
	private VConsoleSystem system;
	private List<VConsoleUser> users = Lists.newArrayList();

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

	public VConsoleSystem getSystem() {
		return system;
	}

	public void setSystem(VConsoleSystem system) {
		this.system = system;
	}

	public List<VConsoleUser> getUsers() {
		return users;
	}

	public void setUsers(List<VConsoleUser> users) {
		this.users = users;
	}
}
