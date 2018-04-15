package com.lanking.uxb.service.examPaper.value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 知识体系树
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public class VKnowledgeTreeNode implements Serializable {
	private static final long serialVersionUID = 8093541088483726833L;

	private Long code;
	private String name;
	private Long pcode;
	private Integer level;
	private Integer sequence;
	private Boolean isSystem = true;
	private Long questionCount = 0L;
	/**
	 * 知识点对应的知识卡片数量
	 * 
	 * @since 2.6.0
	 */
	private Long knowCardCount = 0L;
	private Long fallibleCount = 0L;
	private Long collectCount = 0L;
	private Long schoolQCount = 0L;
	// 下面三个字段只有在组装树形结构的时候才会有效
	private VKnowledgeTreeNode parent;
	private List<VKnowledgeTreeNode> children = Lists.newArrayList();
	@JSONField(serialize = false)
	private Set<Long> allChild = Sets.newHashSet();
	private String allChildren;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPcode() {
		return pcode;
	}

	public void setPcode(Long pcode) {
		this.pcode = pcode;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean getSystem() {
		return isSystem;
	}

	public void setSystem(Boolean system) {
		isSystem = system;
	}

	public Long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Long questionCount) {
		this.questionCount = questionCount;
	}

	public Long getKnowCardCount() {
		return knowCardCount;
	}

	public void setKnowCardCount(Long knowCardCount) {
		this.knowCardCount = knowCardCount;
	}

	public VKnowledgeTreeNode getParent() {
		return parent;
	}

	public void setParent(VKnowledgeTreeNode parent) {
		this.parent = parent;
	}

	public List<VKnowledgeTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<VKnowledgeTreeNode> children) {
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

	public Long getFallibleCount() {
		return fallibleCount;
	}

	public void setFallibleCount(Long fallibleCount) {
		this.fallibleCount = fallibleCount;
	}

	public Long getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(Long collectCount) {
		this.collectCount = collectCount;
	}

	public Long getSchoolQCount() {
		return schoolQCount;
	}

	public void setSchoolQCount(Long schoolQCount) {
		this.schoolQCount = schoolQCount;
	}

}
