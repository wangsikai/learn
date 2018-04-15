package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 章节VO（添加练习信息）
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
public class VExerciseSection implements Serializable {

	private static final long serialVersionUID = -296694289241683471L;

	private long code;
	private long pcode;
	private int textbookCode;
	private String name;
	private int level;
	private VExerciseSection parent;
	private List<VExerciseSection> children = Lists.newArrayList();

	private List<VTextbookExercise> textbookExercises = Lists.newArrayList();

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

	public VExerciseSection getParent() {
		return parent;
	}

	public void setParent(VExerciseSection parent) {
		this.parent = parent;
	}

	public List<VExerciseSection> getChildren() {
		return children;
	}

	public void setChildren(List<VExerciseSection> children) {
		this.children = children;
	}

	public List<VTextbookExercise> getTextbookExercises() {
		return textbookExercises;
	}

	public void setTextbookExercises(List<VTextbookExercise> textbookExercises) {
		this.textbookExercises = textbookExercises;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
