package com.lanking.uxb.service.diagnostic.api.teacher;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;

/**
 * 教学诊断 - 统计服务.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-29 数据跟随相关整理
 *
 */
public interface StaticDiagnosticService {

	/**
	 * 初始化班级诊断数据.
	 * 
	 * @param homeworkClazz
	 *            班级
	 */
	void initDiagnosticClass(HomeworkClazz homeworkClazz) throws ParseException;

	/**
	 * 根据作业进行教学诊断知识点相关统计.
	 *
	 * @param HomeworkClazz
	 *            需要统计的班级
	 * @param Homework
	 *            需要统计的作业
	 */
	void staticDiagnosticByHomework(HomeworkClazz clazz, Homework homework);

	/**
	 * 根据学生进行教学诊断知识点相关统计（学生在该班级有数据）.
	 *
	 * @param HomeworkClazz
	 *            需要统计的班级
	 * @param studentId
	 *            需要统计的学生
	 * @param isJoin
	 *            是否是加入班级
	 */
	void staticDiagnosticByStudent(HomeworkClazz clazz, long studentId, boolean isJoin);

	/**
	 * 根据学生进行教学诊断知识点相关统计（加入班级使用，学生在该班级无数据）.
	 *
	 * @param HomeworkClazz
	 *            需要统计的班级
	 * @param studentId
	 *            需要统计的学生
	 * @param isJoin
	 *            是否是加入班级
	 */
	void staticDiagnosticByStudentOfEmpty(HomeworkClazz clazz, long studentId);

	/**
	 * 处理班级增减量数据统计.
	 * 
	 * @param clazz
	 *            班级
	 * @param hkIds
	 *            该班级作业ID集合
	 * @param hkQuestion
	 *            习题相关数据
	 * @param incr
	 *            是否为增量
	 * @param allStatic
	 *            是否需要统计所有学生
	 */
	@SuppressWarnings("rawtypes")
	void staticDiagnosticClassIncrDatas(HomeworkClazz clazz, List<Long> hkIds, List<Map> hkQuestion, boolean incr,
			boolean allStatic);
}
