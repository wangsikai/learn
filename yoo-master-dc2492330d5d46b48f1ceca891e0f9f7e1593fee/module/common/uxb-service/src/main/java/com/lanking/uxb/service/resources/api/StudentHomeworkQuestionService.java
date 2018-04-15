package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkConfirmStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;

/**
 * 提供学生作业习题相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月22日
 */
public interface StudentHomeworkQuestionService {

	/**
	 * 获取单个学生作业习题
	 * 
	 * @param id
	 *            学生作业习题ID
	 * @return 学生作业习题
	 */
	StudentHomeworkQuestion get(long id);

	/**
	 * 批量获取学生作业习题.
	 * 
	 * @param ids
	 *            学生作业习题ID集合
	 * @return
	 */
	List<StudentHomeworkQuestion> mgetList(Collection<Long> ids);

	/**
	 * 创建一个学生作业习题
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @param questionId
	 *            习题ID
	 * 
	 * @param sub
	 *            是否是子题
	 * @param correct
	 *            是否是订正题
	 * @param type
	 *            题目类型
	 * @return 学生作业习题
	 */
	StudentHomeworkQuestion create(long studentHomeworkId, long questionId, boolean sub, boolean correct, Type type);

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
	 * 根据学生作业ID获取作业习题列表
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @return 学生作业习题集合
	 */
	List<StudentHomeworkQuestion> find(long studentHomeworkId);

	/**
	 * 获取学生作业中需要订正的题目数量
	 * 
	 * @param id
	 *            学生作业ID
	 * @return 需要订正的题目数量
	 */
	Integer getNeedCorrectQuestionCount(long studentHomeworkId);

	/**
	 * 获取学生作业中总的订正题目数量
	 * 
	 * @param id
	 *            学生作业ID
	 * @return 总的订正题目数量
	 */
	Integer getCorrectQuestionCount(long studentHomeworkId);

	/**
	 * 获取学生作业中已订正题目数量
	 * 
	 * @param id
	 *            学生作业ID
	 * @return 已订正题目数量
	 */
	Integer getCorrectedQuestionCount(long studentHomeworkId);

	/**
	 * 更新学生作业订正题的订正状态
	 * 
	 * @param id
	 *            学生作业题目ID
	 * @return
	 */
	void updateReviseStatus(long id);

	/**
	 * 当前订正题目是否正在批改中
	 * 
	 * @param id
	 *            学生作业ID
	 * @return 订正题目是否批改
	 */
	boolean getCorrectAnswerCorrectStatus(long studentHomeworkId);

	/**
	 * 根据学生作业ID和习题IDs批量获取作业习题(包含订正题)
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @param questionIds
	 *            习题IDs
	 * @param newCorrect
	 *            是否订正题:0-false,1-true
	 * @return 作业习题集合
	 */
	List<StudentHomeworkQuestion> findByNewCorrect(List<Long> studentHomeworkIds, Collection<Long> questionIds,
			Boolean newCorrect);

	/**
	 * 查询需要老师批改的订正题
	 * 
	 * @param id
	 *            学生作业ID
	 * @return 订正题数量
	 */
	long getCorrectAnswerTeacherCorrectCount(long studentHomeworkId);

	/**
	 * 更新学生作业习题待确认状态.
	 * 
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 * @param confirmStatus
	 *            确认状态
	 */
	void setStudentHomeworkQuestionConfirmStatus(long studentHomeworkQuestionId, HomeworkConfirmStatus confirmStatus);
}
