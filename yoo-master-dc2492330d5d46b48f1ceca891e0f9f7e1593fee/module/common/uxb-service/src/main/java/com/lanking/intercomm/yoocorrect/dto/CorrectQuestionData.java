package com.lanking.intercomm.yoocorrect.dto;

import java.io.Serializable;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.homework.CorrectQuestionSource;

import lombok.Data;

/**
 * 传输的批改题目数据对象.
 * 
 * @author wanlong.che
 *
 */
@Data
public class CorrectQuestionData implements Serializable {
	private static final long serialVersionUID = -2549766417955246252L;

	/**
	 * 习题ID，对应uxb系统中的question id.
	 */
	private Long questionId;

	/**
	 * 学生ID，对应uxb系统中的做题学生ID.
	 */
	private Long studentId;

	/**
	 * 习题类型.
	 */
	private Question.Type type;

	/**
	 * 习题来源.
	 */
	private CorrectQuestionSource source;

	/**
	 * 习题分类.
	 */
	private CorrectQuestionCategory category;

	/**
	 * 业务ID，由CorrectQuestionSource定义，服务通信定位获取业务数据时使用.
	 * <p>
	 * 作业题：此处为student_homework_question的ID<br>
	 * </p>
	 */
	private Long bizId;
}
