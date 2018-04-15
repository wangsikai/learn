package com.lanking.uxb.service.holiday.form;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 布置作业创建专项form
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public class HolidayHomeworkItemPublishForm {
	private BigDecimal difficulty;
	private List<Long> metaKnows;
	private List<Long> knowedgePoints;
	private long createId;
	private Date deadline;
	private HolidayHomeworkType type;
	private List<Long> questionIds;
	private String name;
	private long hkId;
	private long classId;
	private Date startTime;
	private HomeworkStatus status;

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public List<Long> getMetaKnows() {
		return metaKnows;
	}

	public void setMetaKnows(List<Long> metaKnows) {
		this.metaKnows = metaKnows;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public HolidayHomeworkType getType() {
		return type;
	}

	public void setType(HolidayHomeworkType type) {
		this.type = type;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getHkId() {
		return hkId;
	}

	public void setHkId(long hkId) {
		this.hkId = hkId;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public List<Long> getKnowedgePoints() {
		return knowedgePoints;
	}

	public void setKnowedgePoints(List<Long> knowedgePoints) {
		this.knowedgePoints = knowedgePoints;
	}
}
