package com.lanking.uxb.service.fallible.api;

import java.util.Collection;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionStuKnowpointStat;

/**
 * 学生做作业知识点统计
 * 
 * @author wangsenhao
 *
 */
public interface StaticDoQuestionStuKnowpointStatService {

	/**
	 * 近6个月知识点的统计
	 * 
	 * @param studentIds
	 */
	void stat(Collection<Long> studentIds, boolean isFirstDayInMonth);

	/**
	 * 清空学生知识点统计表
	 */
	void deleteKpStat();

	DoQuestionStuKnowpointStat get(long knowpointCode, long studentId);
}
