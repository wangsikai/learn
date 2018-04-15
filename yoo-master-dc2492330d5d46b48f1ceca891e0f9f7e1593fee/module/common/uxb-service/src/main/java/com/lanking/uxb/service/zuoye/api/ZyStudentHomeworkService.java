package com.lanking.uxb.service.zuoye.api;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 学生作业接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月20日
 */
public interface ZyStudentHomeworkService {

	/**
	 * 查询学生作业列表(页码方式)
	 * 
	 * @since 2.1
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	Page<StudentHomework> query(ZyStudentHomeworkQuery query, Pageable pageable);

	/**
	 * 查询学生作业列表(包含寒假作业)
	 * 
	 * @since yoomath V1.9
	 * @param query
	 * 
	 * @param pageable
	 *            分页条件
	 * @return 作业分页数据
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> queryUnionHolidayStuHk(ZyStudentHomeworkQuery query, Pageable pageable);

	/**
	 * 悠数学2.0版本首页查询普通学生作业及假期作业中的学生待办任务作业<br/>
	 * 1. 未下发作业中的学生作业<br/>
	 * 2. 已经下发但是学生还未查看过的作业
	 *
	 * @since 2.0.3
	 * @param query
	 *            {@link ZyStudentHomeworkQuery}
	 * @return {@link List}
	 */
	List<Map> queryUnionHolidayStuHkNew(ZyStudentHomeworkQuery query);

	/**
	 * @since yoomath mobile V1.1.0
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 作业分页数据
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryUnionHolidayStuHk(ZyStudentHomeworkQuery query, CursorPageable<Long> pageable);

	/**
	 * 查询作业，不包括假期作业
	 * 
	 * @since yoomath mobile V1.1.0
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 作业分页数据
	 */
	@SuppressWarnings("rawtypes")
	CursorPage<Long, Map> queryStuHk(ZyStudentHomeworkQuery query, CursorPageable<Long> pageable);

	/**
	 * 查询学生作业列表(游标方式)
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param query
	 *            查询条件
	 * @param pageable
	 *            分页条件
	 * @return 分页数据
	 */
	CursorPage<Long, StudentHomework> query(ZyStudentHomeworkQuery query, CursorPageable<Long> pageable);

	/**
	 * 更新学生作业计时
	 * 
	 * @since 2.1
	 * @param id
	 *            ID
	 * @param studentId
	 *            学生ID
	 * @param homeworkTime
	 *            作业时间
	 * @return 更新时间
	 */
	long updateHomeworkTime(long id, long studentId, int homeworkTime, Double completionRate);

	/**
	 * 下发作业
	 * 
	 * @since 2.1
	 * @param homeworkId
	 *            作业ID
	 * @param studentId
	 *            学生ID
	 */
	long commitHomework(long homeworkId, long studentId);

	/**
	 * 提交作业
	 * 
	 * @since 2.1
	 * @param homeworkId
	 *            作业ID
	 */
	int commitHomework(long homeworkId);

	/**
	 * 获取最近limit次作业的统计(个人数据和全班数据)
	 * 
	 * @since yoomath V1.2
	 * @param classId
	 *            作业班级ID
	 * @param studentId
	 *            学生ID
	 * @param limit
	 *            获取数量
	 * @return List
	 */
	@SuppressWarnings("rawtypes")
	List<Map> statisticLatestHomework(long classId, long studentId, int limit);

	long countHomeworks(long studentId, Set<StudentHomeworkStatus> status);

	/**
	 * 获取学生指定状态的各种作业的数量
	 * 
	 * @since yoomath mobile V1.1.0
	 * @param studentId
	 *            学生ID
	 * @param status
	 *            学生作业状态
	 * @return 数量
	 */
	long countAllHomeworks(long studentId, Set<StudentHomeworkStatus> status);

