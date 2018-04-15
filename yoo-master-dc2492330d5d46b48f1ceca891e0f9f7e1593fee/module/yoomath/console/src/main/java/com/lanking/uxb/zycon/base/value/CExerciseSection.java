package com.lanking.uxb.zycon.base.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 章节VO（添加练习信息）
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:13:29
 */
public class CExerciseSection implements Serializable {

	private static final long serialVersionUID = -296694289241683471L;

	private long code;
	private long pcode;
	private int textbookCode;
	private String name;
	private int level;
	private CExerciseSection parent;
	private List<CExerciseSection> children = Lists.newArrayList();

	private List<CTextbookExercise> textbookExercises = Lists.newArrayList();

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

	public CExerciseSection getParent() {
		return parent;
	}

	public void setParent(CExerciseSection parent) {
		this.parent = parent;
	}

	public List<CExerciseSection> getChildren() {
		return children;
	}

	public void setChildren(List<CExerciseSection> children) {
		this.children = children;
	}

	public List<CTextbookExercise> getTextbookExercises() {
		return textbookExercises;
	}

	public void setTextbookExercises(List<CTextbookExercise> textbookExercises) {
		this.textbookExercises = textbookExercises;
	}

}
