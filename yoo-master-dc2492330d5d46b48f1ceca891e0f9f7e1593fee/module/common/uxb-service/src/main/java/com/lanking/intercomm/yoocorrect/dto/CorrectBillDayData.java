package com.lanking.intercomm.yoocorrect.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 日流水数据对象.
 * 
 * @author wanlong.che
 *
 */
@Data
public class CorrectBillDayData {

	/**
	 * 快批用户ID
	 */
	private Long userId;

	/**
	 * 流水日期.
	 */
	private Date date;

	/**
	 * 批改题数.
	 */
	private int correctCount;

	/**
	 * 批改费用.
	 */
	private BigDecimal correctFee;

	/**
	 * 奖励费用.
	 */
	private BigDecimal rewardFee;

	/**
	 * 错改费用.
	 */
	private BigDecimal errorFee;

	/**
	 * 提现金额.
	 */
	private BigDecimal withdrawFee;
}
