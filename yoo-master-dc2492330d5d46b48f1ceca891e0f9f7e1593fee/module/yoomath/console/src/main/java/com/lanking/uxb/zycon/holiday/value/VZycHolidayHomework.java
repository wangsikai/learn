package com.lanking.uxb.zycon.holiday.value;

import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.zycon.homework.value.VZycHomeworkClazz;

/**
 * @author zemin.song
 */
public class VZycHolidayHomework {
	private Long id;
	private String name;
	private VZycHomeworkClazz clazz;
	private Date startTime;
	private Date deadline;
	private Status delStatus;
	private HomeworkStatus homeworkStatus;
	private BigDecimal completionRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VZycHomeworkClazz getClazz() {
		return clazz;
	}

	public void setClazz(VZycHomeworkClazz clazz) {
		this.clazz = clazz;
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

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public HomeworkStatus getHomeworkStatus() {
		return homeworkStatus;
	}

	public void setHomeworkStatus(HomeworkStatus homeworkStatus) {
		this.homeworkStatus = homeworkStatus;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

}
