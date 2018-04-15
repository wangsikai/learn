package com.lanking.uxb.zycon.activity.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 暑期作业活动-习题相关接口.
 * 
 * @since 教师端 v1.2.0
 *
 */
public interface ZycHolidayActivity01ExerciseQuestionService {

	/**
	 * 初始化查询教师错题.
	 * 
	 * @param pageable
	 *            分页数据.
	 * @return
	 */
	Page<TeacherFallibleQuestion> initFindFallibleQuestions(Pageable pageable);

	/**
	 * 保存错题.
	 * 
	 * @param activityCode
	 *            活动代码
	 * @param fallibleQuestionPage
	 *            错题分页数据.
	 */
	void saveTeacherFallibleQuestions(Long activityCode, Page<TeacherFallibleQuestion> fallibleQuestionPage);

	/**
	 * 保存某个教师的弱项知识点习题.
	 * 
	 * @param activityCode
	 *            活动CODE
	 * @param teacherId
	 *            教师ID
	 * @param l2KnowledgeCodes
	 *            小专题列表
	 * @param knowledgeSystemMap
	 *            专题数据列表
	 * @param knowledgeQuestions
	 *            知识点对应的习题集合
	 */
	void saveTeacherWeakpointQuestions(Long activityCode, long teacherId, List<Long> l2KnowledgeCodes,
			Map<Long, KnowledgeSystem> knowledgeSystemMap, Map<Long, List<Long>> knowledgeQuestions);
}
