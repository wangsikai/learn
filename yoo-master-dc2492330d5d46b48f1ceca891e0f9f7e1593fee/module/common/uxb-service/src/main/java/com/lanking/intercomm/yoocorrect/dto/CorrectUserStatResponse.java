package com.lanking.intercomm.yoocorrect.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 传输的用户统计数据对象.
 * 
 * @author peng.zhao
 * @version 2018-3-14
 */
@Data
public class CorrectUserStatResponse {

	private Long userId;
	
	/**
	 * 填空题平均批改用时（单位：秒，采用历史月度数据+当月实时数据计算）.
	 */
	private Integer blankQuestionTime;
	
	/**
	 * 解答题平均批改用时（单位：秒，采用历史月度数据+当月实时数据计算）.
	 */
	private Integer answerQuestionTime;
	
	/**
	 * 填空题平均错改率（采用历史月度数据+当月实时数据计算）.
	 */
	private BigDecimal blankQuestionErrorRate;
	
	/**
	 * 解答题平均错改率（采用历史月度数据+当月实时数据计算）.
	 */
	private BigDecimal answerQuestionErrorRate;
	
	/**
	 * 首次错题完成率
	 */
	private BigDecimal firstCorrectRate;
	
	/**
	 * 用户信息
	 */
	private CorrectUserResponse user;
}
