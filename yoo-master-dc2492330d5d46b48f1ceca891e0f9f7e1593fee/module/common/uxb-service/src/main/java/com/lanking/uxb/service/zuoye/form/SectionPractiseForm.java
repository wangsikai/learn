package com.lanking.uxb.service.zuoye.form;

import java.util.List;
import java.util.Map;

/**
 * 章节练习保存form
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.1
 */
public class SectionPractiseForm {
	private Long id;
	private String name;
	private Double difficulty;
	private long sectionCode;
	private int homeworkTime;
	private long userId;
	// 学生答案列表
	private List<Map<Long, List<String>>> answerList;
	// 章节练习的答案
	private List<Long> questionIds;

	public SectionPractiseForm() {
	}

	public SectionPractiseForm(Long id, String name, Double difficulty, long sectionCode, int homeworkTime,
			long userId, List<Map<Long, List<String>>> answerList, List<Long> questionIds) {
		this.id = id;
		this.name = name;
		this.difficulty = difficulty;
		this.sectionCode = sectionCode;
		this.homeworkTime = homeworkTime;
		this.userId = userId;
		this.answerList = answerList;
		this.questionIds = questionIds;
	}

	public List<Map<Long, List<String>>> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Map<Long, List<String>>> answerList) {
		this.answerList = answerList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public int getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(int homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
