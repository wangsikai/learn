package com.lanking.uxb.service.activity.form;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Grade;

public class HolidayActivity01ExerciseForm {

	private long activityCode;
	private long startTime;
	private long deadline;
	private List<Long> homeworkClassIds;
	private List<Long> exerciseIds;
	private Integer categoryCode;
	private HolidayActivity01Grade grade;

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

	public List<Long> getHomeworkClassIds() {
		return homeworkClassIds;
	}

	public void setHomeworkClassIds(List<Long> homeworkClassIds) {
		this.homeworkClassIds = homeworkClassIds;
	}

	public List<Long> getExerciseIds() {
		return exerciseIds;
	}

	public void setExerciseIds(List<Long> exerciseIds) {
		this.exerciseIds = exerciseIds;
	}

	public long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(long activityCode) {
		this.activityCode = activityCode;
	}

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public HolidayActivity01Grade getGrade() {
		return grade;
	}

	public void setGrade(HolidayActivity01Grade grade) {
		this.grade = grade;
	}
	
}
