package com.lanking.uxb.service.holiday.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;

/**
 * 学生假期作业题目VO
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
public class VHolidayStuHomeworkItemQuestion implements Serializable {

	private static final long serialVersionUID = -2449038490446707886L;

	private Long id;

	// 假日作业类型
	private HolidayHomeworkType type;

	// 假日作业ID
	private Long holidayHomeworkId;

	// 假日作业项ID
	private Long holidayHomeworkItemId;

	// 学生假日作业ID
	private Long holidayStuHomeworkId;

	// 学生假日作业项ID
	private Long holidayStuHomeworkItemId;

	// 题目ID
	private Long questionId;

	// 解题过程图片
	private String solvingImg;
	// 答题图片
	private String answerImg;
	// 多张答题图片
	private List<String> answerImgs;
	// 多张答题图片ID
	private List<Long> answerImgIds;
	// 批改后结果iD
	private List<Long> notationAnswerImgIds;
	// 批改后结果
	private List<String> notationAnswerImgs;

	// 结果
	private HomeworkAnswerResult result;

	// 批改评语
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HolidayHomeworkType getType() {
		return type;
	}

	public void setType(HolidayHomeworkType type) {
		this.type = type;
	}

	public Long getHolidayHomeworkId() {
		return holidayHomeworkId;
	}

	public void setHolidayHomeworkId(Long holidayHomeworkId) {
		this.holidayHomeworkId = holidayHomeworkId;
	}

	public Long getHolidayHomeworkItemId() {
		return holidayHomeworkItemId;
	}

	public void setHolidayHomeworkItemId(Long holidayHomeworkItemId) {
		this.holidayHomeworkItemId = holidayHomeworkItemId;
	}

	public String getAnswerImg() {
		return answerImg;
	}

	public void setAnswerImg(String answerImg) {
		this.answerImg = answerImg;
	}

	public List<String> getAnswerImgs() {
		return answerImgs;
	}

	public void setAnswerImgs(List<String> answerImgs) {
		this.answerImgs = answerImgs;
	}

	public Long getHolidayStuHomeworkId() {
		return holidayStuHomeworkId;
	}

	public void setHolidayStuHomeworkId(Long holidayStuHomeworkId) {
		this.holidayStuHomeworkId = holidayStuHomeworkId;
	}

	public Long getHolidayStuHomeworkItemId() {
		return holidayStuHomeworkItemId;
	}

	public void setHolidayStuHomeworkItemId(Long holidayStuHomeworkItemId) {
		this.holidayStuHomeworkItemId = holidayStuHomeworkItemId;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getSolvingImg() {
		return solvingImg;
	}

	public void setSolvingImg(String solvingImg) {
		this.solvingImg = solvingImg;
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

	public List<Long> getAnswerImgIds() {
		return answerImgIds;
	}

	public void setAnswerImgIds(List<Long> answerImgIds) {
		this.answerImgIds = answerImgIds;
	}

	public List<String> getNotationAnswerImgs() {
		return notationAnswerImgs;
	}

	public void setNotationAnswerImgs(List<String> notationAnswerImgs) {
		this.notationAnswerImgs = notationAnswerImgs;
	}

	public List<Long> getNotationAnswerImgIds() {
		return notationAnswerImgIds;
	}

	public void setNotationAnswerImgIds(List<Long> notationAnswerImgIds) {
		this.notationAnswerImgIds = notationAnswerImgIds;
	}

}
