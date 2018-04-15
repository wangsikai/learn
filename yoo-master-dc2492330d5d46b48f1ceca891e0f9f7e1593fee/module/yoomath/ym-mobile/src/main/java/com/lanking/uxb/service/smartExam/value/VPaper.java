package com.lanking.uxb.service.smartExam.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class VPaper implements Serializable {

	private static final long serialVersionUID = -3731121593668475386L;

	private String difficult;

	private int star;

	private String title;

	private int value;
	/**
	 * 试卷正确率
	 */
	private BigDecimal rightRate;
	/**
	 * 提交时间
	 */
	private Date commitAt;
	/**
	 * 试卷id
	 */
	private long paperId;

	private Integer homeworkTime;
	/**
	 * 平均难度
	 * @since 3.9.0
	 */
	private Double avgDifficulty;

	public String getDifficult() {
		return difficult;
	}

	public void setDifficult(String difficult) {
		this.difficult = difficult;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public Date getCommitAt() {
		return commitAt;
	}

	public void setCommitAt(Date commitAt) {
		this.commitAt = commitAt;
	}

	public long getPaperId() {
		return paperId;
	}

	public void setPaperId(long paperId) {
		this.paperId = paperId;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public Double getAvgDifficulty() {
		return avgDifficulty;
	}

	public void setAvgDifficulty(Double avgDifficulty) {
		this.avgDifficulty = avgDifficulty;
	}
}
