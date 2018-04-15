package com.lanking.uxb.service.resources.value;

import java.io.Serializable;
import java.util.List;

/**
 * 学生作业详情VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月19日
 */
public class VStudentHomeworkDetail implements Serializable {

	private static final long serialVersionUID = 6791485989415211652L;

	private VStudentHomework studentHomework;
	private List<VStudentHomeworkQuestion> studentHomeworkQuestions;

	public VStudentHomework getStudentHomework() {
		return studentHomework;
	}

	public void setStudentHomework(VStudentHomework studentHomework) {
		this.studentHomework = studentHomework;
	}

	public List<VStudentHomeworkQuestion> getStudentHomeworkQuestions() {
		return studentHomeworkQuestions;
	}

	public void setStudentHomeworkQuestions(List<VStudentHomeworkQuestion> studentHomeworkQuestions) {
		this.studentHomeworkQuestions = studentHomeworkQuestions;
	}
}
