package com.lanking.uxb.rescon.question.value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Sets;

public class VResLevelKnowpoint implements Serializable {

	private static final long serialVersionUID = -296694289241683471L;

	private Long code;
	private long pcode;
	private String name;
	private int level;
	private VResLevelKnowpoint parent;
	private List<VResLevelKnowpoint> children = Lists.newArrayList();
	@JSONField(serialize = false)
	private Set<Long> allChild = Sets.newHashSet();

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public long getPcode() {
		return pcode;
	}

	public void setPcode(long pcode) {
		this.pcode = pcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Set<Long> getAllChild() {
		return allChild;
	}

	public void setAllChild(Set<Long> allChild) {
		this.allChild = allChild;
	}

	public VResLevelKnowpoint getParent() {
		return parent;
	}

	public void setParent(VResLevelKnowpoint parent) {
		this.parent = parent;
	}

	public List<VResLevelKnowpoint> getChildren() {
		return children;
	}

	public void setChildren(List<VResLevelKnowpoint> children) {
		this.children = children;
	}

}
