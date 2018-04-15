package com.lanking.uxb.service.correct.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 单个学生习题的批改对象.
 * 
 * @author wanlong.che
 *
 */
public class QuestionCorrectObject implements Serializable {
	private static final long serialVersionUID = 7171160925215500690L;

	/**
	 * 对应的学生作业ID.
	 */
	private Long studentHomeworkId;

	/**
	 * 作业题目ID.
	 */
	private Long stuHomeworkQuestionId;

	/**
	 * 习题Type.
	 */
	private Type questionType;

	/**
	 * 习题批改结果（解答题和填空题不传，通过answerResultMap答案批改结果计算）.
	 */
	private HomeworkAnswerResult questionResult;

	/**
	 * 习题正确率（仅解答题传）.
	 */
	private Integer questionRightRate;

	/**
	 * 作业答案批改结果集合（仅填空题传）.
	 */
	private Map<Long, HomeworkAnswerResult> answerResultMap;

	/**
	 * 批改后合成图片id.
	 */
	private Long notationImageId;

	/**
	 * 批注.
	 */
	private String notation;

	/**
	 * 批注前原图片Id.
	 */
	private Long answerImgId;

	/**
	 * 多图--批改后合成图片id(一个图时不传值).
	 */
	private List<Long> notationImageIds;

	/**
	 * 多图--批注(一个图时不传值).
	 */
	private List<String> notations;

	/**
	 * 多图--批注前原图片.
	 */
	private List<Long> answerImgIds;

	public Long getStudentHomeworkId() {
		return studentHomeworkId;
	}

	public void setStudentHomeworkId(Long studentHomeworkId) {
		this.studentHomeworkId = studentHomeworkId;
	}

	public Long getStuHomeworkQuestionId() {
		return stuHomeworkQuestionId;
	}

	public void setStuHomeworkQuestionId(Long stuHomeworkQuestionId) {
		this.stuHomeworkQuestionId = stuHomeworkQuestionId;
	}

	public HomeworkAnswerResult getQuestionResult() {
		return questionResult;
	}

	public void setQuestionResult(HomeworkAnswerResult questionResult) {
		this.questionResult = questionResult;
	}

	public Integer getQuestionRightRate() {
		return questionRightRate;
	}

	public void setQuestionRightRate(Integer questionRightRate) {
		this.questionRightRate = questionRightRate;
	}

	public Map<Long, HomeworkAnswerResult> getAnswerResultMap() {
		return answerResultMap;
	}

	public void setAnswerResultMap(Map<Long, HomeworkAnswerResult> answerResultMap) {
		this.answerResultMap = answerResultMap;
	}

	public Long getNotationImageId() {
		return notationImageId;
	}

	public void setNotationImageId(Long notationImageId) {
		this.notationImageId = notationImageId;
	}

	public String getNotation() {
		return notation;
	}

	public void setNotation(String notation) {
		this.notation = notation;
	}

	public Long getAnswerImgId() {
		return answerImgId;
	}

	public void setAnswerImgId(Long answerImgId) {
		this.answerImgId = answerImgId;
	}

	public List<Long> getNotationImageIds() {
		return notationImageIds;
	}

	public void setNotationImageIds(List<Long> notationImageIds) {
		this.notationImageIds = notationImageIds;
	}

	public List<String> getNotations() {
		return notations;
	}

	public void setNotations(List<String> notations) {
		this.notations = notations;
	}

	public List<Long> getAnswerImgIds() {
		return answerImgIds;
	}

	public void setAnswerImgIds(List<Long> answerImgIds) {
		this.answerImgIds = answerImgIds;
	}

	public Type getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Type questionType) {
		this.questionType = questionType;
	}

}
