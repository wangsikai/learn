package com.lanking.uxb.service.resources.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;

/**
 * 作业form
 * 
 * @since yoomath v2.1.2 新知识点 wanlong.che 2016-11-23
 * @since yoomath v2.3.0 添加自动下发标记 wanlong.che 2016-12-13
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月17日
 */
public class HomeworkForm implements Serializable {

	private static final long serialVersionUID = -1442916749715526530L;

	// 作业名称
	private String name;
	private long homeworkId;
	private long exerciseId;
	private Long courseId;
	private Long homeworkClassId;
	private long startTime;
	private long deadline;
	private long createId;
	private BigDecimal difficulty;
	private Integer textbookCode; // 教材
	private HomeworkCorrectingType correctingType = HomeworkCorrectingType.TEACHER;
	// 包含的知识点
	private List<Long> metaKnowpoints = Lists.newArrayList();

	/**
	 * 包含的新知识点
	 * 
	 * @since yoomath v2.1.2 新旧知识点转换 wanlong.che 2016-11-23
	 */
	private List<Long> knowledgePoints = Lists.newArrayList();

	// 上一个下发的并且未被订正的作业(布置作业的时候记录)
	private Long lastIssued;
	// 创建时间
	private Date createAt;
	// 是否有简答题
	private boolean hasQuestionAnswering = false;

	/**
	 * 废弃字段.
	 * 
	 * @since 小优快批
	 */
	@Deprecated
	private boolean autoIssue = false;

	/**
	 * 通过题目查询到的新知识点
	 * 
	 * @since 教师端 v1.3.0
	 */
	private List<Long> questionknowledgePoints = Lists.newArrayList();;

	/**
	 * 作业限时时间（单位分钟）
	 * 
	 * @since 教师端 v1.3.0
	 */
	private Integer timeLimit;

	private Long groupId;

	/**
	 * 解答题自动批改标记.
	 * 
	 * @since 小优快批
	 */
	private boolean answerQuestionAutoCorrect = false;
	
	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(Long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public HomeworkCorrectingType getCorrectingType() {
		return correctingType;
	}

	public void setCorrectingType(HomeworkCorrectingType correctingType) {
		this.correctingType = correctingType;
	}

	public List<Long> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<Long> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public Long getLastIssued() {
		return lastIssued;
	}

	public void setLastIssued(Long lastIssued) {
		this.lastIssued = lastIssued;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public boolean isHasQuestionAnswering() {
		return hasQuestionAnswering;
	}

	public void setHasQuestionAnswering(boolean hasQuestionAnswering) {
		this.hasQuestionAnswering = hasQuestionAnswering;
	}

	public List<Long> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}
	
	/**
	 * 废弃字段
	 * @since 小优快批
	 * 
	 */
	@Deprecated
	public boolean isAutoIssue() {
		return autoIssue;
	}
	/**
	 * 废弃字段
	 * @since 小优快批
	 * 
	 */
	@Deprecated
	public void setAutoIssue(boolean autoIssue) {
		this.autoIssue = autoIssue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getQuestionknowledgePoints() {
		return questionknowledgePoints;
	}

	public void setQuestionknowledgePoints(List<Long> questionknowledgePoints) {
		this.questionknowledgePoints = questionknowledgePoints;
	}

	public Integer getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public boolean isAnswerQuestionAutoCorrect() {
		return answerQuestionAutoCorrect;
	}

	public void setAnswerQuestionAutoCorrect(boolean answerQuestionAutoCorrect) {
		this.answerQuestionAutoCorrect = answerQuestionAutoCorrect;
	}

}
