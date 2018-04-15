package com.lanking.uxb.service.diagnostic.api.teacher;

import java.util.List;

import com.lanking.cloud.domain.yoomath.homework.Homework;

/**
 * 班级-最近n次作业记录服务.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-30 数据跟随相关整理
 *
 */
public interface TaskDiagnosticClassLatestHomeworkService {

	/**
	 * 删除某个班级的最近N次作业.
	 * 
	 * @param classId
	 *            班级ID
	 */
	void deleteByClassId(long classId);

	/**
	 * 保存班级最近n次作业.
	 * 
	 * @param latestHks
	 *            最近n次作业
	 */
	void saveDiagnosticClassLatestHomeworks(List<Homework> latestHks);
}
