package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;

import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 学校题目的VO
 * 
 * @since yoomath V1.4.2
 * @author wangsenhao
 *
 */
public class VSchoolQuestion implements Serializable {

	private static final long serialVersionUID = -8636532131322496235L;
	/**
	 * 学校题目id
	 */
	private Long schoolQuestionId;

	private VQuestion vQuestion;

	public VQuestion getvQuestion() {
		return vQuestion;
	}

	public void setvQuestion(VQuestion vQuestion) {
		this.vQuestion = vQuestion;
	}

	public Long getSchoolQuestionId() {
		return schoolQuestionId;
	}

	public void setSchoolQuestionId(Long schoolQuestionId) {
		this.schoolQuestionId = schoolQuestionId;
	}

}
