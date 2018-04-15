package com.lanking.uxb.rescon.question.value;

import java.io.Serializable;

/**
 * 题目状态个数VO
 * 
 * @author wangsenhao
 * @version 2015年8月19日
 */
public class VQuestionCheckCount implements Serializable {

	private static final long serialVersionUID = -543893193137025606L;

	private Long total;
	/**
	 * 未校验数
	 */
	private Long editingNum = 0L;
	/**
	 * 通过数
	 */
	private Long passNum = 0L;
	/**
	 * 未通过数
	 */
	private Long noPassNum = 0L;
	/**
	 * 校验中
	 */
	private Long checkingNum = 0L;
	/**
	 * 一较通过
	 */
	private Long onePassNum = 0L;
	/**
	 * 草稿数
	 */
	private Long draftNum = 0L;

	public Long getDraftNum() {
		return draftNum;
	}

	public void setDraftNum(Long draftNum) {
		this.draftNum = draftNum;
	}

	public Long getOnePassNum() {
		return onePassNum;
	}

	public void setOnePassNum(Long onePassNum) {
		this.onePassNum = onePassNum;
	}

	public Long getCheckingNum() {
		return checkingNum;
	}

	public void setCheckingNum(Long checkingNum) {
		this.checkingNum = checkingNum;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getEditingNum() {
		return editingNum;
	}

	public void setEditingNum(Long editingNum) {
		this.editingNum = editingNum;
	}

	public Long getPassNum() {
		return passNum;
	}

	public void setPassNum(Long passNum) {
		this.passNum = passNum;
	}

	public Long getNoPassNum() {
		return noPassNum;
	}

	public void setNoPassNum(Long noPassNum) {
		this.noPassNum = noPassNum;
	}

}
