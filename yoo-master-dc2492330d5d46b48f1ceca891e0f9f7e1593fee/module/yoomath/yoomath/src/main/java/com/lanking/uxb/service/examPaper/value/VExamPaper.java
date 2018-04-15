package com.lanking.uxb.service.examPaper.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 中央资源库试卷VO
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public class VExamPaper implements Serializable {
	private static final long serialVersionUID = -3738207967434601465L;

	private long id;
	private String name;
	private Integer score;
	private Integer year;
	private BigDecimal difficulty;
	private Date createAt;
	private Integer phaseCode;
	private Integer categoryCode;
	private Integer questionCount = 0;
	// 学校名称
	private String schoolName;

	public Integer getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(Integer categoryCode) {
		this.categoryCode = categoryCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

}
