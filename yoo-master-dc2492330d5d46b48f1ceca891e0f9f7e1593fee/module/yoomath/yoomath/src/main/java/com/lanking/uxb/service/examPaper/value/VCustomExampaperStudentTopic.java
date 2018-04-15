package com.lanking.uxb.service.examPaper.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopicType;

/**
 * 学生试卷中的试题分类信息.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月15日
 */
public class VCustomExampaperStudentTopic implements Serializable {
	private static final long serialVersionUID = -5988681403284878076L;

	/**
	 * topicID.
	 */
	private Long id;

	/**
	 * 分类名称.
	 */
	private String name;

	/**
	 * 次序.
	 */
	private Integer sequence;

	/**
	 * 分类类型.
	 */
	private CustomExampaperTopicType type;

	/**
	 * 总分.
	 */
	private Integer totalScore;

	/**
	 * 每道选择题/每空分值.
	 */
	private Integer singleScore;

	/**
	 * 学生习题.
	 */
	List<VCustomExampaperStudentQuestion> questions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public CustomExampaperTopicType getType() {
		return type;
	}

	public void setType(CustomExampaperTopicType type) {
		this.type = type;
	}

	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getSingleScore() {
		return singleScore;
	}

	public void setSingleScore(Integer singleScore) {
		this.singleScore = singleScore;
	}

	public List<VCustomExampaperStudentQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<VCustomExampaperStudentQuestion> questions) {
		this.questions = questions;
	}
}
