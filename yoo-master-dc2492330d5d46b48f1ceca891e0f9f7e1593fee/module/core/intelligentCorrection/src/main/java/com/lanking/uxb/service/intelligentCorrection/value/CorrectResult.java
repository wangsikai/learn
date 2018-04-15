package com.lanking.uxb.service.intelligentCorrection.value;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionAutoCorrectMethod;

/**
 * 批改结果
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月9日
 */
public class CorrectResult {
	/**
	 * 批改结果
	 */
	private HomeworkAnswerResult result;

	/**
	 * 是否可信
	 */
	private boolean credible;

	/**
	 * 批改方式方法
	 */
	private QuestionAutoCorrectMethod method;

	public CorrectResult() {
		super();
	}

	public CorrectResult(HomeworkAnswerResult result, boolean credible, QuestionAutoCorrectMethod method) {
		super();
		this.result = result;
		if (result == HomeworkAnswerResult.UNKNOW) {
			this.credible = false;
		} else {
			this.credible = credible;
		}
		this.method = method;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public boolean isCredible() {
		return credible;
	}

	public QuestionAutoCorrectMethod getMethod() {
		return method;
	}

}
