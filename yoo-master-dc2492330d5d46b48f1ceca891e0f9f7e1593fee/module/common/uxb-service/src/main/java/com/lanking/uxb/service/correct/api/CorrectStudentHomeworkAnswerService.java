package com.lanking.uxb.service.correct.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.uxb.service.correct.vo.AnswerCorrectResult;

/**
 * 批改流程使用的学生作业答案接口.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
public interface CorrectStudentHomeworkAnswerService {

	/**
	 * 批量保存习题答案批改结果.
	 * 
	 * @param answerResults
	 *            批改结果集合
	 */
	void saveCorrectResults(List<AnswerCorrectResult> answerResults);

	/**
	 * 根据作业习题IDs批量获取作业答案集合
	 * 
	 * @param studentHomeworkQuestionIds
	 *            学生作业习题IDs
	 * @return 学生作业答案集合
	 */
	Map<Long, List<StudentHomeworkAnswer>> find(Collection<Long> studentHomeworkQuestionIds);
}
