package com.lanking.uxb.service.web.form;

import java.util.List;

/**
 * 针对性训练作业布置表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version yoomath v2.3.0 2016年12月8日
 */
public class PertinenceHomeworkForm {

	/**
	 * 薄弱知识点集合.
	 */
	private List<Long> knowledgeCodes;

	/**
	 * 选择题数量.
	 */
	private int choiceNum = 0;

	/**
	 * 填空题数量.
	 */
	private int fillBlankNum = 0;

	/**
	 * 解答题数量.
	 */
	private int answerNum = 0;

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
	 * 作业名称.
	 */
	private String name;

	/**
	 * 班级ID集合.
	 */
	private List<Long> classIds;

	/**
	 * 自定义开始时间（为空表示立即）.
	 */
	private Long startTime;

	/**
	 * 自定义结束天数.
	 */
	private Integer endDay;

	/**
	 * 自定义结束时间.
	 */
	private Long deadline;

	/**
	 * 自动下发标记.
	 */
	private Boolean autoIssue;

	public List<Long> getKnowledgeCodes() {
		return knowledgeCodes;
	}

	public void setKnowledgeCodes(List<Long> knowledgeCodes) {
		this.knowledgeCodes = knowledgeCodes;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getClassIds() {
		return classIds;
	}

	public void setClassIds(List<Long> classIds) {
		this.classIds = classIds;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Integer getEndDay() {
		return endDay;
	}

	public void setEndDay(Integer endDay) {
		this.endDay = endDay;
	}

	public Long getDeadline() {
		return deadline;
	}

	public void setDeadline(Long deadline) {
		this.deadline = deadline;
	}

	public Boolean getAutoIssue() {
		return autoIssue;
	}

	public void setAutoIssue(Boolean autoIssue) {
		this.autoIssue = autoIssue;
	}

}
