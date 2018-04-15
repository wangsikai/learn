package com.lanking.uxb.service.correct.api;

import java.util.Collection;

import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.uxb.service.correct.vo.CorrectorType;
import com.lanking.uxb.service.correct.vo.QuestionCorrectObject;

/**
 * 悠数学批改流程控制入口.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
public interface CorrectProcessor {

	/**
	 * 学生自行/客户端强制提交作业后的批改流程处理（异步线程）.
	 * 
	 * @since 小悠快批 2018-2-11，注意必须在学生提交作业处理完成后调用，不能在同一个事务内。
	 * @param studentHomeworkId
	 *            学生作业ID
	 */
	void afterStudentCommitHomeworkAsync(long studentHomeworkId);

	/**
	 * 服务端强制提交学生作业后的批改流程.
	 * 
	 * @since 小悠快批 2018-2-11，注意必须在强制提交作业处理完成后调用，不能在同一个事务内。
	 * @param homeworkId
	 *            作业ID
	 */
	void afterForceCommitHomework(long homeworkId);

	/**
	 * 学生订正习题后的批改流程.
	 * 
	 * @param studentHomeworkQuestionId
	 *            学生作业习题
	 * @param done
	 *            是否真正做答，不会做没有做答为false
	 */
	void afterStudentCorrectQuestion(long studentHomeworkQuestionId, boolean done);

	// --------------------------[手动批改]--------------------------------

	/**
	 * 手动批改学生单条作业习题.
	 * 
	 * @param corrector
	 *            批改员
	 * @param correctorType
	 *            批改员类型
	 * @param questionCorrectObject
	 *            批改习题对象
	 */
	void correctStudentHomeworkQuestion(long corrector, CorrectorType correctorType,
			QuestionCorrectObject questionCorrectObject);

	/**
	 * 手动批改学生多条作业习题（注意此时必须是同一份学生作业的习题集合）.
	 * 
	 * @param corrector
	 *            批改员
	 * @param correctorType
	 *            批改员类型
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @param questionCorrectObject
	 *            批改习题对象
	 */
	void correctStudentHomeworkQuestions(long corrector, CorrectorType correctorType, long studentHomeworkId,
			Collection<QuestionCorrectObject> questionCorrectObjects);

	// ------------------------------[]----------------------------------------

	/**
	 * 处理未处理完成的学生作业.
	 * 
	 * @param studentHomeworks
	 *            学生作业集合
	 */
	void notCompleteStudentHomeworkHandle(Collection<StudentHomework> studentHomeworks);

}
