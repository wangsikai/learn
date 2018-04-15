package com.lanking.intercomm.yoocorrect.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 用户统计数据.
 * 
 * @author wanlong.che
 *
 */
@Data
public class CorrectUserStatData {

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
	 * 首次派题数（不重复习题个数）.
	 */
	private Long allotQuestionCount;

	/**
	 * 首次派题批改题数（首次派题完成率可由首次派题数和本字段计算）.
	 */
	private Long firstCorrectCount;

	/**
	 * 总批改题数.
	 */
	private Long correctCount;
}
