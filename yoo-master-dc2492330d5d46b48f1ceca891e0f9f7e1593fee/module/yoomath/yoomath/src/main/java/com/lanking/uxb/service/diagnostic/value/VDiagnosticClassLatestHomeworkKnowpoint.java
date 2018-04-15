package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class VDiagnosticClassLatestHomeworkKnowpoint implements Serializable {
	private static final long serialVersionUID = -8608055431459892350L;

	private long classId;
	private String name;
	private int doCount;
	private BigDecimal rightRate;
	private String rightRateTitle;
	private BigDecimal masterRate;
	private Long code;
	private boolean hasCard = false;

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDoCount() {
		return doCount;
	}

	public void setDoCount(int doCount) {
		this.doCount = doCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
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
