package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 教师错题VO
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月17日
 */
public class VTeacherFallibleQuestion implements Serializable {

	private static final long serialVersionUID = -1023655229939625583L;

	private long id;
	private long questionId;
	private int doNum;
	private int rightNum;
	private BigDecimal rightRate = new BigDecimal(0);
	private String rightRateTitle;
	private Date createAt;
	private Date updateAt;
	private VQuestion question;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public int getDoNum() {
		return doNum;
	}

	public void setDoNum(int doNum) {
		this.doNum = doNum;
	}

	public int getRightNum() {
		return rightNum;
	}

	public void setRightNum(int rightNum) {
		this.rightNum = rightNum;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
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

	public VQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VQuestion question) {
		this.question = question;
	}

}
