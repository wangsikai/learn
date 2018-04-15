package com.lanking.uxb.service.zuoye.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractiseQuestion;

/**
 * 每日一练题目处理service
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
public interface ZyDailyPractiseQuestionService {
	/**
	 * 根据练习的id查找每日一练的题目
	 *
	 * @param pId
	 *            每日一练id
	 * @return {@link DailyPractiseQuestion}
	 */
	List<DailyPractiseQuestion> findByPractise(long pId);

	/**
	 * 得到用户已经练习过的每日一练题目
	 *
	 * @param userId
	 *            用户id
	 * @param sectionCode
	 *            章节码
	 * @return 题目id列表
	 */
	List<Long> findPulledQuestionIds(long userId, long sectionCode);

	/**
	 * 保存每日一练题目
	 *
	 * @param qIds
	 *            题目的id
	 * @param practiseId
	 *            练习id
	 */
	void save(List<Long> qIds, long practiseId);

	/**
	 * copy题目
	 *
	 * @param practiseId
	 *            原每日练习id
	 * @param newPractiseId
	 *            新的每日练习id
	 */
	void copy(long practiseId, long newPractiseId);

	/**
	 * 更新题目的结果状态
	 *
	 * @param updateResults
	 *            更新数据
	 * @param practiseId
	 *            练习id
	 * @return 得到成长值及金币奖励
	 */
	void updateResults(List<Map<String, Object>> updateResults, long practiseId, long userId);

	/**
	 * 统计学生做过的题目数量
	 *
	 * @param textbookCode
	 *            教材码
	 * @param studentId
	 *            学生id
	 * @return 数量
	 */
	long countStudentQuestion(int textbookCode, long studentId);

	/**
	 * 保存题目答案非提交状态
	 *
	 * @param answerList
	 *            答案列表
	 * @param questionIds
	 *            题目id列表
	 * @param practiseId
	 *            练习id
	 * @return 做的题目数量
	 */
	int draft(List<Map<Long, List<String>>> answerList, List<Long> questionIds, long practiseId);

	/**
	 * 保存一题数据 悠数学2.0 web端的每日练实时保存题目结果service
	 *
	 * @since 2.0.3
	 * @param answers
	 *            答案
	 * @param id
	 *            题目id {@link DailyPractiseQuestion}
	 */
	void doOne(Map<Long, List<String>> answers, long id);

	/**
	 * 查询已做题目数量
	 *
	 * @param practiseId
	 *            练习id
	 * @return {@link int}
	 */
	int countDone(long practiseId);
}
