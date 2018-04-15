package com.lanking.uxb.service.imperial.value;

import java.io.Serializable;

import com.lanking.cloud.domain.type.HomeworkStatus;

/**
 * 班级提交作业情况VO
 * 
 * @author wangsenhao
 *
 */
public class VImperialExaminationClazz implements Serializable {

	private static final long serialVersionUID = 3687827684444029569L;
	/**
	 * 作业id
	 */
	private long homeworkId;
	/**
	 * 班级id
	 */
	private long classId;
	/**
	 * 班级名称
	 */
	private String className;
	/**
	 * 分发数量
	 */
	private Long distributeCount = 0L;
	/**
	 * 提交数量
	 */
	private Long commitCount = 0L;
	/**
	 * 作业状态
	 */
	private HomeworkStatus status;

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Long getDistributeCount() {
		return distributeCount;
	}

	public void setDistributeCount(Long distributeCount) {
		this.distributeCount = distributeCount;
	}

	public Long getCommitCount() {
		return commitCount;
	}

	public void setCommitCount(Long commitCount) {
		this.commitCount = commitCount;
	}

	public HomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(HomeworkStatus status) {
		this.status = status;
	}

}
