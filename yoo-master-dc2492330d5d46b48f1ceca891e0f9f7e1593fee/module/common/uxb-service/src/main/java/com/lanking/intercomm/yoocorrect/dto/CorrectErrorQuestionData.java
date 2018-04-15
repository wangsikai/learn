package com.lanking.intercomm.yoocorrect.dto;

import lombok.Data;

/**
 * 改错对象.
 * 
 * @author wanlong.che
 *
 */
@Data
public class CorrectErrorQuestionData {

	/**
	 * 批改人ID（小悠快批用户ID）.
	 */
	private Long correctUserId;

	/**
	 * 批改人ID（悠数学用户ID）.
	 */
	private Long uxbUserId;

	/**
	 * 学生作业习题ID.
	 */
	private Long studentHomeworkQuestionId;
}
