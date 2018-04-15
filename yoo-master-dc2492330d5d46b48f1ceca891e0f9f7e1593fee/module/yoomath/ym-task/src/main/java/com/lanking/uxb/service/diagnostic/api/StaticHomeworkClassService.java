package com.lanking.uxb.service.diagnostic.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 统计相关班级查询数据
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface StaticHomeworkClassService {

	/**
	 * 获取班级.
	 * 
	 * @param id
	 *            班级ID
	 * @return
	 */
	HomeworkClazz get(long id);

	/**
	 * 批量获取班级
	 * 
	 * @param ids
	 *            班级ID集合
	 * @return
	 */
	Map<Long, HomeworkClazz> mget(Collection<Long> ids);

	/**
	 * 分页查询当前有效班级(并且下发过作业)
	 *
	 * @param cursorPageable
	 *            分页条件
	 * @return 分页查询结果
	 */
	CursorPage<Long, HomeworkClazz> findEnableClass(CursorPageable<Long> cursorPageable);

	/**
	 * 根据班级id查找这个班级下所有学生
	 *
	 * @param classId
	 *            班级id
	 * @return 学生id列表
	 */
	List<Long> findStudentIds(long classId);

	/**
	 * 查询某个时间段下发过作业的班级
	 * 
	 * @param cursorPageable
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	CursorPage<Long, Long> curDayIssuedClass(CursorPageable<Long> cursorPageable, Date startTime, Date endTime);

	/**
	 * 根据一个班级找到下面所有学生所在的所有班级关系.
	 * 
	 * @param homeworkClassId
	 *            班级ID
	 * @return Map<Long, List<Long>> key 为班级ID，value 为该班级包含的学生ID集合
	 */
	Map<Long, List<Long>> findAllByOneClassStudent(long homeworkClassId);
}
