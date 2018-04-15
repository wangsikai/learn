package com.lanking.uxb.service.fallible.api;

import java.util.Collection;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 学生做题自然月统计
 * 
 * @author wangsenhao
 *
 */
public interface StaticDoQuestionStuNaturalMonthStatService {

	/**
	 * 近6个月的统计,例:现在2016.7,传2,统计的就是6月份
	 * 
	 * @param month
	 * @param studentIds
	 */
	void statByMonth(int month, Collection<Long> studentIds);

	/**
	 * 获取所有学生
	 * 
	 * @param cursorPageable
	 * @return
	 */
	CursorPage<Long, Long> getAllStudent(CursorPageable<Long> cursorPageable);

	/**
	 * 清空表
	 */
	void deleteMonthStat();

}
