package com.lanking.uxb.service.user.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.domain.type.Biz;

public class VPracticeHistory implements Serializable {

	private static final long serialVersionUID = -161111579342724426L;

	private String name;

	private Biz biz;

	private long bizId;

	private BigDecimal rightRate;

	private BigDecimal completionRate;

	private Date commitAt;

	private Long sectionCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Biz getBiz() {
		return biz;
	}

	public void setBiz(Biz biz) {
		this.biz = biz;
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
		this.bizId = bizId;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public Date getCommitAt() {
		return commitAt;
	}

	public void setCommitAt(Date commitAt) {
		this.commitAt = commitAt;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

}
