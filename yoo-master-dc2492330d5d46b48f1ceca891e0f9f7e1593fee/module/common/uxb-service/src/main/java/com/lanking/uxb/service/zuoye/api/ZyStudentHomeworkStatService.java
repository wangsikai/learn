package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;

/**
 * 悠作业:学生作业统计接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
public interface ZyStudentHomeworkStatService {

	/**
	 * 获取某一班级下所有学生的作业统计
	 * 
	 * @since 2.1
	 * @param clazzId
	 *            班级ID
	 * @return Map(key为学生ID)
	 */
	Map<Long, StudentHomeworkStat> findByClazzId(long clazzId);

	/**
	 * 查找一个学生作业某门课程的统计
	 * 
	 * @since 2.1
	 * @param studentId
	 *            学生ID
	 * @param homeworkClassId
	 *            作业班级ID
	 * @return 学生作业统计
	 */
	StudentHomeworkStat findOne(long studentId, Long homeworkClassId);

	/**
	 * 发布作业后更新统计
	 * 
	 * @since 2.1
	 * @param studentId
	 *            学生ID
	 * @param homeworkClassId
	 *            作业班级ID
	 */
	void updateAfterPublishHomework(long studentId, Long homeworkClassId);

	/**
	 * @since 2.1
	 * @param studentId
	 *            学生ID
	 * @param homeworkClassId
	 *            作业班级ID
	 */
	void updateAfterCommitHomework(long studentId, Long homeworkClassId);

	/**
	 * 根据作业班级IDs获取学生作业统计
	 * 
	 * @since yoomathV1.2
	 * @param studentId
	 *            学生ID
	 * @param homeworkClassId
	 *            作业班级ID
	 * @return StudentHomeworkStat
	 */
	StudentHomeworkStat getByHomeworkClassId(long studentId, long homeworkClassId);

	/**
	 * 根据作业班级IDs获取学生作业统计
	 * 
	 * @since yoomathV1.2
	 * @param studentId
	 *            学生ID
	 * @param homeworkClassIds
	 *            作业班级IDs
	 * @return List
	 */
	List<StudentHomeworkStat> getByHomeworkClassIds(long studentId, Collection<Long> homeworkClassIds);

	/**
	 * 根据作业班级ID和学生IDs获取学生作业统计
	 * 
	 * @since yoomath V1.2
	 * @param homeworkClassId
	 *            班级ID
	 * @param studentIds
	 *            学生IDs
	 * @return List
	 */
	List<StudentHomeworkStat> find(long homeworkClassId, Collection<Long> studentIds);

	/**
	 * 批量查询学生的提交率,取整
	 * 
	 * @param studentId
	 * @param classIds
	 * @return
	 */
	Map<Long, Integer> getSubmitRateByStuId(Long studentId, List<Long> classIds);

}
