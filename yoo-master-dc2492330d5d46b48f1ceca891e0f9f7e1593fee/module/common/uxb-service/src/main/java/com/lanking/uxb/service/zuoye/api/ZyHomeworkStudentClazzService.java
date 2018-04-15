package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Order.Direction;

/**
 * 学生班级相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月23日
 */
public interface ZyHomeworkStudentClazzService {

	HomeworkStudentClazz join(long classId, long studentId);

	HomeworkStudentClazz find(long classId, long studentId);

	HomeworkStudentClazz findAll(long classId, long studentId);

	boolean isJoin(long classId, long studentId);

	HomeworkStudentClazz exit(long classId, long studentId, Long teacherId);

	void exit(long classId, Collection<Long> studentIds, Long teacherId);

	List<HomeworkStudentClazz> listCurrentClazzs(long studentId);

	List<HomeworkStudentClazz> listCurrentClazzsHasTeacher(long studentId);

	List<Long> listClassStudents(long classId);

	Page<HomeworkStudentClazz> query(long classId, Pageable pageable);

	Page<HomeworkStudentClazz> query(long classId, Pageable pageable, Direction joinorder);

	List<HomeworkStudentClazz> list(long classId);

	long countStudentClazz(long studentId, Status status);

	int mark(long teacherId, long classId, long studentId, String name);

	/**
	 * 根据学生的id查找HomeworkStudentClazz
	 *
	 * @param studentIds
	 *            学生的id List
	 * @return 数据
	 */
	List<HomeworkStudentClazz> findByStudentIds(Collection<Long> studentIds);

	/**
	 * 根据学生id列表以及班级id查找数据
	 *
	 * @param studentIds
	 *            学生id列表
	 * @param classId
	 *            班级id
	 * @return Map数据 id -> HomeworkStudentClazz
	 */
	Map<Long, HomeworkStudentClazz> findByStudentIdsAndClassId(Collection<Long> studentIds, long classId);

	/**
	 * 根据班级、组查询学生.
	 * 
	 * @param classId
	 *            班级ID
	 * @param groupId
	 *            组ID
	 * @param pageable
	 *            分页信息
	 * @return
	 */
	Page<HomeworkStudentClazz> query(long classId, long groupId, Pageable pageable);

	Page<HomeworkStudentClazz> query(long classId, long groupId, Pageable pageable, Direction joinorder);
}
