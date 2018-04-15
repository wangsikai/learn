package com.lanking.intercomm.yoocorrect.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 奖励配置.
 * 
 */
@Getter
@Setter
public class CorrectReward implements Serializable {
	private static final long serialVersionUID = -405516009186814224L;

	/**
	 * 类型.
	 */
	private CorrectRewardType type;

	/**
	 * 批改题量.
	 */
	private Integer correctQuestionCount;

	/**
	 * 奖励费用.
	 */
	private BigDecimal fee;

	/**
	 * 创建时间.
	 */
	private Date createAt;
}
