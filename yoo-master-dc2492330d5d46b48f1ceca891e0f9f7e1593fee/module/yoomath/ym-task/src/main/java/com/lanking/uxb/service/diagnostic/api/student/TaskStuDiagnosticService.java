package com.lanking.uxb.service.diagnostic.api.student;

import java.util.Date;

public interface TaskStuDiagnosticService {
	/**
	 * 学生全局数据统计
	 * 
	 * @param startTime
	 * @param endTime
	 */
	void taskStatStuClassKp(Date startTime, Date endTime);

	/**
	 * topn统计
	 * 
	 * @param startTime
	 * @param endTime
	 */
	void taskStatTopnStu(Date startTime, Date endTime);

}
