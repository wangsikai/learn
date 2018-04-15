package com.lanking.uxb.zycon.homework.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;

public interface ZycStudentHomeworkAnswerService {
	/**
	 * 根据学生作业习题ID获取作业答案集合
	 *
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 * @return 学生作业答案集合
	 */
	List<StudentHomeworkAnswer> find(long studentHomeworkQuestionId);

	/**
	 * 根据作业习题IDs批量获取作业答案集合
	 *
	 * @param studentHomeworkQuestionIds
	 *            学生作业习题IDs
	 * @return 学生作业答案集合
	 */
	Map<Long, List<StudentHomeworkAnswer>> find(Collection<Long> studentHomeworkQuestionIds);

	/**
	 * 未批改的题目数量
	 *
	 * @param keys
	 *            ids
	 * @return 数量
	 */
	Map<Long, Long> countNotCorrected(Collection<Long> keys);
}
