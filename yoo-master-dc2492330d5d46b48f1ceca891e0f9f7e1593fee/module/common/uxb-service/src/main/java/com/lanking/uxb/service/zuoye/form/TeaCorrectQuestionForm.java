package com.lanking.uxb.service.zuoye.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

import httl.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教师批改form
 *
 * @author xinyu.zhou
 * @since yoomath V1.9.1
 */
public class TeaCorrectQuestionForm {
	// 作业id
	private long homeworkId;
	// 学生作业列表
	private List<Long> stuHkIds;
	// 需要批改的学生列表
	private List<Long> stuHkQuestionIds;
	// 批改后的结果
	private List<HomeworkAnswerResult> results;
	// 批改后的正确率
	private List<Integer> rightRates;
	// 是否是对简答题进行批改
	private boolean isQuestionAnswering = false;
	// 批改后合成图片id
	private Long notationImageId;
	// 填空题 答案id -> 结果
	private String answerResults;
	// 批注
	private String notation;
	// 批注前图片
	private Long answerImgId;

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
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

	public List<Integer> getRightRates() {
		return rightRates;
	}

	public void setRightRates(List<Integer> rightRates) {
		this.rightRates = rightRates;
	}

	public boolean isQuestionAnswering() {
		return isQuestionAnswering;
	}

	public void setQuestionAnswering(boolean questionAnswering) {
		isQuestionAnswering = questionAnswering;
	}

	public List<Long> getStuHkQuestionIds() {
		return stuHkQuestionIds;
	}

	public void setStuHkQuestionIds(List<Long> stuHkQuestionIds) {
		this.stuHkQuestionIds = stuHkQuestionIds;
	}

	public Long getNotationImageId() {
		return notationImageId;
	}

	public void setNotationImageId(Long notationImageId) {
		this.notationImageId = notationImageId;
	}

	public Map<Long, HomeworkAnswerResult> getAnswerMap() {
		if (StringUtils.isEmpty(this.answerResults)) {
			return Maps.newHashMap();
		}

		JSONObject jsonObject = JSON.parseObject(this.answerResults);
		Map<Long, HomeworkAnswerResult> resultMap = new HashMap<Long, HomeworkAnswerResult>(jsonObject.keySet().size());
		for (String key : jsonObject.keySet()) {
			resultMap.put(Long.valueOf(key), HomeworkAnswerResult.valueOf(jsonObject.getString(key)));
		}

		return resultMap;
	}

	public String getAnswerResults() {
		return answerResults;
	}

	public void setAnswerResults(String answerResults) {
		this.answerResults = answerResults;
	}

	public String getNotation() {
		return notation;
	}

	public void setNotation(String notation) {
		this.notation = notation;
	}

	public Long getAnswerImgId() {
		return answerImgId;
	}

	public void setAnswerImgId(Long answerImgId) {
		this.answerImgId = answerImgId;
	}
}
