package com.lanking.uxb.service.youngyedu.homework.form;

import com.lanking.uxb.service.base.form.AbstractHomeworkQuestionForm;

/**
 * @author xinyu.zhou
 * @since 3.0.3
 */
public class YoungHomeworkQuestionForm extends AbstractHomeworkQuestionForm {
	private long homeworkId;
	private long stuHkId;
	private long stuHkQuestionId;
	// 手写轨迹数据
	private String handWriting;

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

	public String getHandWriting() {
		return handWriting;
	}

	public void setHandWriting(String handWriting) {
		this.handWriting = handWriting;
	}
}
