package com.lanking.uxb.service.homework.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 作业题目-学生
 * 
 * @author peng.zhao
 * @version 2018-2-26
 */
public class VHomeworkQuestionStudent implements Serializable {

	private static final long serialVersionUID = -4988323955186457762L;
	
	private long questionId;
	
	/**
	 * 序号
	 */
	private Integer sequence;
	
	/**
	 * 学生作业题目
	 */
	private List<VStudentHomeworkQuestion> studentHomeworkQuestions;
	
	/**
	 * 学生个人信息，和studentHomeworkQuestions一一对应
	 */
	private List<VUser> users;
	
	private VQuestion question;

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public List<VStudentHomeworkQuestion> getStudentHomeworkQuestions() {
		return studentHomeworkQuestions;
	}

	public void setStudentHomeworkQuestions(List<VStudentHomeworkQuestion> studentHomeworkQuestions) {
		this.studentHomeworkQuestions = studentHomeworkQuestions;
	}

	public List<VUser> getUsers() {
		return users;
	}

	public void setUsers(List<VUser> users) {
		this.users = users;
	}

	public VQuestion getQuestion() {
		return question;
	}

	public void setQuestion(VQuestion question) {
		this.question = question;
	}

}
