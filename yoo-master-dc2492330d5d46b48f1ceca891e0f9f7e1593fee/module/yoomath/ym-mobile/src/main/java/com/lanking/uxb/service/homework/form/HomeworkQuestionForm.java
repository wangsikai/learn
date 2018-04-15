package com.lanking.uxb.service.homework.form;

import com.lanking.uxb.service.base.form.AbstractHomeworkQuestionForm;

/**
 * 客户端保存作业答案参数
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年01月13日
 */
public class HomeworkQuestionForm extends AbstractHomeworkQuestionForm {

	// 作业ID
	private long homeworkId;
	// 学生作业ID
	private long stuHkId;
	// 学生题目ID
	private long stuHkQuestionId;
	// 做题时长
	private Integer doTime;

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public long getStuHkId() {
		return stuHkId;
	}

	public void setStuHkId(long stuHkId) {
		this.stuHkId = stuHkId;
	}

	public long getStuHkQuestionId() {
		return stuHkQuestionId;
	}

	public void setStuHkQuestionId(long stuHkQuestionId) {
		this.stuHkQuestionId = stuHkQuestionId;
	}

	public Integer getDoTime() {
		return doTime;
	}

	public void setDoTime(Integer doTime) {
		this.doTime = doTime;
	}
}
