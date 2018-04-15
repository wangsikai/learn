package com.lanking.uxb.channelSales.base.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.channelSales.base.form.HomeworkClazzForm;

/**
 * 班级相关Service
 *
 * @author xinyu.zhou
 * @since 3.9.2
 */
public interface CsHomeworkClassService {
	/**
	 * 查询班级数据
	 *
	 * @param queryForm
	 *            {@link HomeworkClazzForm}
	 * @param pageable
	 *            {@link Pageable}
	 * @return 分页查询数据
	 */
	Page<HomeworkClazz> query(HomeworkClazzForm queryForm, Pageable pageable);

	/**
	 * 查询班级数据(班级名称，老师等信息)
	 * 
	 * @param classId
	 * @return
	 */
	Map queryClassInfo(long classId);

	/**
	 * 班级VIP个数
	 * 
	 * @param classId
	 * @return
	 */
	Long countVip(long classId);

	/**
	 * 学生数量
	 * 
	 * @param classId
	 * @return
	 */
	Long countStu(long classId);

	/**
	 * 查询班级学生数量
	 *
	 * @param classIds
	 *            班级id列表
	 * @return 统计数据
	 */
	List<Map> countStus(Collection<Long> classIds);

	/**
	 * 获取所有班级内的学生.
	 * 
	 * @param classId
	 *            班级ID
	 * @return
	 */
	List<Student> listAllStudents(long classId);
}
