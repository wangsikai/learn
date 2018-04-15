package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 第一期科举预置作业题目
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Getter
@Setter
@Entity
@Table(name = "imperial_exam_question")
@IdClass(ImperialExaminationQuestionKey.class)
public class ImperialExaminationQuestion extends ImperialExaminationQuestionKey {

	private static final long serialVersionUID = 8663360794710678580L;

	@Column(name = "sequence", precision = 3)
	private int sequence;

}
