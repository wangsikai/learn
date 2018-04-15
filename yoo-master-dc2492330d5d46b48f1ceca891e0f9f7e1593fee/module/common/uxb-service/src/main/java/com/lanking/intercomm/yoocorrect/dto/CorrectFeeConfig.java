package com.lanking.intercomm.yoocorrect.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 快批费用设置.
 *
 */
@Getter
@Setter
public class CorrectFeeConfig {

	/**
	 * 填空题批改费用.
	 */
	private BigDecimal blankQuestionFee;

	/**
	 * 填空题错误惩罚扣减比例，例如3倍，此处为3.
	 */
	private Integer blankQuestionReduceRate;

	/**
	 * 解答题批改费用.
	 */
	private BigDecimal answerQuestionFee;

	/**
	 * 解答题错误惩罚扣减比例，例如3倍，此处为3.
	 */
	private Integer answerQuestionReduceRate;
}