	/**
	 * 获取学生指定状态的各种作业的数量,不包括假期作业
	 * 
	 * @param studentId
	 *            学生ID
	 * @param status
	 *            学生作业状态
	 * @return 数量
	 */
	long countAllHomeworksNoHoliday(long studentId, Set<StudentHomeworkStatus> status);

	/**
	 * 查询一份作业未提交的学生列表
	 * 
	 * @since yoomath V1.8
	 * @param homeworkId
	 *            作业ID
	 * @return 学生ID集合
	 */
	List<Long> listNotCommitStudent(long homeworkId);

	/**
	 * 获取单份作业TOP limit 个学生
	 * 
	 * @param homeworkId
	 *            作业id
	 * @param limit
	 *            limit
	 * @return
	 */
	List<StudentHomework> listTop5ByHomework(long homeworkId, Integer limit);

	/**
	 * 获取作业当前排名的所有学生
	 * 
	 * @since 2.0.3
	 * @param homeworkId
	 * @param rank
	 * @return
	 */
	List<StudentHomework> findByRank(long homeworkId, Integer rank);

	/**
	 * 获取班级最近两份下发的作业 进步最快学生
	 * 
	 * @param classId
	 *            班级ID
	 * @param limit
	 *            进步最快学生排名个数
	 * @return
	 */
	List<Long> getMostImprovedStu(Long classId, int limit);

	/**
	 * 查询班级最近两份下发的作业 进步最快学生<br>
	 * 进步名次对应的map和当前名次对应的map
	 * 
	 * @param classId
	 * @param limit
	 * @return
	 */
	Map<String, Map<Long, Integer>> getMostImprovedMap(Long classId, int limit);

	/**
	 * 得到未批改学生数量
	 *
	 * @param hkId
	 *            作业id
	 * @return 数量
	 */
	long countNotCorrect(long hkId);

	/**
	 * 获取班级最近两份下发的作业 退步最快学生
	 * 
	 * @since 2.0.3
	 * 
	 * @param classId
	 *            班级ID
	 * @param limit
	 *            进步最快学生排名个数
	 * @return
	 */
	List<Long> getMostBackwardStu(Long classId, int limit);

	/**
	 * 获取班级退步最快学生，当前排名和退步名次
	 * 
	 * @param classId
	 * @param limit
	 * @return
	 */
	Map<String, Map<Long, Integer>> getMostBackwardMap(Long classId, int limit);

	/**
	 * 查询没有提交的学生id集合<br>
	 * 1.stu_submit_at 为null<br>
	 * 2.status 为SUBMIT 或 ISSUED
	 * 
	 * @param homeworkId
	 * @return
	 */
	List<Long> findNotSubmitStus(Long homeworkId);

	/**
	 * 更新学生已经下发的查看状态
	 *
	 * @param id
	 *            普通学生作业id列表
	 */
	void updateViewStatus(long id);

	/**
	 * 分页查询作业列表(新作业查询)
	 * 
	 * @since 2.0.3 (web v2.0)
	 * @param query
	 *            查询条件{@link ZyHomeworkQuery}
	 * @param pageable
	 *            分页条件
	 * @return 分页数据 {@link Page}
	 */
	Page<StudentHomework> queryHomeworkWeb(ZyStudentHomeworkQuery query, Pageable pageable);

	/**
	 * 获取学生最早的作业、假期作业创建时间
	 * 
	 * @since 2.0.3 (web v2.0)
	 * @param studentId
	 *            学生ID
	 * @return
	 */
	Date getFirstStartAt(Long studentId);

	/**
	 * 查询当前学生有没有待处理的作业(普通和假期作业)
	 * 
	 * @param studentId
	 * @return
	 */
	Integer countToDoHk(Long studentId);

	/**
	 * 查询未走完流程的学生作业列表(游标方式)
	 * 
	 * @param pageable
	 * @return
	 */
	CursorPage<Long, StudentHomework> queryNotCompleteStudentHomework(CursorPageable<Long> pageable);

}
