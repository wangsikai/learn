package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 书本目录VO
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年11月4日 下午1:24:40
 */
public class VBookCatalog implements Serializable {

	private static final long serialVersionUID = 1156672099722176907L;
	private long code;
	private long pcode;
	private int textbookCode;
	private String name;
	private int level;

	// 下面三个字段用于组装树形结构
	private VBookCatalog parent;
	private List<VBookCatalog> children = Lists.newArrayList();
	@JSONField(serialize = false)
	private Set<Long> allChild = Sets.newHashSet();
	private String allChildren;

	// 有没有选中(通用)
	private boolean selected = false;

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public long getPcode() {
		return pcode;
	}

	public void setPcode(long pcode) {
		this.pcode = pcode;
	}

	public int getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(int textbookCode) {
		this.textbookCode = textbookCode;
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

	public VBookCatalog getParent() {
		return parent;
	}

	public void setParent(VBookCatalog parent) {
		this.parent = parent;
	}

	public List<VBookCatalog> getChildren() {
		return children;
	}

	public void setChildren(List<VBookCatalog> children) {
		this.children = children;
	}

	public Set<Long> getAllChild() {
		return allChild;
	}

	public void setAllChild(Set<Long> allChild) {
		this.allChild = allChild;
	}

	public String getAllChildren() {
		if (allChildren == null) {
			StringBuffer sb = new StringBuffer();
			for (Long id : allChild) {
				sb.append(id + ",");
			}
			allChildren = sb.toString();
		}
		return allChildren;
	}

	public void setAllChildren(String allChildren) {
		this.allChildren = allChildren;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
