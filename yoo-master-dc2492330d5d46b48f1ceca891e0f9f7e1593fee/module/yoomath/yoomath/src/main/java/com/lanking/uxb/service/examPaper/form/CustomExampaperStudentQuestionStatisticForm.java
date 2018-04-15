package com.lanking.uxb.service.examPaper.form;

import java.io.Serializable;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 学生题目统计表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月18日
 */
public class CustomExampaperStudentQuestionStatisticForm implements Serializable {
	private static final long serialVersionUID = 6093534024533969828L;

	/**
	 * 学生组卷习题ID.
	 */
	private Long stuQuestionId;

	/**
	 * 习题统计结果.
	 */
	private HomeworkAnswerResult result;

	public Long getStuQuestionId() {
		return stuQuestionId;
	}

	public void setStuQuestionId(Long stuQuestionId) {
		this.stuQuestionId = stuQuestionId;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}
}
