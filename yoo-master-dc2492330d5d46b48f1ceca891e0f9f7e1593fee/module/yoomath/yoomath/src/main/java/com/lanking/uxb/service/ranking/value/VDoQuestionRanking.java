package com.lanking.uxb.service.ranking.value;

import java.math.BigDecimal;

/**
 * 做题排名抽象类
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月24日
 */
public class VDoQuestionRanking extends VRanking {

	private static final long serialVersionUID = -3420916573033010618L;

	// 正确率
	private BigDecimal rightRate;
	private String rightRateTitle;
	// 做题数量
	private long questionCount;

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(long questionCount) {
		this.questionCount = questionCount;
	}

}
