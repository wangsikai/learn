package com.lanking.uxb.service.resources.api;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.uxb.service.resources.ex.HomeworkException;

/**
 * 提供学生作业相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public interface StudentHomeworkService {

	/**
	 * 获取一个学生作业
	 * 
	 * @param id
	 *            学生作业Id
	 * @return 学生作业
	 */
	StudentHomework get(long id);

	/**
	 * 批量获取学生作业列表
	 * 
	 * @param ids
	 * @return
	 */
	List<StudentHomework> mget(Collection<Long> ids);

	Map<Long, StudentHomework> mgetMap(Collection<Long> ids);

	/**
	 * 根据作业ID及学生ID获取一个学生作业.
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param studentId
	 *            学生ID
	 * @return
	 */
	StudentHomework getByHomeworkAndStudentId(long homeworkId, long studentId);

	/**
	 * 获取一次作业的所有学生作业
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @return
	 */
	List<StudentHomework> listByHomework(long homeworkId);

	/**
	 * 获取一次作业的所有学生作业,按学生加入时间排序<br>
	 * 2017.5.4 senhao.wang
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param classId
	 *            班级ID
	 * @return
	 */
	List<StudentHomework> listByHomeworkOrderByJoinAt(long homeworkId, long classId);

	/**
	 * 根据作业ID集合及学生ID批量获取一个学生的相关作业集合.
	 * 
	 * @param homeworkIds
	 *            作业ID集合
	 * @param studentId
	 *            学生ID
	 * @return Map<Long, StudentHomework> key为作业ID
	 */
	Map<Long, StudentHomework> mgetByHomeworksAndStudentId(Collection<Long> homeworkIds, long studentId);

	/**
	 * 提交某个学生的作业
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param studentId
	 *            学生ID
	 * @throws HomeworkException
	 */
	Homework commitHomework(long homeworkId, long studentId) throws HomeworkException;

	/**
	 * 发布作业
	 * 
	 * @param homework
	 *            作业
	 * @param studentIds
	 *            学生IDs
	 * @param createAt
	 *            创建时间
	 * @throws HomeworkException
	 */
	void publishHomework(Homework homework, Set<Long> studentIds, Date createAt) throws HomeworkException;

	/**
	 * 发布作业带删除状态
	 * 
	 * @since 小优快批，2018-2-27，订正题不再通过布置作业时处理
	 * 
	 * @param homework
	 *            作业
	 * @param studentIds
	 *            学生IDs
	 * @param createAt
	 *            创建时间
	 * @param delStatus
	 *            删除状态
	 * @throws HomeworkException
	 */
	void publishHomeworkWithDelStatus(Homework homework, Set<Long> studentIds, Date createAt, Status delStatus)
			throws HomeworkException;

	/**
	 * 下发作业
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @throws HomeworkException
	 */
	void issue(long homeworkId) throws HomeworkException;

	/**
	 * 根据课程查询学生的作业
	 * 
	 * @param courseId
	 *            课程ID
	 * @param studentId
	 *            学生ID
	 * @param status
	 *            作业状态
	 * @param keyword
	 *            作业关键字
	 * @param cpr
	 *            分页条件
	 * @return 分页数据
	 */
	CursorPage<Long, StudentHomework> query(long courseId, long studentId, StudentHomeworkStatus status, String keyword,
			CursorPageable<Long> cpr);

	CursorPage<Long, StudentHomework> query(long studentId, Integer commitCount, StudentHomeworkStatus status,
			CursorPageable<Long> cpr);

	/**
	 * 获取一次作业的所有学生作业
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param rightRate
	 *            正确率区间
	 * @param leftRate
	 *            正确率区间
	 * @param timeLimit
	 *            限时分钟
	 * @param hStatus
	 *            作业状态
	 * @param orderby
	 *            排序方式
	 * @param name
	 *            学生名
	 * @since 教师端v1.3.0
	 * @return
	 */
	List<StudentHomework> listByHomework(long homeworkId, BigDecimal rightRate, BigDecimal leftRate, Integer timeLimit,
			StudentHomeworkStatus status, String orderby, String name);

	/**
	 * 获取已经提交的学生作业个数（有效提交）.
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @return
	 */
	long countCommit(long homeworkId);

	/**
	 * 查询出已经提交了的学生作业集合
	 * 
	 * @param homeworkId
	 * @param classId
	 * @return
	 */
	List<StudentHomework> findSubmitedStuHomeworks(long homeworkId, long classId);

}
