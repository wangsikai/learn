package com.lanking.uxb.service.homeworkRightRate.api;

import java.util.Date;

public interface TaskHomeworkRightRateStatService {

	/**
	 * 统计最近一周的，触发时间周一凌晨
	 * 
	 * @param classId
	 */
	void stat(long classId);

	/**
	 * 获取一周作业平均正确率集合
	 * 
	 * @param classId
	 * @param endTime
	 * @return
	 */
	Integer findWeekRateList(long classId, Date endTime);

}
