package com.lanking.uxb.service.stat.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;

/**
 * 错题答题历史记录
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月10日
 */
public interface StudentQuestionAnswerService {

	void create(long studentId, long questionId, Map<Long, List<String>> latexAnswers,
			Map<Long, List<String>> asciimathAnswers, List<Long> answerImgs, List<HomeworkAnswerResult> itemResults,
			Integer rightRate, HomeworkAnswerResult result, StudentQuestionAnswerSource source, Date createAt);

	void asynCreate(long studentId, List<Long> questionIds, List<Map<Long, List<String>>> latexAnswers,
			List<Map<Long, List<String>>> asciimathAnswers, List<List<Long>> answerImgs,
			List<List<HomeworkAnswerResult>> itemResults, List<Integer> rightRates, List<HomeworkAnswerResult> results,
			StudentQuestionAnswerSource source, Date createAt);

	void asynCreateExamActivity(long studentId, List<Long> questionIds, List<Map<Long, List<String>>> latexAnswers,
			List<Map<Long, List<String>>> asciimathAnswers, List<List<Long>> answerImgs,
			List<List<HomeworkAnswerResult>> itemResults, List<Integer> rightRates, List<HomeworkAnswerResult> results,
			StudentQuestionAnswerSource source, Date createAt);

	List<StudentQuestionAnswer> findByQuestionId(long studentId, long questionId, int limit);

	/**
	 * 获取某个学生不同题目分组后的前N条答题历史记录
	 * 
	 * @param studentId
	 *            学生ID
	 * @param limit
	 *            条数
	 * @since 错题集导出
	 * @return
	 */
	List<StudentQuestionAnswer> findByQuestionIdGroup(long studentId, int limit);

	/**
	 * 获取某个学生不同题目分组后的前N条答题历史记录
	 * 
	 * @param studentId
	 *            学生ID
	 * @param questionIds
	 *            指定习题集合
	 * @param limit
	 *            条数
	 * @since 错题集导出
	 * @return
	 */
	List<StudentQuestionAnswer> findByQuestionIdGroup(long studentId, Collection<Long> questionIds, int limit);

	/**
	 *
	 * @since 3.9.0
	 * @param studentId
	 *            学生id
	 * @param questionIds
	 *            题目id列表
	 * @return 做题情况统计数据列表
	 */
	List<Map> findStudentCondition(long studentId, Collection<Long> questionIds);

}
