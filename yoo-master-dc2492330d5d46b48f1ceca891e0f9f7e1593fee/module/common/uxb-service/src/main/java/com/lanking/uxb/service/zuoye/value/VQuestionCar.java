package com.lanking.uxb.service.zuoye.value;

import com.lanking.cloud.domain.common.resource.question.Question;

/**
 * 作业蓝子返回数据
 *
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
public class VQuestionCar {
	private Long id;
	private Question.Type type;
	private Double difficult;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Question.Type getType() {
		return type;
	}

	public void setType(Question.Type type) {
		this.type = type;
	}

	public Double getDifficult() {
		return difficult;
	}

	public void setDifficult(Double difficult) {
		this.difficult = difficult;
	}
}
