package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class VDiagnosticClassLatestHomework implements Serializable {
	private static final long serialVersionUID = 378621664471228807L;

	private long id;
	private long classId;
	private long homeworkId;
	private String name;
	private Date startTime;
	private BigDecimal rightRate;
	private int classRank;
	private BigDecimal difficulty;
	private String rightRateTitle;
	// 当前作业的提交率，2016.12.13，在首页显示
	private BigDecimal submitRate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public int getClassRank() {
		return classRank;
	}

	public void setClassRank(int classRank) {
		this.classRank = classRank;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public BigDecimal getSubmitRate() {
		return submitRate;
	}

	public void setSubmitRate(BigDecimal submitRate) {
		this.submitRate = submitRate;
	}

}
