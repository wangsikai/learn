package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;
import java.math.BigDecimal;

public class VDiagnosticStudentClassKnowpoint implements Serializable {

	private static final long serialVersionUID = -4347335457804584988L;

	private Long knowpointCode;

	private String knowpointName;

	// 最小难度
	private BigDecimal minDifficulty;

	// 最大难度
	private BigDecimal maxDifficulty;

	// 基础题
	private int doHard1Count;

	private int rightHard1Count;
	// 基础题正确率
	private BigDecimal hard1RightRate;

	// 提高题
	private int doHard2Count;

	private int rightHard2Count;
	// 提高题的正确率
	private BigDecimal hard2RightRate;

	// 冲刺题
	private int doHard3Count;

	private int rightHard3Count;
	// 冲刺题的正确率
	private BigDecimal hard3RightRate;

	// 做题数量
	private int doCount;

	// 做对数量
	private int rightCount;

	// 正确率
	private BigDecimal rightRate;

	// 掌握情况
	private MasterStatus masterStatus;

	// 掌握百分比
	private BigDecimal masterRate;

	private boolean hasData = true;

	public Long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(Long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public String getKnowpointName() {
		return knowpointName;
	}

	public void setKnowpointName(String knowpointName) {
		this.knowpointName = knowpointName;
	}

	public BigDecimal getMinDifficulty() {
		return minDifficulty;
	}

	public void setMinDifficulty(BigDecimal minDifficulty) {
		this.minDifficulty = minDifficulty;
	}

	public BigDecimal getMaxDifficulty() {
		return maxDifficulty;
	}

	public void setMaxDifficulty(BigDecimal maxDifficulty) {
		this.maxDifficulty = maxDifficulty;
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

	public int getDoCount() {
		return doCount;
	}

	public void setDoCount(int doCount) {
		this.doCount = doCount;
	}

	public int getRightCount() {
		return rightCount;
	}

	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getHard1RightRate() {
		return hard1RightRate;
	}

	public void setHard1RightRate(BigDecimal hard1RightRate) {
		this.hard1RightRate = hard1RightRate;
	}

	public BigDecimal getHard2RightRate() {
		return hard2RightRate;
	}

	public void setHard2RightRate(BigDecimal hard2RightRate) {
		this.hard2RightRate = hard2RightRate;
	}

	public BigDecimal getHard3RightRate() {
		return hard3RightRate;
	}

	public void setHard3RightRate(BigDecimal hard3RightRate) {
		this.hard3RightRate = hard3RightRate;
	}

	public MasterStatus getMasterStatus() {
		return masterStatus;
	}

	public void setMasterStatus(MasterStatus masterStatus) {
		this.masterStatus = masterStatus;
	}

	public BigDecimal getMasterRate() {
		return masterRate;
	}

	public void setMasterRate(BigDecimal masterRate) {
		this.masterRate = masterRate;
	}

	public boolean isHasData() {
		return hasData;
	}

	public void setHasData(boolean hasData) {
		this.hasData = hasData;
	}

}
