package com.lanking.uxb.zycon.homework.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
public interface ZycStudentHomeworkQuestionService {
	/**
	 * 根据id查找
	 *
	 * @param id
	 *            Id
	 * @return 数据
	 */
	StudentHomeworkQuestion get(Long id);

	/**
	 * 批量获取.
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, StudentHomeworkQuestion> mget(Collection<Long> ids);

	/**
	 * 回收已经推送的题目
	 *
	 * @param homeworkId
	 *            作业相id
	 * @param ids
	 *            回收的题目id
	 */
	void removePushedId(Long homeworkId, List<Long> ids);

	/**
	 * 根据学生作业ID和习题ID获取作业习题
	 *
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @param questionId
	 *            习题ID
	 * @return 作业习题
	 */
	StudentHomeworkQuestion find(long studentHomeworkId, long questionId);

	/**
	 * 根据学生作业ID和习题IDs批量获取作业习题
	 *
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @param questionIds
	 *            习题IDs
	 * @return 作业习题集合
	 */
	List<StudentHomeworkQuestion> find(long studentHomeworkId, Collection<Long> questionIds);

	/**
	 * 得到当前学生作业有未批改的数量
	 *
	 * @param studentHomeworkId
	 *            学生的作业id
	 * @return 数量
	 */
	Long countNotCorrected(long studentHomeworkId);

	/**
	 * 根据学生作业id获取习题作业列表
	 *
	 * @param studentHomeworkId
	 *            学生作业id
	 * @return 习题列表
	 */
	List<StudentHomeworkQuestion> find(long studentHomeworkId);

	List<Long> getCorrectQuestions(long stuHomeworkId);

	/**
	 * 根据学生作业查找题目的Map对象
	 *
	 * @param stuHkId
	 *            学生作业id
	 * @return {@link Map}
	 */
	Map<Long, Question> findByStuHk(long stuHkId);
	
	/**
	 * 查询学生习题
	 * 
	 * @since 小优快批
	 * @param studentHomeworkId
	 * @param questionId
	 * @param newCorrect
	 *            是否只查询订正题
	 * @return
	 */
	List<StudentHomeworkQuestion> queryStuQuestions(long studentHomeworkId, long questionId,
			Collection<Long> questionIds, boolean newCorrect);
	
	/**
	 * 修改学生作业题目的确认状态
	 *
	 * @param stuHkQId
	 *            学生的作业题目id
	 * @param status
	 *            确认状态
	 * @return 
	 */
	void updateConfirmStatus(long stuHkQId,HomeworkConfirmStatus status);
	
	/**
	 * 确认批改结果
	 *
	 * @param ids
	 *            确认批改的学生题目id列表
	 */
	void confirm(List<Long> ids);
}
