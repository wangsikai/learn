package com.lanking.uxb.service.holiday.value;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 作业项习题VO
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月23日 下午3:46:25
 */
public class VHolidayHomeworkItemQuestion implements Serializable {

	private static final long serialVersionUID = -2320844707035476710L;
	private long homeworkItemId;
	private long questionId;
	private int sequence;
	private BigDecimal rightRate;
	private String rightRateTitle;
	private int rightCount;
	private int wrongCount;

	public long getHomeworkItemId() {
		return homeworkItemId;
	}

	public void setHomeworkItemId(long homeworkItemId) {
		this.homeworkItemId = homeworkItemId;
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

	public String getRightRateTitle() {
		if (StringUtils.isBlank(rightRateTitle)) {
			if (getRightRate() == null) {
				setRightRateTitle(StringUtils.EMPTY);
			} else {
				setRightRateTitle(String.valueOf(getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}
}
