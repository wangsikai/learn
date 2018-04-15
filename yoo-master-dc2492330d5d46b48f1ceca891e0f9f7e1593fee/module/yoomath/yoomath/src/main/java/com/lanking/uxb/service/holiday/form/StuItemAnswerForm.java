package com.lanking.uxb.service.holiday.form;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question.Type;

/**
 * 专项题目保存的表单
 * 
 * @author wangsenhao
 * @since yoomath V1.9
 *
 */
public class StuItemAnswerForm implements Serializable {

	private static final long serialVersionUID = -1924932875329175694L;
	/**
	 * 当前做的题目ID
	 */
	private long questionId;
	/**
	 * 答案
	 */
	private String answer;
	/**
	 * 解题过程截图
	 */
	private List<Long> solvingImgs;
	/**
	 * 做题目花的时间
	 */
	private int homeworkTime;
	/**
	 * 是否需要返回最新学生题目的数据
	 */
	private Boolean updateData;
	/**
	 * 题目类型,多选题多个答案的话只入一条
	 */
	private Type type;

	private Map<Long, List<String>> answerData;

	private Map<Long, List<String>> answerAsciiData;

	private Long studentId;
	/**
	 * 假期学生作业专项ID
	 */
	private Long holidayStuHomeworkItemId;
	/**
	 * 学生完成率
	 */
	private Double completionRate;

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public List<Long> getSolvingImgs() {
		return solvingImgs;
	}

	public void setSolvingImgs(List<Long> solvingImgs) {
		this.solvingImgs = solvingImgs;
	}

	public int getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(int homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public Boolean getUpdateData() {
		return updateData;
	}

	public void setUpdateData(Boolean updateData) {
		this.updateData = updateData;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Map<Long, List<String>> getAnswerData() {
		return answerData;
	}

	public void setAnswerData(Map<Long, List<String>> answerData) {
		this.answerData = answerData;
	}

	public Map<Long, List<String>> getAnswerAsciiData() {
		return answerAsciiData;
	}

	public void setAnswerAsciiData(Map<Long, List<String>> answerAsciiData) {
		this.answerAsciiData = answerAsciiData;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getHolidayStuHomeworkItemId() {
		return holidayStuHomeworkItemId;
	}

	public void setHolidayStuHomeworkItemId(Long holidayStuHomeworkItemId) {
		this.holidayStuHomeworkItemId = holidayStuHomeworkItemId;
	}

	public Double getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(Double completionRate) {
		this.completionRate = completionRate;
	}

}
