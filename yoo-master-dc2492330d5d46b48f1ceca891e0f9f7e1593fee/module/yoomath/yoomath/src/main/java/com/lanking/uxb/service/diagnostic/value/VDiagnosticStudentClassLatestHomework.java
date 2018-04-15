package com.lanking.uxb.service.diagnostic.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生诊断最新作业 VO
 * 
 * @author wangsenhao
 *
 */
public class VDiagnosticStudentClassLatestHomework implements Serializable {

	private static final long serialVersionUID = -239995567937366796L;
	// 作业名称
	private String name;

	private Date startTime;

	private BigDecimal difficulty;

	private BigDecimal rightRate;

	private BigDecimal classRightRate;

	private int rank;

	private Long classId;

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

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getClassRightRate() {
		return classRightRate;
	}

	public void setClassRightRate(BigDecimal classRightRate) {
		this.classRightRate = classRightRate;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

}
