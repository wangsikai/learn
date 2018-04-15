package com.lanking.uxb.service.correct.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkCorrectStatus;

/**
 * 批改流程使用的学生作业接口.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
public interface CorrectStudentHomeworkService {

	/**
	 * 获取学生作业.
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @return
	 */
	StudentHomework getStudentHomework(long studentHomeworkId);

	/**
	 * 批量获取学生作业.
	 * 
	 * @param studentHomeworkIds
	 *            学生作业ID
	 * @return
	 */
	List<StudentHomework> mgetStudentHomework(Collection<Long> studentHomeworkIds);

	/**
	 * 获取学生作业集合.
	 * 
	 * @param homeworkId
	 *            作业ID.
	 * @return
	 */
	List<StudentHomework> listByHomework(long homeworkId);

	/**
	 * 设置学生作业批改状态.
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @param status
	 *            状态
	 */
	void setStudentHomeworkCorrectStatus(long studentHomeworkId, StudentHomeworkCorrectStatus status);

	/**
	 * 统计作业正确率.
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 */
	void staticStudentHomeworkRightRate(long studentHomeworkId);

	/**
	 * 判断当前作业的待批改状态
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 */
	void checkStudentHomeworkCorrectStatus(long studentHomeworkId);

	/**
	 * 设置作业订正后的正确率.
	 * 
	 * @param studentHomeworkId
	 *            学生作业ID
	 * @param rightRateCorrect
	 *            订正后的作业正确率
	 */
	void setStudentHomeworkRightRateCorrect(long studentHomeworkId, int rightRateCorrect);
}
