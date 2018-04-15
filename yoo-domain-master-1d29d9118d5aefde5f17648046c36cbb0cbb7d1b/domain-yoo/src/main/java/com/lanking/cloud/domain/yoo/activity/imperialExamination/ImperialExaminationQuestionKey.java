package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
public class ImperialExaminationQuestionKey implements Serializable {

	private static final long serialVersionUID = 2179268382352634900L;

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

	public ImperialExaminationQuestionKey(Long activityCode, long questionId, ImperialExaminationType type,
			ImperialExaminationGrade grade) {
		super();
		this.activityCode = activityCode;
		this.questionId = questionId;
		this.type = type;
		this.grade = grade;
	}

}
