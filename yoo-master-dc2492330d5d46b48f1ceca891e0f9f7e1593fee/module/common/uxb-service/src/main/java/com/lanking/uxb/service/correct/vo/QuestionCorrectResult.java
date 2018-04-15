package com.lanking.uxb.service.correct.vo;

import java.io.Serializable;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;

import lombok.Getter;
import lombok.Setter;

/**
 * 批改结果.
 * 
 * @author wanlong.che
 *
 */
@Getter
@Setter
public class QuestionCorrectResult implements Serializable {
	private static final long serialVersionUID = -1067034591411986663L;

	public QuestionCorrectResult() {
	};

	public QuestionCorrectResult(long studentHomeworkQuestionId, HomeworkAnswerResult result,
			QuestionCorrectType correctType, Integer rightRate) {
		this.studentHomeworkQuestionId = studentHomeworkQuestionId;
		this.result = result;
		this.correctType = correctType;
		this.rightRate = rightRate;
	}

	/**
	 * 作业习题ID.
	 */
	private long studentHomeworkQuestionId;

	/**
	 * 批改结果.
	 */
	private HomeworkAnswerResult result;

	/**
	 * 批改方式.
	 */
	private QuestionCorrectType correctType;

	/**
	 * 正确率.
	 */
	private Integer rightRate;
}
