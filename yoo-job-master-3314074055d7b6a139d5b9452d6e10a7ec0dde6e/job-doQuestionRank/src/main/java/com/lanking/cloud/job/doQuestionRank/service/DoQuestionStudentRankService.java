package com.lanking.cloud.job.doQuestionRank.service;

import java.util.Date;

public interface DoQuestionStudentRankService {

	void statStudentDoQuestion(int startInt, int endInt, Date startDate, Date endDate);

	/**
	 * 统计完成后刷新班级榜数据
	 * 
	 * @param startInt
	 * @param endInt
	 */
	void refreshClassData(int startInt, int endInt);

	/**
	 * 统计校级榜排名
	 * 
	 * @param startInt
	 * @param endInt
	 */
	void statisSchoolRank(int startInt, int endInt);

	/**
	 * 统计完成后刷新校级榜数据
	 * 
	 * @param startInt
	 * @param endInt
	 */
	void refreshSchoolData(int startInt, int endInt);
}
