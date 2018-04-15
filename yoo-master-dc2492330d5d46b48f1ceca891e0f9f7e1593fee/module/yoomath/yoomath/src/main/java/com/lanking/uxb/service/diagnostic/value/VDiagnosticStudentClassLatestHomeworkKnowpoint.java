package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 学生诊断最新作业知识点VO
 * 
 * @author wangsenhao
 *
 */
public class VDiagnosticStudentClassLatestHomeworkKnowpoint implements Serializable {

	private static final long serialVersionUID = 1240577281519259487L;

	private String knowpointName;

	private long knowpointCode;

	private int doCount;

	private int rightCount;

	private BigDecimal rightRate;

	private Long classId;

	private boolean hasCard = false;

	// 掌握百分比
	private BigDecimal masterRate;

	public String getKnowpointName() {
		return knowpointName;
	}

	public void setKnowpointName(String knowpointName) {
		this.knowpointName = knowpointName;
	}

	public long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(long knowpointCode) {
		this.knowpointCode = knowpointCode;
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

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public boolean isHasCard() {
		return hasCard;
	}

	public void setHasCard(boolean hasCard) {
		this.hasCard = hasCard;
	}

	public BigDecimal getMasterRate() {
		return masterRate;
	}

	public void setMasterRate(BigDecimal masterRate) {
		this.masterRate = masterRate;
	}

}
