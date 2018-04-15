package com.lanking.uxb.zycon.holiday.value;

import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.uxb.zycon.base.value.CQuestion;
import com.lanking.uxb.zycon.homework.value.VZycStudentHomeworkAnswer;

/**
 * 待批改的假期作业VO
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public class VZycHolidayStuHomeworkItemQuestion {
	private long id;
	private CQuestion question;
	private long holidayHomeworkItemId;
	private long holidayStuHomeworkId;
	private long holidayStuHomeworkItemId;
	private long studentId;
	private HomeworkAnswerResult result;

	/**
	 * 学生答案，管控台批改功能，请不要使用CQuestion中的studentHomeworkAnswers
	 * 
	 * @since 学生端v1.4.3 2017-05-31 修复假期作业批改时，不同学生同一道题答案一样的bug
	 */
	private List<VZycStudentHomeworkAnswer> studentHomeworkAnswers = Lists.newArrayList();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CQuestion getQuestion() {
		return question;
	}

	public void setQuestion(CQuestion question) {
		this.question = question;
	}

	public long getHolidayHomeworkItemId() {
		return holidayHomeworkItemId;
	}

	public void setHolidayHomeworkItemId(long holidayHomeworkItemId) {
		this.holidayHomeworkItemId = holidayHomeworkItemId;
	}

	public long getHolidayStuHomeworkId() {
		return holidayStuHomeworkId;
	}

	public void setHolidayStuHomeworkId(long holidayStuHomeworkId) {
		this.holidayStuHomeworkId = holidayStuHomeworkId;
	}

	public long getHolidayStuHomeworkItemId() {
		return holidayStuHomeworkItemId;
	}

	public void setHolidayStuHomeworkItemId(long holidayStuHomeworkItemId) {
		this.holidayStuHomeworkItemId = holidayStuHomeworkItemId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public HomeworkAnswerResult getResult() {
		return result;
	}

	public void setResult(HomeworkAnswerResult result) {
		this.result = result;
	}

	public List<VZycStudentHomeworkAnswer> getStudentHomeworkAnswers() {
		return studentHomeworkAnswers;
	}

	public void setStudentHomeworkAnswers(List<VZycStudentHomeworkAnswer> studentHomeworkAnswers) {
		this.studentHomeworkAnswers = studentHomeworkAnswers;
	}

}
