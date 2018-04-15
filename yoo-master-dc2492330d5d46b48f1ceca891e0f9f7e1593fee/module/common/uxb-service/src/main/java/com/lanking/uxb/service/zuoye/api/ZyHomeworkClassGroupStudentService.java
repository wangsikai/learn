package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroupStudent;

/**
 * @author xinyu.zhou
 * @since 3.9.3
 */
public interface ZyHomeworkClassGroupStudentService {

	/**
	 * 根据学生id列表,班级id
	 *
	 * @param studentIds
	 *            学生id列表
	 * @param classId
	 *            班级id
	 * @return 班级组学生数据
	 */
	List<HomeworkClazzGroupStudent> findByStusAndClass(Collection<Long> studentIds, long classId);

	/**
	 * 根据班级id以及学生id列表，移除学生所在分组
	 *
	 * @param studentIds
	 *            学生id列表
	 * @param classId
	 *            班级id
	 * @return 影响行数
	 */
	int removeStudents(Collection<Long> studentIds, long classId);

	/**
	 * 添加学生至组
	 *
	 * @param studentIds
	 *            学生id
	 * @param groupId
	 *            组别id
	 * @param classId
	 *            班级id
	 */
	void addStudents(Collection<Long> studentIds, long groupId, long classId);

	/**
	 * 删除分组内的学生
	 *
	 * @param groupId
	 *            班级组id
	 */
	void removeStudents(long groupId);

	/**
	 * 获取所有的学生班级分组信息.
	 * 
	 * @param clazzId
	 *            班级ID
	 * @return
	 */
	List<HomeworkClazzGroupStudent> findAll(long clazzId);

	/**
	 * 获取组成员列表
	 * 
	 * @param classId
	 * @param groupId
	 * @return
	 */
	List<Long> findGroupStudents(Long classId, Long groupId);

	/**
	 * 更新单个学生分组.
	 * 
	 * @param clazzId
	 *            班级ID
	 * @param studentId
	 *            学生ID
	 * @param newGroupId
	 *            新的分组ID
	 * @param oldGroupId
	 *            旧的分组ID
	 */
	void changeGroup(long clazzId, long studentId, long newGroupId);

	/**
	 * 删除分组内学生
	 *
	 * @param studentIds
	 *            学生id列表
	 * @param groupId
	 *            分组id
	 * @return 影响条数
	 */
	int removeGroupStudents(Collection<Long> studentIds, long groupId);
}
