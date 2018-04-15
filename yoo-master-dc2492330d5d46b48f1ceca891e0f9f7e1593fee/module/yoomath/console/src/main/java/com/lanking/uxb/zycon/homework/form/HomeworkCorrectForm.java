package com.lanking.uxb.zycon.homework.form;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 提供统一的批改接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
public class HomeworkCorrectForm {
	private List<Long> sqIds;
	private List<Long> stuHkIds;
	private List<HomeworkAnswerResult> results;
	private List<Long> hkIds;
	private List<Long> countDownTime;
	private List<Map<Long, HomeworkAnswerResult>> answerResults;
	private List<Long> rightRates;

	public List<Long> getSqIds() {
		return sqIds;
	}

	public void setSqIds(List<Long> sqIds) {
		this.sqIds = sqIds;
	}

	public List<Long> getStuHkIds() {
		return stuHkIds;
	}

	public void setStuHkIds(List<Long> stuHkIds) {
		this.stuHkIds = stuHkIds;
	}

	public List<HomeworkAnswerResult> getResults() {
		return results;
	}

	public void setResults(List<HomeworkAnswerResult> results) {
		this.results = results;
	}

	public List<Long> getHkIds() {
		return hkIds;
	}

	public void setHkIds(List<Long> hkIds) {
		this.hkIds = hkIds;
	}

	public List<Long> getCountDownTime() {
		return countDownTime;
	}

	public void setCountDownTime(List<Long> countDownTime) {
		this.countDownTime = countDownTime;
	}

	public List<Map<Long, HomeworkAnswerResult>> getAnswerResults() {
		return answerResults;
	}

	public void setAnswerResults(List<Map<Long, HomeworkAnswerResult>> answerResults) {
		this.answerResults = answerResults;
	}

	public List<Long> getRightRates() {
		return rightRates;
	}

	public void setRightRates(List<Long> rightRates) {
		this.rightRates = rightRates;
	}
}
