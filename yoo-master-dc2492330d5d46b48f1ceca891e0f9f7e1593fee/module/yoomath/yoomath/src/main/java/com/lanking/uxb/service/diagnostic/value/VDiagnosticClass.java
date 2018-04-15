package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class VDiagnosticClass implements Serializable {
	private static final long serialVersionUID = -236969626985919614L;

	private long classId;
	private int doCountMonth;
	private int rightCountMonth;
	private int doHard1Count;
	private int doHard2Count;
	private int doHard3Count;
	private int rightHard1Count;
	private int rightHard2Count;
	private int rightHard3Count;

	// 各难度的正确率
	private BigDecimal doHard1RightRate;
	private String doHard1RightRateTitle;
	private BigDecimal doHard2RightRate;
	private String doHard2RightRateTitle;
	private BigDecimal doHard3RightRate;
	private String doHard3RightRateTitle;

	// 全国的平均掌握情况
	private int allDoCount;

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
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

	public int getDoHard2Count() {
		return doHard2Count;
	}

	public void setDoHard2Count(int doHard2Count) {
		this.doHard2Count = doHard2Count;
	}

	public int getDoHard3Count() {
		return doHard3Count;
	}

	public void setDoHard3Count(int doHard3Count) {
		this.doHard3Count = doHard3Count;
	}

	public int getRightHard1Count() {
		return rightHard1Count;
	}

	public void setRightHard1Count(int rightHard1Count) {
		this.rightHard1Count = rightHard1Count;
	}

	public int getRightHard2Count() {
		return rightHard2Count;
	}

	public void setRightHard2Count(int rightHard2Count) {
		this.rightHard2Count = rightHard2Count;
	}

	public int getRightHard3Count() {
		return rightHard3Count;
	}

	public void setRightHard3Count(int rightHard3Count) {
		this.rightHard3Count = rightHard3Count;
	}

	public BigDecimal getDoHard1RightRate() {
		return doHard1RightRate;
	}

	public void setDoHard1RightRate(BigDecimal doHard1RightRate) {
		this.doHard1RightRate = doHard1RightRate;
	}

	public String getDoHard1RightRateTitle() {
		return doHard1RightRateTitle;
	}

	public void setDoHard1RightRateTitle(String doHard1RightRateTitle) {
		this.doHard1RightRateTitle = doHard1RightRateTitle;
	}

	public BigDecimal getDoHard2RightRate() {
		return doHard2RightRate;
	}

	public void setDoHard2RightRate(BigDecimal doHard2RightRate) {
		this.doHard2RightRate = doHard2RightRate;
	}

	public String getDoHard2RightRateTitle() {
		return doHard2RightRateTitle;
	}

	public void setDoHard2RightRateTitle(String doHard2RightRateTitle) {
		this.doHard2RightRateTitle = doHard2RightRateTitle;
	}

	public BigDecimal getDoHard3RightRate() {
		return doHard3RightRate;
	}

	public void setDoHard3RightRate(BigDecimal doHard3RightRate) {
		this.doHard3RightRate = doHard3RightRate;
	}

	public String getDoHard3RightRateTitle() {
		return doHard3RightRateTitle;
	}

	public void setDoHard3RightRateTitle(String doHard3RightRateTitle) {
		this.doHard3RightRateTitle = doHard3RightRateTitle;
	}

	public int getAllDoCount() {
		return allDoCount;
	}

	public void setAllDoCount(int allDoCount) {
		this.allDoCount = allDoCount;
	}
}
