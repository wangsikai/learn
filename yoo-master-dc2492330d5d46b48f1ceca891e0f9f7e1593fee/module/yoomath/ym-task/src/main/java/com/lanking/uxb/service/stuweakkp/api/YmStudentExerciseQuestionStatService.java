package com.lanking.uxb.service.stuweakkp.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.type.HomeworkAnswerResult;

/**
 * 处理学生练习题题目章节掌握情况，及新知识点掌握情况
 */
public interface YmStudentExerciseQuestionStatService {
	/**
	 * 1. 更新学生在某个章节下的掌握情况
	 * @param studentId
	 *            学生id
	 * @param questionId
	 *            题目id
	 * @param result
	 *            结果
	 * @param sqaId
	 *            学生答案id
	 */
	void updateExerciseQuestion(long studentId, long questionId, HomeworkAnswerResult result, long sqaId);

	/**
	 * 统计新知识点学生练习
	 *
	 * @param studentId
	 *            学生id
	 * @param questionId
	 *            题目id
	 * @param result
	 *            结果
	 * @param sqaId
	 *            学生答案id
	 */
	void updateExerciseQuestionKnowledge(long studentId, long questionId, HomeworkAnswerResult result, long sqaId);

	/**
	 * 批量处理学生章节练习相关数据
	 *
	 * @param studentIds
	 *            学生id列表
	 * @param questionIds
	 *            题目id列表
	 * @param results
	 *            结果列表
	 * @param sqaIds
	 *            StudentQuestionAnswer列表
	 */
	void updateExerciseQuestions(List<Long> studentIds, List<Long> questionIds,
	        List<HomeworkAnswerResult> results, List<Long> sqaIds, List<Date> createAt);

	/**
	 * 批量处理学生知识点练习情况数据
	 *
	 * @param studentIds
	 *            学生id列表
	 * @param questionIds
	 *            题目id列表
	 * @param results
	 *            结果列表
	 * @param sqaIds
	 *            StudentQuestionAnswer列表
	 */
	void updateExerciseQuestionKnowledges(List<Long> studentIds, List<Long> questionIds,
	        List<HomeworkAnswerResult> results, List<Long> sqaIds);

}
