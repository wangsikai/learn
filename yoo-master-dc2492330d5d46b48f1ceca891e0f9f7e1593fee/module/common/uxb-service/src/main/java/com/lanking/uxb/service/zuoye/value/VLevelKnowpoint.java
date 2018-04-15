package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Sets;

/**
 * 带层次的知识点VO
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年9月7日 下午6:03:32
 */
public class VLevelKnowpoint implements Serializable {

	private static final long serialVersionUID = -296694289241683471L;

	private Long code;
	private long pcode;
	private String name;
	private int level;
	private Long fallibleCount = 0L;
	/**
	 * 收藏数量
	 */
	private Long collectCount = 0L;
	private Long schoolQCount = 0L;
	private VLevelKnowpoint parent;
	private List<VLevelKnowpoint> children = Lists.newArrayList();
	@JSONField(serialize = false)
	private Set<Long> allChild = Sets.newHashSet();

	public Long getSchoolQCount() {
		return schoolQCount;
	}

	public void setSchoolQCount(Long schoolQCount) {
		this.schoolQCount = schoolQCount;
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

	public VLevelKnowpoint getParent() {
		return parent;
	}

	public void setParent(VLevelKnowpoint parent) {
		this.parent = parent;
	}

	public List<VLevelKnowpoint> getChildren() {
		return children;
	}

	public void setChildren(List<VLevelKnowpoint> children) {
		this.children = children;
	}

	public Long getFallibleCount() {
		return fallibleCount;
	}

	public void setFallibleCount(Long fallibleCount) {
		this.fallibleCount = fallibleCount;
	}

	public Set<Long> getAllChild() {
		return allChild;
	}

	public void setAllChild(Set<Long> allChild) {
		this.allChild = allChild;
	}

	public Long getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(Long collectCount) {
		this.collectCount = collectCount;
	}

}
