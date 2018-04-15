package com.lanking.uxb.service.diagnostic.form;

public class DiagnosticStudentClassForm {

	private long classId;

	private long studentId;

	private int textbookCode;

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

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public int getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(int textbookCode) {
		this.textbookCode = textbookCode;
	}

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

}
