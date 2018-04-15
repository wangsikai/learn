package com.lanking.uxb.service.holiday.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;

/**
 * 学生假期作业答案VO
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
public class VHolidayStuHomeworkItemAnswer implements Serializable {

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

	// 学生假日作业项-题目ID
	private Long holidayStuHomeworkItemQuestionId;

	// 顺序
	private Integer sequence;

	// latex答案内容(如果是多选题直接拼接一个字符串放入，如：ABD)
	private String content;

	// ascii math 答案
	private String contentAscii;

	private String imageContent;
	// 没有<ux-mth>标签的编码内容
	private String noLabelContentAscii;

	// 答题时间
	private Date answerAt;

	// 结果
	private HomeworkAnswerResult result;

	// 批改时间
	private Date correctAt;

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

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public Long getHolidayStuHomeworkItemQuestionId() {
		return holidayStuHomeworkItemQuestionId;
	}

	public void setHolidayStuHomeworkItemQuestionId(Long holidayStuHomeworkItemQuestionId) {
		this.holidayStuHomeworkItemQuestionId = holidayStuHomeworkItemQuestionId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentAscii() {
		return contentAscii;
	}

	public void setContentAscii(String contentAscii) {
		this.contentAscii = contentAscii;
	}

	public String getImageContent() {
		return imageContent;
	}

	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}

	public String getNoLabelContentAscii() {
		return noLabelContentAscii;
	}

	public void setNoLabelContentAscii(String noLabelContentAscii) {
		this.noLabelContentAscii = noLabelContentAscii;
	}

	public Date getAnswerAt() {
		return answerAt;
	}

	public void setAnswerAt(Date answerAt) {
		this.answerAt = answerAt;
	}

	public Date getCorrectAt() {
		return correctAt;
	}

	public void setCorrectAt(Date correctAt) {
		this.correctAt = correctAt;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
