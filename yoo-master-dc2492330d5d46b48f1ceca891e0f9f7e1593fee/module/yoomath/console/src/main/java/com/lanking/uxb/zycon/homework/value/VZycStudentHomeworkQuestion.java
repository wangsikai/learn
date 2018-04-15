package com.lanking.uxb.zycon.homework.value;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.sdk.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

/**
 * 学生作业习题VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月20日
 */
public class VZycStudentHomeworkQuestion implements Serializable {

	private static final long serialVersionUID = 5599084460077712421L;

	private long id;
	private long studentHomeworkId;
	private long questionId;
	private String solvingImg = StringUtils.EMPTY;
	private long solvingImgId = 0;
	private HomeworkAnswerResult result;
	private String comment;
	private Date correctAt;
	private String answerImg = StringUtils.EMPTY;
	private long answerImgId = 0;
	
	//解题过程相关，可以有多个图片
	private List<String> answerImgs;
	private List<Long> answerImgIds;

	private VZycQuestion question;
	
	/**
	 * 是否是新订正题(默认值为false，小悠快批上线后新流程下的订正题才有此标记)
	 * 
	 * @since 小优快批
	 */
	private boolean newCorrect = false;
	
	// 是否已订正
	private boolean revised = false;
	
	private Integer rightRate;
	private String rightRateTitle;
	
	/**
	 * 习题的预期批改方式（可能还未批改）.
	 * 
	 * @since 小优快批
	 */
	@Column(name = "correct_type")
	private QuestionCorrectType correctType = QuestionCorrectType.DEFAULT;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentHomeworkId() {
		return studentHomeworkId;
	}

	public void setStudentHomeworkId(long studentHomeworkId) {
		this.studentHomeworkId = studentHomeworkId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public String getSolvingImg() {
		return solvingImg;
	}

	public void setSolvingImg(String solvingImg) {
		this.solvingImg = solvingImg;
	}

	public long getSolvingImgId() {
		return solvingImgId;
	}

	public void setSolvingImgId(long solvingImgId) {
		this.solvingImgId = solvingImgId;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCorrectAt() {
		return correctAt;
	}

	public void setCorrectAt(Date correctAt) {
		this.correctAt = correctAt;
	}

	public VZycQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VZycQuestion question) {
		this.question = question;
	}

	public String getAnswerImg() {
		return answerImg;
	}

	public void setAnswerImg(String answerImg) {
		this.answerImg = answerImg;
	}

	public long getAnswerImgId() {
		return answerImgId;
	}

	public void setAnswerImgId(long answerImgId) {
		this.answerImgId = answerImgId;
	}

	public List<String> getAnswerImgs() {
		return answerImgs;
	}

	public void setAnswerImgs(List<String> answerImgs) {
		this.answerImgs = answerImgs;
	}

	public List<Long> getAnswerImgIds() {
		return answerImgIds;
	}

	public void setAnswerImgIds(List<Long> answerImgIds) {
		this.answerImgIds = answerImgIds;
	}

	public boolean isNewCorrect() {
		return newCorrect;
	}

	public void setNewCorrect(boolean newCorrect) {
		this.newCorrect = newCorrect;
	}

	public boolean isRevised() {
		return revised;
	}

	public void setRevised(boolean revised) {
		this.revised = revised;
	}

	public QuestionCorrectType getCorrectType() {
		return correctType;
	}

	public void setCorrectType(QuestionCorrectType correctType) {
		this.correctType = correctType;
	}
	
	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	public String getRightRateTitle() {
		if (StringUtils.isBlank(rightRateTitle)) {
			if (getRightRate() == null) {
				setRightRateTitle(StringUtils.EMPTY);
			} else {
				setRightRateTitle(rightRate.intValue() + "%");
			}
		}
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}
}
