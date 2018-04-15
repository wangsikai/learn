package com.lanking.intercomm.yoocorrect.dto;

import lombok.Data;

/**
 * 提现记录查询条件
 * 
 * @author peng.zhao
 * @version 2018-3-15
 */
@Data
public class CorrectWithdrawApplyRequest {

	/**
	 * 数据量
	 */
	private Integer size = 20;
	
	/**
	 * 页码
	 */
	private Integer page = 1;
	
	private Integer year;
	
	private Integer month;
	
	/**
	 * 订单号,对应CorrectWithdrawApply表主键
	 */
	private Long correctWithdrawApplyId;
	
	/**
	 * 帐号
	 */
	private String accountName;
	
	/**
	 * 姓名
	 */
	private String realName;
	
	/**
	 * 提现状态
	 */
	private WithdrawStatus status;
}
