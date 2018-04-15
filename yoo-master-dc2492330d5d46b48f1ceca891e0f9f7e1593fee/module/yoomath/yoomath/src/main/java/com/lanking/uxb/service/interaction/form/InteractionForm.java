package com.lanking.uxb.service.interaction.form;

import com.lanking.cloud.domain.yoomath.interaction.InteractionStatus;
import com.lanking.cloud.domain.yoomath.interaction.InteractionType;

public class InteractionForm {

	private Long teacherId;

	private Long studentId;

	private Long classId;

	private InteractionStatus status;

	private InteractionType type;

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public InteractionStatus getStatus() {
		return status;
	}

	public void setStatus(InteractionStatus status) {
		this.status = status;
	}

	public InteractionType getType() {
		return type;
	}

	public void setType(InteractionType type) {
		this.type = type;
	}

}
