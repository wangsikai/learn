package com.lanking.uxb.service.diagnostic.api;

import com.lanking.uxb.service.diagnostic.form.StaticClassQuestionDifficultyForm;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 当天定时统计相关数据
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface StaticDiagnosticClassDayService {
	/**
	 * 统计列表中的班级月做题量
	 *
	 * @param classIds
	 *            班级id列表
	 */
	void staticTopnStu(Collection<Long> classIds);

	/**
	 * 处理当前班级作业知识点难度等数据
	 *
	 * @param homeworkClassId
	 *            班级id
	 * @param difficultMap
	 *            题目对应的难度列表
	 */
	void doClassKpStat(Long homeworkClassId, Map<Long, List<StaticClassQuestionDifficultyForm>> difficultMap);

	/**
	 * 全国平均
	 */
	void doDiagnostic();

	void doClassStudentRankStat(Long classId, Date date) throws ParseException;

	/**
	 * 判断是否是初次运行
	 *
	 * @return true表示有数据 false 表示无数据
	 */
	boolean hasClassKpData();
}
