package com.lanking.cloud.domain.common.resource.question;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.lanking.cloud.sdk.bean.Status;

/**
 * 题目&考点关系表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(QuestionExaminationPointKey.class)
@Table(name = "question_examination_point")
public class QuestionExaminationPoint extends QuestionExaminationPointKey {

	private static final long serialVersionUID = 2106872436445134473L;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3, nullable = false)
	private Status status = Status.ENABLED;
}
