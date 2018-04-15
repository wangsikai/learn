package com.lanking.uxb.rescon.statistics.value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 题目统计VO
 * 
 * @author wangsenhao
 *
 */
public class VQuestionStatis implements Serializable {

	private static final long serialVersionUID = -432715492320424669L;
	/**
	 * 章节目录名称/知识点目录名称
	 */
	private String name;
	/**
	 * 章节code/知识点code
	 */
	private Long code;
	/**
	 * 已录入
	 */
	private Long total;
	/**
	 * 通过数
	 */
	private Long passNum;
	/**
	 * 校验中数
	 */
	private Long checkingNum;
	/**
	 * 未通过数
	 */
	private Long noPassNum;
	/**
	 * 未校验数
	 */
	private Long editingNum;

	private Long onepassNum;

	private int level;

	private Long pcode;

	private String allChildren;

	private Set<Long> allChild = Sets.newHashSet();

	public Long getOnepassNum() {
		return onepassNum;
	}

	public void setOnepassNum(Long onepassNum) {
		this.onepassNum = onepassNum;
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

	private List<VQuestionStatis> children = Lists.newArrayList();

	public Long getPcode() {
		return pcode;
	}

	public void setPcode(Long pcode) {
		this.pcode = pcode;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<VQuestionStatis> getChildren() {
		return children;
	}

	public void setChildren(List<VQuestionStatis> children) {
		this.children = children;
	}

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

	public Long getCheckingNum() {
		return checkingNum;
	}

	public void setCheckingNum(Long checkingNum) {
		this.checkingNum = checkingNum;
	}

	public Long getNoPassNum() {
		return noPassNum;
	}

	public void setNoPassNum(Long noPassNum) {
		this.noPassNum = noPassNum;
	}

	public Long getEditingNum() {
		return editingNum;
	}

	public void setEditingNum(Long editingNum) {
		this.editingNum = editingNum;
	}

}
