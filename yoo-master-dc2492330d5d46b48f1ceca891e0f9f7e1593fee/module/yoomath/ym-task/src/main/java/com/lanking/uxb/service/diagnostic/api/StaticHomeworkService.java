package com.lanking.uxb.service.diagnostic.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * Diagnostic统计中的作业查询服务
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface StaticHomeworkService {
	/**
	 * 根据id得到作业数据
	 *
	 * @param id
	 *            作业id
	 * @return {@link Homework}
	 */
	Homework get(long id);

	/**
	 * 分页查找当天已经下发的作业
	 *
	 * @param pageable
	 *            分页条件
	 * @return {@link Page}
	 */
	Page<Homework> findTodayIssuedHk(Pageable pageable);

	/**
	 * 查找一份作业中对应题目练习情况（统计近n次作业薄弱知识点使用，其他勿用）
	 *
	 * @param id
	 *            作业id
	 * @return {@link List}
	 */
	@SuppressWarnings("rawtypes")
	List<Map> findHomeworkQuestion(long id);

	/**
	 * 根据多个作业id查找数据（统计近n次作业薄弱知识点使用，其他勿用）
	 * 
	 * @since 教师端 v1.3.0 2017-7-5 有修改，直接通过student_homework_question表取数据
	 * @param ids
	 *            作业id列表
	 * @param studentIds
	 *            限定学生列表
	 * @return {@link List}
	 */
	@SuppressWarnings("rawtypes")
	List<Map> findHomeworkQuestion(Collection<Long> ids, Collection<Long> studentIds);

	/**
	 * 根据学生班级查找数据，数据可能会比较多，采用分页形式（统计近n次作业薄弱知识点使用，其他勿用）
	 * 
	 * @since 教师端 v1.3.0 2017-7-5 有修改，直接通过student_homework_question表取数据
	 * @param classId
	 *            班级ID
	 * @param studentId
	 *            学生ID
	 * @return {@link List}
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> findHomeworkQuestionByStudent(Long classId, long studentId, Pageable pageable);

	/**
	 * 获得班级最近30次作业
	 *
	 * @param classId
	 *            班级id
	 * @return 最近30次作业列表
	 */
	List<Homework> getLatestHks(long classId);

	/**
	 * 查找一同布置的班级
	 *
	 * @param exerciseId
	 *            练习id
	 * @return {@link Homework}
	 */
	List<Homework> getByExerciseId(long exerciseId);

	Map<Long, List<Homework>> mgetByExerciseIds(Collection<Long> exerciseId);

	/**
	 * 根据班级查找所有已下发的作业
	 *
	 * @param classId
	 *            班级id
	 * @return 班级已下发作业列表
	 */
	List<Homework> findAllByClassId(long classId);

	/**
	 * 判断原班级内是否有已下发的该学生的作业（教学诊断统计判断使用，若有则学生重新加入班级时需要重新统计）
	 * 
	 * @param classId
	 *            班级ID
	 * @param studentId
	 *            学生ID
	 * @param curDate
	 *            截止时间，为空表示左右
	 * @return
	 */
	boolean hasStudentHomework(long classId, long studentId, Date curDate);
}
