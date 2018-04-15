package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SectionPractise VO {@link SectionPractise}
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.1
 */
public class VSectionPractise implements Serializable {
	private static final long serialVersionUID = 5461336658096771083L;

	private long id;
	private long userId;
	private String name;
	private int questionCount;
	private int doCount;
	private BigDecimal rightRate;
	private String rightRateTitle;
	private int rightCount;
	private int wrongCount;
	private Date createAt;
	private Date updateAt;
	private Double difficulty;
	private long sectionCode;
	private Integer homeworkTime;
	private BigDecimal completeRate;
	private String completeRateTitle;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
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

	public int getRightCount() {
		return rightCount;
	}

	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}

	public int getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public BigDecimal getCompleteRate() {
		return completeRate;
	}

	public void setCompleteRate(BigDecimal completeRate) {
		this.completeRate = completeRate;
	}

	public String getCompleteRateTitle() {
		return completeRateTitle;
	}

	public void setCompleteRateTitle(String completeRateTitle) {
		this.completeRateTitle = completeRateTitle;
	}
}
