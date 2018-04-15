package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DailyPractise VO
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
public class VDailyPractise implements Serializable {
	private static final long serialVersionUID = -639922384812224644L;

	private long id;
	private String name;
	private int questionCount;
	private int doCount;
	private BigDecimal completeRate;
	private BigDecimal rightRate;
	private int rightCount;
	private int wrongCount;
	private Date createAt;
	private Date updateAt;
	private Date commitAt;
	private Double difficulty;
	// 每日一练平均难度对应的星级
	private int difficultyStar;
	private String rightRateTitle;
	private String completeRateTitle;
	// 是否为当天的每日一练
	private boolean nowDay;
	private Integer homeworkTime;

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

	public BigDecimal getCompleteRate() {
		return completeRate;
	}

	public void setCompleteRate(BigDecimal completeRate) {
		this.completeRate = completeRate;
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

	public Date getCommitAt() {
		return commitAt;
	}

	public void setCommitAt(Date commitAt) {
		this.commitAt = commitAt;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public int getDifficultyStar() {
		return difficultyStar;
	}

	public void setDifficultyStar(int difficultyStar) {
		this.difficultyStar = difficultyStar;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public String getCompleteRateTitle() {
		return completeRateTitle;
	}

	public void setCompleteRateTitle(String completeRateTitle) {
		this.completeRateTitle = completeRateTitle;
	}

	public boolean isNowDay() {
		return nowDay;
	}

	public void setNowDay(boolean nowDay) {
		this.nowDay = nowDay;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}
}
