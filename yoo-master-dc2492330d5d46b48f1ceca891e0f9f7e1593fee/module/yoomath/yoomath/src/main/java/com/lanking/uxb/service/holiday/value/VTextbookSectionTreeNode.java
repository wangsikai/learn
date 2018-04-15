package com.lanking.uxb.service.holiday.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.holiday.type.HolidayPublishRangeType;

/**
 * 版本及版本下第一级章节树节点对象
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public class VTextbookSectionTreeNode implements Serializable {

	private static final long serialVersionUID = 1392120273213462289L;

	// 版本或章节的code
	private long id;
	private String name;
	private HolidayPublishRangeType type;
	private List<VTextbookSectionTreeNode> children = Lists.newArrayList();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HolidayPublishRangeType getType() {
		return type;
	}

	public void setType(HolidayPublishRangeType type) {
		this.type = type;
	}

	public List<VTextbookSectionTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<VTextbookSectionTreeNode> children) {
		this.children = children;
	}
}
