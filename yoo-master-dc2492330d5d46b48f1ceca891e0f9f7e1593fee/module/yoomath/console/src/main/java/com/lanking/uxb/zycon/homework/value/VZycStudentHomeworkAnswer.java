package com.lanking.uxb.zycon.homework.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 学生作业答案VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月20日
 */
public class VZycStudentHomeworkAnswer implements Serializable {

	private static final long serialVersionUID = -3664495996171484708L;

	private long id;
	private long studentHomeworkQuestionId;
	private int sequence;
	private String content;
	private String contentAscii;
	private HomeworkAnswerResult result;
	private long answerId;
	private Date answerAt;
	private Date correctAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentHomeworkQuestionId() {
		return studentHomeworkQuestionId;
	}

	public void setStudentHomeworkQuestionId(long studentHomeworkQuestionId) {
		this.studentHomeworkQuestionId = studentHomeworkQuestionId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(long answerId) {
		this.answerId = answerId;
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

	public String getContentAscii() {
		return contentAscii;
	}

	public void setContentAscii(String contentAscii) {
		this.contentAscii = contentAscii;
	}
}
