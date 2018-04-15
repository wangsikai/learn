package com.lanking.uxb.service.diagnostic.api.teacher;

import java.text.ParseException;
import java.util.Date;

/**
 * 班级-学生维度统计服务.
 * 
 * @author wlche
 *
 */
public interface TaskDiagnosticClassStudentService {

	/**
	 * 处理班级学生作业排名数据.
	 * 
	 * @param classId
	 *            班级ID
	 * @param date
	 *            统计开始日期
	 */
	void doClassStudentRankStat(Long classId, Date date) throws ParseException;
}
