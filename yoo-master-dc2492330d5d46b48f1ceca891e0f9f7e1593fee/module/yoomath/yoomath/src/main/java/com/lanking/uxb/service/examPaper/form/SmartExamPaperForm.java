package com.lanking.uxb.service.examPaper.form;

import java.util.List;

/**
 * 智能组卷拉取题目提交参数
 * 
 * @since yoomath 2.0.6
 * @author wangsenhao
 *
 */
public class SmartExamPaperForm {
	/**
	 * 班级ID
	 */
	private Long classId;
	/**
	 * 教材
	 */
	private Integer textbookCode;
	/**
	 * 考察方式<br>
	 * 1.薄弱知识点专练<br>
	 * 2.平衡性训练<br>
	 * 3.自订知识点考察
	 */
	private int testMode;
	/**
	 * 选择题数量
	 */
	private int choiceNum;
	/**
	 * 填空题数量
	 */
	private int fillBlankNum;
	/**
	 * 解答题数量
	 */
	private int answerNum;
	/**
	 * 基础题百分比
	 */
	private Integer basePercent;
	/**
	 * 提高题百分比
	 */
	private Integer raisePercent;
	/**
	 * 冲刺题百分比
	 */
	private Integer sprintPercent;
	/**
	 * 用户选择知识点集合
	 */
	private List<Long> knowledgeCodes;
	// 以下几个只是传递中使用的参数，不从前台传
	private Integer questionType;
	/**
	 * 临时查询的数量
	 */
	private Integer size;
	/**
	 * base.基础题,raise.提高题,sprint.冲刺题
	 */
	private String questionDiffType;
	/**
	 * 不包含的题目
	 */
	private List<Long> notQuestionIds;

	private Long sectionCode;

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public int getTestMode() {
		return testMode;
	}

	public void setTestMode(int testMode) {
		this.testMode = testMode;
	}

	public int getChoiceNum() {
		return choiceNum;
	}

	public void setChoiceNum(int choiceNum) {
		this.choiceNum = choiceNum;
	}

	public int getFillBlankNum() {
		return fillBlankNum;
	}

	public void setFillBlankNum(int fillBlankNum) {
		this.fillBlankNum = fillBlankNum;
	}

	public int getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(int answerNum) {
		this.answerNum = answerNum;
	}

	public Integer getBasePercent() {
		return basePercent;
	}

	public void setBasePercent(Integer basePercent) {
		this.basePercent = basePercent;
	}

	public Integer getRaisePercent() {
		return raisePercent;
	}

	public void setRaisePercent(Integer raisePercent) {
		this.raisePercent = raisePercent;
	}

	public Integer getSprintPercent() {
		return sprintPercent;
	}

	public void setSprintPercent(Integer sprintPercent) {
		this.sprintPercent = sprintPercent;
	}

	public List<Long> getKnowledgeCodes() {
		return knowledgeCodes;
	}

	public void setKnowledgeCodes(List<Long> knowledgeCodes) {
		this.knowledgeCodes = knowledgeCodes;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getQuestionDiffType() {
		return questionDiffType;
	}

	public void setQuestionDiffType(String questionDiffType) {
		this.questionDiffType = questionDiffType;
	}

	public List<Long> getNotQuestionIds() {
		return notQuestionIds;
	}

	public void setNotQuestionIds(List<Long> notQuestionIds) {
		this.notQuestionIds = notQuestionIds;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

}
