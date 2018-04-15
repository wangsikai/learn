package com.lanking.uxb.service.holiday.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;

/**
 * 假期作业布置form
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public class HolidayHomeworkPublishForm {
	private HolidayHomeworkType type;
	private Long homeworkClassId;
	private String name;
	private BigDecimal difficulty;
	private Integer questionCount;
	private Date startTime;
	private Date deadline;
	private Long createId;

	// 此次假期作业包含的知识点
	private List<Long> metaKnowpoints;
	/**
	 * @since 3.0.1
	 */
	private List<Long> knowledgePoints;
	/*
	 * 专项中数据，格式如下: [ {"section": {code: xx, name: xxxxx}, questions: [],
	 * difficulty: xxx, metaKnows: []}, {"section": {code: xx, name: xxxxx},
	 * questions: [], difficulty: xxx, metaKnows: [], knowledgePoints: []} ]
	 */
	private List<Map<String, Object>> sectionQuestions;

	public HolidayHomeworkType getType() {
		return type;
	}

	public void setType(HolidayHomeworkType type) {
		this.type = type;
	}

	public Long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(Long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public List<Long> getMetaKnowpoints() {
		return metaKnowpoints;
	}

	public void setMetaKnowpoints(List<Long> metaKnowpoints) {
		this.metaKnowpoints = metaKnowpoints;
	}

	public List<Map<String, Object>> getSectionQuestions() {
		return sectionQuestions;
	}

	public void setSectionQuestions(List<Map<String, Object>> sectionQuestions) {
		this.sectionQuestions = sectionQuestions;
	}

	public List<Long> getKnowledgePoints() {
		return knowledgePoints;
	}

	public void setKnowledgePoints(List<Long> knowledgePoints) {
		this.knowledgePoints = knowledgePoints;
	}

}
