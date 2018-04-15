package com.lanking.uxb.service.resources.value;

import java.io.Serializable;
import java.util.List;

/**
 * 作业详情VO
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月17日
 */
public class VHomeworkDetail implements Serializable {

	private static final long serialVersionUID = 9083378081052650138L;

	private VHomework homework;
	private List<VHomeworkQuestion> homeworkQuestions;

	public VHomework getHomework() {
		return homework;
	}

	public void setHomework(VHomework homework) {
		this.homework = homework;
	}

	public List<VHomeworkQuestion> getHomeworkQuestions() {
		return homeworkQuestions;
	}

	public void setHomeworkQuestions(List<VHomeworkQuestion> homeworkQuestions) {
		this.homeworkQuestions = homeworkQuestions;
	}

}
