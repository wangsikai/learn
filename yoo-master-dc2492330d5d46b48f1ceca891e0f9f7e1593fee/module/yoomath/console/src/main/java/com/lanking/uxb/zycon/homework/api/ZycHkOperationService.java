package com.lanking.uxb.zycon.homework.api;

import java.util.List;

/**
 * 作业便捷操作的相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年10月20日
 */
public interface ZycHkOperationService {

	/**
	 * 根据学生作业ID和序号获取题目ID
	 * 
	 * @since yoomath V1.5
	 * @param stuHkId
	 *            学生作业ID
	 * @param index
	 *            题目序号
	 * @return 题目ID
	 */
	Long queryQuestionId(long stuHkId, int index);

	/**
	 * 获取标准答案
	 * 
	 * @since yoomath V1.5
	 * @param stuHkId
	 *            学生作业ID
	 * @param index
	 *            作业序号
	 * @param qId
	 *            题目编号
	 * @return 答案集合
	 */
	List<String> findStandardAnswer(long stuHkId, int index, long qId);

	/**
	 * 获取学生答案
	 * 
	 * @since yoomath V1.5
	 * @param stuHkId
	 *            学生作业ID
	 * @param index
	 *            作业序号
	 * @param qId
	 *            题目编号
	 * @param newCorrect
	 *            是否订正题
	 * @return 答案集合
	 */
	List<String> findStudentAnswer(long stuHkId, int index, long qId, Boolean newCorrect);
}
