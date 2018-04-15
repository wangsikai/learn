package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;

public class VDiagnosticStudentClass implements Serializable {

	private static final long serialVersionUID = 1978778793103731404L;

	// 月答题数量
	private int doCountMonth;

	// 答对的数量
	private int rightCountMonth;

	// 基础题
	private int doHard1Count;

	private int rightHard1Count;

	// 提高题
	private int doHard2Count;

	private int rightHard2Count;

	// 冲刺题
	private int doHard3Count;

	private int rightHard3Count;
	// 班级平均正确率,取整数
	private Integer classAvgRightRate;
	// 班级平均练习数
	private Integer classAvgExerCount;

	public int getDoCountMonth() {
		return doCountMonth;
	}

	public void setDoCountMonth(int doCountMonth) {
		this.doCountMonth = doCountMonth;
	}

	public int getRightCountMonth() {
		return rightCountMonth;
	}

	public void setRightCountMonth(int rightCountMonth) {
		this.rightCountMonth = rightCountMonth;
	}

	public int getDoHard1Count() {
		return doHard1Count;
	}

	public void setDoHard1Count(int doHard1Count) {
		this.doHard1Count = doHard1Count;
	}

	public int getRightHard1Count() {
		return rightHard1Count;
	}

	public void setRightHard1Count(int rightHard1Count) {
		this.rightHard1Count = rightHard1Count;
	}

	public int getDoHard2Count() {
		return doHard2Count;
	}

	public void setDoHard2Count(int doHard2Count) {
		this.doHard2Count = doHard2Count;
	}

	public int getRightHard2Count() {
		return rightHard2Count;
	}

	public void setRightHard2Count(int rightHard2Count) {
		this.rightHard2Count = rightHard2Count;
	}

	public int getDoHard3Count() {
		return doHard3Count;
	}

	public void setDoHard3Count(int doHard3Count) {
		this.doHard3Count = doHard3Count;
	}

	public int getRightHard3Count() {
		return rightHard3Count;
	}

	public void setRightHard3Count(int rightHard3Count) {
		this.rightHard3Count = rightHard3Count;
	}

	public Integer getClassAvgRightRate() {
		return classAvgRightRate;
	}

	public void setClassAvgRightRate(Integer classAvgRightRate) {
		this.classAvgRightRate = classAvgRightRate;
	}

	public Integer getClassAvgExerCount() {
		return classAvgExerCount;
	}

	public void setClassAvgExerCount(Integer classAvgExerCount) {
		this.classAvgExerCount = classAvgExerCount;
	}

}
