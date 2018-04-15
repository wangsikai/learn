package com.lanking.uxb.zycon.homework.value;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkCorrectStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryType;

import java.util.Date;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
public class VZycHomework {
	private Long id;
	private String name;
	private VZycHomeworkClazz clazz;
	private Date startTime;
	private Date deadline;
	private HomeworkStatus status;
	private boolean allCommitMement = false;
	private Long commitCount;
	private Long distributeCount;
	private Status delStatus;
	private Status manStatus;
	private HomeworkQueryType type;
	private boolean allCorrectComplete;
	//是否正在批改中，false不是，true是
	private boolean correctIng;

	public boolean isCorrectIng() {
		return correctIng;
	}

	public void setCorrectIng(boolean correctIng) {
		this.correctIng = correctIng;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

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

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

	public boolean isAllCommitMement() {
		return allCommitMement;
	}

	public void setAllCommitMement(boolean allCommitMement) {
		this.allCommitMement = allCommitMement;
	}

	public Long getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(Long commitCount) {
		this.commitCount = commitCount;
	}

	public Long getDistributeCount() {
		return distributeCount;
	}

	public void setDistributeCount(Long distributeCount) {
		this.distributeCount = distributeCount;
	}

	public Status getManStatus() {
		return manStatus;
	}

	public void setManStatus(Status manStatus) {
		this.manStatus = manStatus;
	}

	public HomeworkQueryType getType() {
		return type;
	}

	public void setType(HomeworkQueryType type) {
		this.type = type;
	}

	public boolean isAllCorrectComplete() {
		return allCorrectComplete;
	}

	public void setAllCorrectComplete(boolean allCorrectComplete) {
		this.allCorrectComplete = allCorrectComplete;
	}

}
