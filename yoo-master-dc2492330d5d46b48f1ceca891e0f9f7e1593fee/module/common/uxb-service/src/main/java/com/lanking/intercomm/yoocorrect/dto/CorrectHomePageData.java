package com.lanking.intercomm.yoocorrect.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 首页数据.
 * 
 * @author wanlong.che
 *
 */
@Data
public class CorrectHomePageData {

	/**
	 * 今日批改题数.
	 */
	private int todayCorrect = 0;

	/**
	 * 今日流水.
	 */
	private BigDecimal todayEarn;

	/**
	 * 今日是否可提现.
	 */
	private boolean canWithdraw = false;

	/**
	 * 下一个提现日.
	 */
	private String nextWithdrawDate;

	/**
	 * 提现开始时间.
	 */
	private String withdrawBt;

	/**
	 * 提现结束时间.
	 */
	private String withdrawEt;

	/**
	 * 每日奖励任务.
	 */
	private List<Map<String, Object>> todayRewards;
}
