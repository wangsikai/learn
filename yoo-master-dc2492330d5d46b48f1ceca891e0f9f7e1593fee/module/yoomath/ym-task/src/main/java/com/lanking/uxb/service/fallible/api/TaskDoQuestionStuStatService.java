package com.lanking.uxb.service.fallible.api;

public interface TaskDoQuestionStuStatService {

	/**
	 * 统计学生最近6个月的做题和错题情况
	 */
	void statStuDoQuestion();

	/**
	 * 学生做题知识点统计
	 */
	void statStuDoQuestionKp();

}
