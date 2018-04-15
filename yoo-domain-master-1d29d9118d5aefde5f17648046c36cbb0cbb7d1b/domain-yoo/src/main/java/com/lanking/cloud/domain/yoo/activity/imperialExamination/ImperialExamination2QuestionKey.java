package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@MappedSuperclass
public class ImperialExamination2QuestionKey implements Serializable {

	private static final long serialVersionUID = 1573807439242130466L;

	@Id
	@Column(name = "activity_code", nullable = false)
	private Long activityCode;

	@Id
	@Column(name = "question_id", nullable = false)
	private long questionId;

	@Id
	@Column(name = "type", nullable = false, precision = 3)
	private ImperialExaminationType type;

	@Id
	@Column(name = "grade", nullable = false, precision = 3)
	private ImperialExaminationGrade grade;

	@Id
	@Column(name = "textbook_category_code", nullable = false)
	private Integer textbookCategoryCode;

	/**
	 * 第二期:标签1,2,3(2,3用于存储冲刺题目)
	 */
	@Id
	@Column(name = "tag", nullable = false, precision = 3)
	private Integer tag;

	public ImperialExamination2QuestionKey(Long activityCode, long questionId, ImperialExaminationType type,
			ImperialExaminationGrade grade, Integer textbookCategoryCode, Integer tag) {
		super();
		this.activityCode = activityCode;
		this.questionId = questionId;
		this.type = type;
		this.grade = grade;
		this.textbookCategoryCode = textbookCategoryCode;
		this.tag = tag;
	}

}
