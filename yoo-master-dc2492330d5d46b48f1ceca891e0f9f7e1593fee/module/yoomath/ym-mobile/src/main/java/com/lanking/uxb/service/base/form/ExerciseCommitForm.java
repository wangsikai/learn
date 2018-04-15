package com.lanking.uxb.service.base.form;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 客户端作业练习提交数据表单(章节练习、每日练、智能试卷)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月30日
 */
public class ExerciseCommitForm {

	// 1:章节练习2:每日练3:智能试卷4:学生新知识点加强练习
	private int type;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

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

}
