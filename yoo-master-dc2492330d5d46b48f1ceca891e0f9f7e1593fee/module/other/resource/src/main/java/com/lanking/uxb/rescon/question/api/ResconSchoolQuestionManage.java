package com.lanking.uxb.rescon.question.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;

/**
 * 学校题库中央资源库相关修改接口
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
public interface ResconSchoolQuestionManage {
	/**
	 * 添加设置学校题目
	 * 
	 * @param schoolId
	 *            学校ID
	 * @param questionId
	 *            题目Id
	 */
	void addSchoolQuestion(long schoolId, long questionId);

	/**
	 * 删除学校题目
	 * 
	 * @param schoolId
	 *            学校ID
	 * @param questionId
	 *            题目Id
	 */
	void delSchoolQuestion(long schoolId, long questionId);

	/**
	 * 批量添加设置学校题目
	 * 
	 * @param list
	 *            key 为 schoolId和questionId
	 */
	void batchAddSchoolQuestions(List<Map> list);

	/**
	 * 批量删除
	 * 
	 * @param list
	 *            key 为 schoolId和questionId
	 */
	void delSchoolQuestion(long schoolId, List<Long> questionIds);

	/**
	 * 获取学校题库
	 * 
	 * @param schoolId
	 * @param questionId
	 * @return
	 */
	SchoolQuestion getSquestion(long schoolId, long questionId);

	/**
	 * 获取教材集合
	 * 
	 * @param questionId
	 *            题目ID
	 * @return
	 */
	List<Integer> findByQuestionId(long questionId);

	/**
	 * 更新学校题库缓存
	 * 
	 * @param schoolId
	 * @param questionId
	 */
	void updateSchoolCache(Long schoolId, Long questionId);
}
