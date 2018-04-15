package com.lanking.uxb.service.zuoye.value;

import com.lanking.uxb.service.user.value.VUser;

import java.math.BigDecimal;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class VHomeworkStudentClazzStat {
	private BigDecimal rightRate;
	private VUser student;
	// 学生的备注
	private String mark = "";

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public VUser getStudent() {
		return student;
	}

	public void setStudent(VUser student) {
		this.student = student;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}
