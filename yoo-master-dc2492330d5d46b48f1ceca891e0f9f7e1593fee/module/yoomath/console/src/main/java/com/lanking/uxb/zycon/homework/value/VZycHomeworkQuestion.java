package com.lanking.uxb.zycon.homework.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 作业习题VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月20日
 */
public class VZycHomeworkQuestion implements Serializable {

	private static final long serialVersionUID = 5599084460077712421L;

	private long id;
	private long homeworkId;
	private long questionId;
	private int sequence;
	private BigDecimal rightRate;
	private String rightRateTitle;
	private int rightCount;
	private int wrongCount;
	private VZycQuestion question;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
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

	public VZycQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VZycQuestion question) {
		this.question = question;
	}

	public String getRightRateTitle() {
		if (StringUtils.isBlank(rightRateTitle)) {
			if (getRightRate() == null) {
				setRightRateTitle(StringUtils.EMPTY);
			} else {
				setRightRateTitle(String.valueOf(getRightRate().setScale(2, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}
}
