package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 题目&考点关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class QuestionExaminationPointKey implements Serializable {

	private static final long serialVersionUID = -7819867971588356490L;

	/**
	 * 题目ID
	 */
	@Id
	@Column(name = "question_id", nullable = false)
	private long questionId;

	/**
	 * 考点代码
	 */
	@Id
	@Column(name = "examination_point_code", nullable = false)
	private long examinationPointCode;

	public QuestionExaminationPointKey(long questionId, int examinationPointCode) {
		this.questionId = questionId;
		this.examinationPointCode = examinationPointCode;
	}

	public QuestionExaminationPointKey() {
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getExaminationPointCode() {
		return examinationPointCode;
	}

	public void setExaminationPointCode(long examinationPointCode) {
		this.examinationPointCode = examinationPointCode;
	}
}
