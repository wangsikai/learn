package com.lanking.uxb.service.interaction.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.yoomath.interaction.InteractionStatus;
import com.lanking.cloud.domain.yoomath.interaction.InteractionType;

/**
 * 老师师生互动VO
 * 
 * @since 2.0.3
 * @author wangsenhao
 *
 */
public class VInteraction implements Serializable {

	private static final long serialVersionUID = 7571071592643130152L;

	private Long id;
	/**
	 * 学生名称
	 */
	private String studentName;
	/**
	 * 班级名称
	 */
	private String className;
	/**
	 * 创建时间
	 */
	private Date createAt;
	/**
	 * 原因
	 */
	private String reason;

	private InteractionStatus status;
	private InteractionType type;

	private String p1;
	private String p2;

	private Long classId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

}
