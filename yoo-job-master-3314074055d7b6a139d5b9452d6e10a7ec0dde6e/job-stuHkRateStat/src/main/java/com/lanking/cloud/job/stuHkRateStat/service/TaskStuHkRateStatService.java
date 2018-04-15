package com.lanking.cloud.job.stuHkRateStat.service;

public interface TaskStuHkRateStatService {

	/**
	 * 获取学生所有班的到当天为止最新的平均正确率
	 * 
	 * @param studentId
	 * @return
	 */
	Integer getAvgRate(Long studentId);

	void stat(Long studentId);

	void taskStuHkRateStat(int modVal);

}
