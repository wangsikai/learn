package com.lanking.uxb.service.web.form;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * web端每日练提交form
 *
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
public class PractiseCommitForm {

	// 题目数量
	private int count;
	// 题目ID集合
	private List<Long> qIds;
	// 题目答案
	private String answers;
	private List<Map<Long, List<String>>> answerList;
	// 智能试卷时，试卷ID,章节练习及每日练习的id
	private Long paperId;
	// 答题所花时间
	private Integer homeworkTime;
	// 每日练题目id
	private Long dailyQuestionId;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Long> getqIds() {
		return qIds;
	}

	public void setqIds(List<Long> qIds) {
		this.qIds = qIds;
	}

	public String getAnswers() {
		return answers;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	@SuppressWarnings("unchecked")
	public List<Map<Long, List<String>>> getAnswerList() {
		if (StringUtils.isBlank(getAnswers())) {
			return Collections.EMPTY_LIST;
		}
		answerList = JSON.parseObject(getAnswers(), List.class);
		return answerList;
	}

	public void setAnswerList(List<Map<Long, List<String>>> answerList) {
		this.answerList = answerList;
	}

	public Long getPaperId() {
		return paperId;
	}

	public void setPaperId(Long paperId) {
		this.paperId = paperId;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public Long getDailyQuestionId() {
		return dailyQuestionId;
	}

	public void setDailyQuestionId(Long dailyQuestionId) {
		this.dailyQuestionId = dailyQuestionId;
	}
}
