package com.lanking.uxb.rescon.exam.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopicType;
import com.lanking.uxb.rescon.question.value.VQuestion;

public class VExamPaperTopic implements Serializable {

	private static final long serialVersionUID = -8248709956285879607L;

	private Long id;
	private Long examId;
	private String name;
	private Integer sequence;
	private List<VQuestion> questionList;
	private ExamPaperTopicType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<VQuestion> getQuestionList() {
		return questionList;
	}

	public void setQuestionList(List<VQuestion> questionList) {
		this.questionList = questionList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Long getExamId() {
		return examId;
	}

	public void setExamId(Long examId) {
		this.examId = examId;
	}

	public ExamPaperTopicType getType() {
		return type;
	}

	public void setType(ExamPaperTopicType type) {
		this.type = type;
	}

}
