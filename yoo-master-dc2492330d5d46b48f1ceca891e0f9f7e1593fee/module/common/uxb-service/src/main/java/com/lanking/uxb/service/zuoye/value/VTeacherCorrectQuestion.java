package com.lanking.uxb.service.zuoye.value;

import com.alibaba.fastjson.annotation.JSONField;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.user.value.VUser;

import java.io.Serializable;
import java.util.List;

/**
 * 教师批改学生题目时,使用此列表显示学生的答案
 *
 * @author xinyu.zhou
 * @since yoomath V1.9.1
 */
public class VTeacherCorrectQuestion implements Serializable {
	private static final long serialVersionUID = -2864108782130402979L;

	private Long stuHomeworkId;
	private VStudentHomeworkQuestion stuHomeworkQuestion;
	private List<VStudentHomeworkAnswer> answers;
	private VUser user;

	@JSONField(serialize = false)
	private Long userId;

	public VStudentHomeworkQuestion getStuHomeworkQuestion() {
		return stuHomeworkQuestion;
	}

	public void setStuHomeworkQuestion(VStudentHomeworkQuestion stuHomeworkQuestion) {
		this.stuHomeworkQuestion = stuHomeworkQuestion;
	}

	public List<VStudentHomeworkAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<VStudentHomeworkAnswer> answers) {
		this.answers = answers;
	}

	public VUser getUser() {
		return user;
	}

	public void setUser(VUser user) {
		this.user = user;
	}

	public Long getStuHomeworkId() {
		return stuHomeworkId;
	}

	public void setStuHomeworkId(Long stuHomeworkId) {
		this.stuHomeworkId = stuHomeworkId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
