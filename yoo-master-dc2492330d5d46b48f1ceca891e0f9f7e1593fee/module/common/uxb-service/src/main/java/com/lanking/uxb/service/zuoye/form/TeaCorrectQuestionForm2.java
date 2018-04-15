package com.lanking.uxb.service.zuoye.form;

import httl.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 教师批改form
 *
 * @author xinyu.zhou
 * @since yoomath V1.9.1
 */
public class TeaCorrectQuestionForm2 extends TeaCorrectQuestionForm {
	// 学生作业id
	private Long stuHkId;
	// 学生作业题目id
	private Long stuHkQuestionId;
	// 批改结果(解答题和填空题不传)
	private HomeworkAnswerResult result;
	// 题目的正确率(只解答题传)
	private Integer rightRate;
	// 批改后合成图片id
	private Long notationImageId;
	// 填空题 答案id -> 结果
	private String answerResults;
	// 批注
	private String notation;
	// 批注前原图片
	private Long answerImgId;
	// 多图--批改后合成图片id(一个图时不传值)
	private List<Long> notationImageIds;
	// 多图--批注(一个图时不传值)
	private List<String> notations;
	// 多图--批注前原图片
	private List<Long> answerImgIds;
	// 题目类型
	private Question.Type type;

	public Long getStuHkId() {
		return stuHkId;
	}

	public void setStuHkId(Long stuHkId) {
		this.stuHkId = stuHkId;
	}

	public Long getStuHkQuestionId() {
		return stuHkQuestionId;
	}

	public void setStuHkQuestionId(Long stuHkQuestionId) {
		this.stuHkQuestionId = stuHkQuestionId;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public Integer getRightRate() {
		return rightRate;
	}

	public void setRightRate(Integer rightRate) {
		this.rightRate = rightRate;
	}

	@Override
	public Long getNotationImageId() {
		return notationImageId;
	}

	@Override
	public void setNotationImageId(Long notationImageId) {
		this.notationImageId = notationImageId;
	}

	@Override
	public String getAnswerResults() {
		return answerResults;
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

	@Override
	public void setAnswerResults(String answerResults) {
		this.answerResults = answerResults;
	}

	@Override
	public String getNotation() {
		return notation;
	}

	@Override
	public void setNotation(String notation) {
		this.notation = notation;
	}

	public Long getAnswerImgId() {
		return answerImgId;
	}

	public void setAnswerImgId(Long answerImgId) {
		this.answerImgId = answerImgId;
	}

	public List<Long> getNotationImageIds() {
		return notationImageIds;
	}

	public void setNotationImageIds(List<Long> notationImageIds) {
		this.notationImageIds = notationImageIds;
	}

	public List<String> getNotations() {
		return notations;
	}

	public void setNotations(List<String> notations) {
		this.notations = notations;
	}

	public List<Long> getAnswerImgIds() {
		return answerImgIds;
	}

	public void setAnswerImgIds(List<Long> answerImgIds) {
		this.answerImgIds = answerImgIds;
	}

	public Question.Type getType() {
		return type;
	}

	public void setType(Question.Type type) {
		this.type = type;
	}

}
