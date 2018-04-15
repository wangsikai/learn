package com.lanking.uxb.zycon.activity.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Exercise;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ExerciseType;

/**
 * 暑期作业活动-练习相关接口.
 * 
 * @since 教师端 v1.2.0
 *
 */
public interface ZycHolidayActivity01ExerciseService {

	/**
	 * 获取该老师已有的最后一个练习.
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param teacherId
	 *            老师ID
	 * @param type
	 *            练习类型
	 * @return
	 */
	HolidayActivity01Exercise getLastTeacherExercise(long activityCode, long teacherId,
			HolidayActivity01ExerciseType type);

	/**
	 * 获取当前教师弱项知识点的个数.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> listTeacherWeakpointCount();

	/**
	 * 获取当前教师弱项知识点集合（符合2017暑期作业活动需求的数据）.
	 * 
	 * @param teacherIds
	 *            教师ID集合，输入参数控制返回数据数量.
	 * @return Map<Long, Map<Long, List<Long>>>
	 *         key：教师ID，value：已过滤的教师弱项知识点集合（key:小专题代码）
	 */
	Map<Long, Map<Long, List<Long>>> listTeacherWeakpoint(Collection<Long> teacherIds);

	/**
	 * 处理所有知识点的题目数据进入缓存.
	 */
	void handleAllKnowpointQuestions();
}
