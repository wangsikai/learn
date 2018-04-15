package com.lanking.uxb.rescon.statistics.value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Sets;

public class VStatisKnowpoint implements Serializable {

	private static final long serialVersionUID = 1030765617432988507L;
	private Long code;
	private long pcode;
	private String name;
	private int level;
	/**
	 * 通过数
	 */
	private Long passNum = 0L;
	private Long noPassNum = 0L;
	private Long total = 0L;
	private Long editingNum = 0L;
	private Long onePassNum = 0L;
	private VStatisKnowpoint parent;
	private List<VStatisKnowpoint> children = Lists.newArrayList();
	@JSONField(serialize = false)
	private Set<Long> allChild = Sets.newHashSet();

	public Long getOnePassNum() {
		return onePassNum;
	}

	public void setOnePassNum(Long onePassNum) {
		this.onePassNum = onePassNum;
	}

	public Long getEditingNum() {
		return editingNum;
	}

	public void setEditingNum(Long editingNum) {
		this.editingNum = editingNum;
	}

	public Long getNoPassNum() {
		return noPassNum;
	}

	public void setNoPassNum(Long noPassNum) {
		this.noPassNum = noPassNum;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getPassNum() {
		return passNum;
	}

	public void setPassNum(Long passNum) {
		this.passNum = passNum;
	}

	public void setParent(VStatisKnowpoint parent) {
		this.parent = parent;
	}

	public void setChildren(List<VStatisKnowpoint> children) {
		this.children = children;
	}

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

	public VStatisKnowpoint getParent() {
		return parent;
	}

	public List<VStatisKnowpoint> getChildren() {
		return children;
	}

}
