package com.lanking.uxb.service.code.value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 章节VO
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
public class VSection implements Serializable {

	private static final long serialVersionUID = -3465435474390647697L;

	private long code;
	private long pcode;
	private int textbookCode;
	private String name;
	private int level;

	// 下面三个字段只有在组装树形结构的时候才会有效
	private VSection parent;
	private List<VSection> children = Lists.newArrayList();
	@JSONField(serialize = false)
	private Set<Long> allChild = Sets.newHashSet();
	private String allChildren;
	// 以下为扩展字段，不在本身convert里面实现
	private Long lessonCount = 0L;// 课时数量
	private Long fallibleCount = 0L;// 错题数量
	/**
	 * 收藏数量,yoomath V1.3
	 */
	private Long collectCount = 0L;
	/**
	 * 学校题目数量,yoomath V1.4.2
	 */
	private Long schoolQCount = 0L;
	/**
	 * 题目数量, yoomath V2.0.7
	 */
	private Long questionCount = 0L;

	public Long getSchoolQCount() {
		return schoolQCount;
	}

	public void setSchoolQCount(Long schoolQCount) {
		this.schoolQCount = schoolQCount;
	}

	public Long getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(Long collectCount) {
		this.collectCount = collectCount;
	}

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

	public VSection getParent() {
		return parent;
	}

	public void setParent(VSection parent) {
		this.parent = parent;
	}

	public List<VSection> getChildren() {
		return children;
	}

	public void setChildren(List<VSection> children) {
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

	public Long getLessonCount() {
		return lessonCount;
	}

	public void setLessonCount(Long lessonCount) {
		this.lessonCount = lessonCount;
	}

	public Long getFallibleCount() {
		return fallibleCount;
	}

	public void setFallibleCount(Long fallibleCount) {
		this.fallibleCount = fallibleCount;
	}

	public Long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Long questionCount) {
		this.questionCount = questionCount;
	}

}
