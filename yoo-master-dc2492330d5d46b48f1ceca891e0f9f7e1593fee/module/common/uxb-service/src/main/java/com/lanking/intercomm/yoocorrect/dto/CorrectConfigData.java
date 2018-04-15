package com.lanking.intercomm.yoocorrect.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 传输的配置数据对象.
 * 
 * @author peng.zhao
 * @version 2018-3-12
 */
@Data
public class CorrectConfigData {

	private long id;

	/**
	 * 批改费用设置.
	 */
	private CorrectFeeConfig feeConfig;

	/**
	 * 奖励费用设置.
	 */
	private CorrectRewardConfig rewardConfig;

	/**
	 * 默认批改新用户的初始信用分.
	 */
	private Integer defaultTrustRank;

	/**
	 * 信用分最低值，低于该值的用户将不能继续批改.
	 */
	private Integer minTrustRank;

	/**
	 * 可提现的周次（1表示周一，7表示周日）.
	 */
	private Integer withdrawWeekDay;

	/**
	 * 提现开始时间（包含）.
	 * <p>
	 * 例如零点开始，此处xxxx-xx-xx 00:00:000，年月日忽略. <br>
	 * </p>
	 */
	private Date withdrawBt;

	/**
	 * 提现结束时间（不包含，注意此处为0点，则表示是当天的24点）.
	 * <p>
	 * 例如22点结束，此处xxxx-xx-xx 22:00:000，年月日忽略，22点整点不包含.<br>
	 * </p>
	 */
	private Date withdrawEt;

	/**
	 * 单日提现最大限额.
	 */
	private BigDecimal dayWithdrawMax;

	/**
	 * 单笔提现最小数额.
	 */
	private BigDecimal withdrawMin;

	/**
	 * 单日提现次数.
	 */
	private Integer dayWithdrawCount;

	/**
	 * 到账时长提示.
	 * <p>
	 * eg. "24小时内到账"
	 * </p>
	 */
	private String transferTimeComment;
}
