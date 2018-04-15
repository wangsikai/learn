package com.lanking.uxb.service.homework.form;

import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 批改接口参数
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月17日
 */
public class CorrectForm {
	private Long stuHkId;
	private Long stuHkQuestionId;
	private Long stuHkAnswerId;
	private Long questionId;
	// 题型
	private Type type;
	private HomeworkAnswerResult result;
	private Integer rightRate;

	public Long getStuHkId() {
		return stuHkId;
	}

	public void setStuHkId(Long stuHkId) {
		this.stuHkId = stuHkId;
	}

	public Long getStuHkQuestionId() {
		return stuHkQuestionId;
	}

	public void setStuHkQuestionId(Long stuHkQuestionId) {
		this.stuHkQuestionId = stuHkQuestionId;
	}

	public Long getStuHkAnswerId() {
		return stuHkAnswerId;
	}

	public void setStuHkAnswerId(Long stuHkAnswerId) {
		this.stuHkAnswerId = stuHkAnswerId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

}
