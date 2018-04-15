package com.lanking.uxb.service.resources.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.uxb.service.resources.ex.HomeworkException;

/**
 * 提供学生作业答案相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月22日
 */
public interface StudentHomeworkAnswerService {

	/**
	 * 获取一个学生作业答案
	 * 
	 * @param id
	 *            学生作业答案ID
	 * @return 学生作业答案
	 */
	StudentHomeworkAnswer get(long id);

	/**
	 * 创建学生作业答案
	 * 
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 * @param sequence
	 *            序号
	 * @return 学生作业答案
	 */
	StudentHomeworkAnswer create(long studentHomeworkQuestionId, int sequence);

	/**
	 * 保存学生作业答案.
	 * 
	 * @param answers
	 *            答案
	 * @throws HomeworkException
	 */
	void save(Collection<StudentHomeworkAnswer> answers) throws HomeworkException;

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
	 * 获取作业未批改的答案数量
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @return 未批改的作业答案数量
	 */
	long countNotCorrect(long homeworkId);

	/**
	 * 统计学生作业的未批改数量
	 * 
	 * @param studentHomeworkIds
	 *            学生作业ID
	 * @return 学生作业的未批改数量
	 */
	Map<Long, Long> countNotCorrected(Collection<Long> studentHomeworkIds);

}
