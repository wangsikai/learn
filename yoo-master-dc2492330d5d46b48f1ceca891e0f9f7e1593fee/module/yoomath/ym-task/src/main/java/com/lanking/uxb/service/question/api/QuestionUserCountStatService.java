package com.lanking.uxb.service.question.api;

import java.util.Date;
import java.util.Map;

import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 老师题目布置次数，学生题目练习次数统计接口
 * 
 * @author wangsenhao
 *
 */
public interface QuestionUserCountStatService {

	/**
	 * 处理学生数据,计数
	 * 
	 * @param map
	 */
	@SuppressWarnings("rawtypes")
	void handleStu(Map map);

	/**
	 * 处理老师数据,计数
	 * 
	 * @param map
	 */
	@SuppressWarnings("rawtypes")
	void handleTea(Map map);

	/**
	 * 查询当前时间之前,学生做的普通作业列表
	 * 
	 * @param pageable
	 * @param nowTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> stuCommonHkList(CursorPageable<Long> pageable, Date nowTime);

	/**
	 * 查询当前时间之前,学生做的假期作业列表
	 * 
	 * @param pageable
	 * @param nowTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> stuHolidayHkList(CursorPageable<Long> pageable, Date nowTime);

	/**
	 * 查询当前时间之前,学生做的普通和假期作业之外的列表
	 * 
	 * @param pageable
	 * @param nowTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> stuOtherExerciseList(CursorPageable<Long> pageable, Date nowTime);

	/**
	 * 查询当前时间之前,老师布置的普通作业列表
	 * 
	 * @param pageable
	 * @param nowTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> teaCommonHkList(CursorPageable<Long> pageable, Date nowTime);

	/**
	 * 查询当前时间之前,老师布置的假期作业列表
	 * 
	 * @param pageable
	 * @param nowTime
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> teaHolidayHkList(CursorPageable<Long> pageable, Date nowTime);

	/**
	 * 游标查询学生练习列表
	 * 
	 * @param pageable
	 * @return
	 */
	CursorPage<Long, Map> stuExerciseListByStuQuestionAnswerId(CursorPageable<Long> pageable);

	/**
	 * 发送mq消息
	 * 
	 * @param map
	 */
	void stuExerciseMqSender(Map map);
}
