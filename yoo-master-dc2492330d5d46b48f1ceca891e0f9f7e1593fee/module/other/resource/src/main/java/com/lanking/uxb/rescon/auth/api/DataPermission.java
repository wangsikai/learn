package com.lanking.uxb.rescon.auth.api;

import java.io.Serializable;

/**
 * 平台用户数据权限.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年8月12日
 */
public class DataPermission implements Serializable {
	private static final long serialVersionUID = 8562190917671825579L;

	/**
	 * 录入权限.
	 */
	private Integer entry = 1;

	/**
	 * 一校权限.
	 */
	private Integer checkOne = 0;

	/**
	 * 二校权限.
	 */
	private Integer checkTwo = 0;

	/**
	 * 抽查权限.
	 */
	private Integer spotCheck = 0;

	/**
	 * 删除题目权限.
	 */
	private Integer deleteQuestion = 0;

	/**
	 * 查看资源统计权限.
	 */
	private Integer viewResCount = 0;

	/**
	 * 查看题目统计权限.
	 */
	private Integer viewQuestionCount = 0;

	/**
	 * 查看用户统计权限.
	 */
	private Integer viewUserCount = 0;

	/**
	 * 查看书本统计权限.
	 */
	private Integer viewBookCount = 0;

	/**
	 * 查看试卷统计权限.
	 */
	private Integer viewExamCount = 0;

	/**
	 * 纠错权限
	 */
	private Integer questionError = 0;
	/**
	 * 知识体系权限
	 */
	private Integer knowledgeSystem = 0;

	public Integer getEntry() {
		return entry;
	}

	public void setEntry(Integer entry) {
		this.entry = entry;
	}

	public Integer getCheckOne() {
		return checkOne;
	}

	public void setCheckOne(Integer checkOne) {
		this.checkOne = checkOne;
	}

	public Integer getCheckTwo() {
		return checkTwo;
	}

	public void setCheckTwo(Integer checkTwo) {
		this.checkTwo = checkTwo;
	}

	public Integer getSpotCheck() {
		return spotCheck;
	}

	public void setSpotCheck(Integer spotCheck) {
		this.spotCheck = spotCheck;
	}

	public Integer getDeleteQuestion() {
		return deleteQuestion;
	}

	public void setDeleteQuestion(Integer deleteQuestion) {
		this.deleteQuestion = deleteQuestion;
	}

	public Integer getViewResCount() {
		return viewResCount;
	}

	public void setViewResCount(Integer viewResCount) {
		this.viewResCount = viewResCount;
	}

	public Integer getViewQuestionCount() {
		return viewQuestionCount;
	}

	public void setViewQuestionCount(Integer viewQuestionCount) {
		this.viewQuestionCount = viewQuestionCount;
	}

	public Integer getViewUserCount() {
		return viewUserCount;
	}

	public void setViewUserCount(Integer viewUserCount) {
		this.viewUserCount = viewUserCount;
	}

	public Integer getQuestionError() {
		return questionError;
	}

	public void setQuestionError(Integer questionError) {
		this.questionError = questionError;
	}

	public Integer getKnowledgeSystem() {
		return knowledgeSystem;
	}

	public void setKnowledgeSystem(Integer knowledgeSystem) {
		this.knowledgeSystem = knowledgeSystem;
	}

	public Integer getViewBookCount() {
		return viewBookCount;
	}

	public void setViewBookCount(Integer viewBookCount) {
		this.viewBookCount = viewBookCount;
	}

	public Integer getViewExamCount() {
		return viewExamCount;
	}

	public void setViewExamCount(Integer viewExamCount) {
		this.viewExamCount = viewExamCount;
	}
}
