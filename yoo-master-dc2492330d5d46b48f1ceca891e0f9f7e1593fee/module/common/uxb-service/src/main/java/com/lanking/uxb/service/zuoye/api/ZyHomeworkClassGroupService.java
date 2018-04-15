package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroup;
import com.lanking.uxb.service.zuoye.form.HomeworkClazzGroupForm;

/**
 * 班级组管理
 *
 * @author xinyu.zhou
 * @since 3.9.3
 */
public interface ZyHomeworkClassGroupService {
	/**
	 * 创建班级组
	 *
	 * @param form
	 *            {@link HomeworkClazzGroupForm}
	 * @return 添加不成功的学生id列表
	 */
	List<Long> create(HomeworkClazzGroupForm form);

	/**
	 * 创建空的班级分组.
	 * 
	 * @param clazzId
	 *            班级
	 * @param name
	 *            分组名称
	 * @return
	 */
	HomeworkClazzGroup create(long clazzId, String name);

	/**
	 * 此班级下是否已经存在相同的小组
	 *
	 * @param name
	 *            小组名
	 * @param classId
	 *            班级id
	 * @return 存在true 不存在false
	 */
	boolean isExist(String name, long classId);

	/**
	 * 统计班级下面组的数量
	 *
	 * @param classId
	 *            班级id
	 * @return 班级下，小组数量
	 */
	Long countClassGroupNum(long classId);

	/**
	 * 删除分组
	 *
	 * @param id
	 *            班级组id
	 */
	void removeGroup(long id);

	/**
	 * 获取班级所有分组.
	 * 
	 * @param clazzId
	 *            班级ID
	 * @return
	 */
	List<HomeworkClazzGroup> groups(long clazzId);

	/**
	 * 批量获取班级的的分组
	 * 
	 * @param classIds
	 * @return
	 */
	Map<Long, List<HomeworkClazzGroup>> groupMaps(Collection<Long> classIds);

	/**
	 * 获取分组.
	 * 
	 * @param id
	 *            分组ID
	 * @return
	 */
	HomeworkClazzGroup get(long id);

	/**
	 * 批量获取分组
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, HomeworkClazzGroup> mget(Collection<Long> ids);

	/**
	 * 更新分组名称.
	 * 
	 * @param id
	 *            分组ID
	 * @param name
	 *            名称
	 */
	void updateGroupName(long id, String name);

	/**
	 * 移除分组内的学生
	 *
	 * @param id
	 *            分组id
	 * @param studentIds
	 *            学生id列表
	 */
	void removeStudents(long id, Collection<Long> studentIds);

	/**
	 * 根据班级id移除学生所在分组
	 *
	 * @param classId
	 *            班级id
	 * @param studentIds
	 *            学生id列表
	 */
	void removeStudentsByClass(long classId, Collection<Long> studentIds);

	/**
	 * 添加学生至某个分组
	 *
	 * @param id
	 *            小组id
	 * @param studentIds
	 *            学生列表
	 */
	void addStudents(long id, Collection<Long> studentIds);

	/**
	 * 更新分组学生增量.
	 * 
	 * @param id
	 *            分组ID
	 * @param addCount
	 *            增量
	 */
	void addStudentCount(long id, int addCount);

	/**
	 * 更新分组学生数量.
	 * 
	 * @param id
	 *            分组ID
	 * @param StudentCount
	 *            学生数量
	 */
	void updateStudentCount(long id, int StudentCount);
}
