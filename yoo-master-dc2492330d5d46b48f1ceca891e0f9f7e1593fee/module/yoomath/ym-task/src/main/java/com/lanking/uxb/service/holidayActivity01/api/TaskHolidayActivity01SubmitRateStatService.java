package com.lanking.uxb.service.holidayActivity01.api;

/**
 * 假期作业提交率统计
 * 
 * @author wangsenhao
 *
 */
public interface TaskHolidayActivity01SubmitRateStatService {

	/**
	 * 更新作业的提交率
	 * 
	 */
	void updateHkSubmitRate();

	/**
	 * 更新班级的提交率
	 * 
	 * @param h
	 */
	void updateClassSubmitRate();

	/**
	 * 更新学生的提交率
	 * 
	 * @param h
	 */
	void updateClassUserSubmitRate();

}
