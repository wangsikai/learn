package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 第二期科举预置作业题目
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_2_question")
@IdClass(ImperialExaminationQuestionKey.class)
public class ImperialExamination2Question extends ImperialExamination2QuestionKey {

	private static final long serialVersionUID = 4431973018283583618L;

	@Column(name = "sequence", precision = 3)
	private int sequence;

	/**
	 * 考场
	 */
	@Column(name = "room", precision = 3)
	private Integer room;

}
