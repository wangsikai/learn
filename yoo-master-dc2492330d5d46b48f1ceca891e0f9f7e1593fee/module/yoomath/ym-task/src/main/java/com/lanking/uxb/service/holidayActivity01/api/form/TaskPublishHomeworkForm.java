package com.lanking.uxb.service.holidayActivity01.api.form;

import java.math.BigDecimal;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;

public class TaskPublishHomeworkForm {
	// 已经有的题目ID列表
	private List<Long> qIds;
	// 作业班级ID列表
	private Long homeworkClassIds;
	// 开始时间
	private Long startTime;
	// 结束时间
	private Long deadline;
	// 创建人,在control中覆盖
	private Long createId;
	// 难度
	private BigDecimal difficulty;
	// 作业批改类型
	private HomeworkCorrectingType correctingType = HomeworkCorrectingType.TEACHER;
	private List<Long> knowledgePoints = Lists.newArrayList();
	private String name;
	// 从作业篮子布置作业
	private boolean fromCar = false;
	private Long homeworkId;

	private boolean hasQuestionAnswering = false;

	/**
	 * 自动下发标记（默认不自动下发）.
	 * 
	 * @since yoomath v2.3.0
	 */
	private boolean autoIssue = false;

	public List<Long> getqIds() {
		return qIds;
	}

	public void setqIds(List<Long> qIds) {
		this.qIds = qIds;
	}

	public Long getHomeworkClassIds() {
		return homeworkClassIds;
	}

	public void setHomeworkClassIds(Long homeworkClassIds) {
		this.homeworkClassIds = homeworkClassIds;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getDeadline() {
		return deadline;
	}

	public void setDeadline(Long deadline) {
		this.deadline = deadline;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public HomeworkCorrectingType getCorrectingType() {
		return correctingType;
	}

	public void setCorrectingType(HomeworkCorrectingType correctingType) {
		this.correctingType = correctingType;
	}

	public List<Long> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFromCar() {
		return fromCar;
	}

	public void setFromCar(boolean fromCar) {
		this.fromCar = fromCar;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public boolean getAutoIssue() {
		return autoIssue;
	}

	public void setAutoIssue(boolean autoIssue) {
		this.autoIssue = autoIssue;
	}

	public boolean isHasQuestionAnswering() {
		return hasQuestionAnswering;
	}

	public void setHasQuestionAnswering(boolean hasQuestionAnswering) {
		this.hasQuestionAnswering = hasQuestionAnswering;
	}

}
