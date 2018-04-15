package com.lanking.uxb.zycon.homeclazz.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

public interface ZycHkClazzService {

	/**
	 * 查询班级
	 * 
	 * @param cq
	 *            查询条件
	 * @param p
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> queryHkClazz(ClazzQuery cq, Pageable p);

	/**
	 * 查询单个班级的详情
	 * 
	 * @param clazzId
	 *            班级id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getHkClazz(Long clazzId);
	
	HomeworkClazz getHkClassByClassId(Long clazzId);

	/**
	 * 查询单个班级的详情
	 * 
	 * @param clazzId
	 *            班级id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getHkClazz(String clazzCode);

	/**
	 * 更新班级信息,只支持班级名称和班级状态修改
	 * 
	 * @param hkClazzId
	 *            作业班级id
	 * @param name
	 *            班级名称
	 * @param status
	 *            班级状态
	 * @param schoolYear
	 *            学生入学年份
	 */
	void updateHkClazz(Long hkClazzId, String name, Status status, Integer schoolYear);

	/**
	 * 班级成员
	 * 
	 * @param hkClazzId
	 *            作业班级id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> queryClazzMember(Long hkClazzId);

	/**
	 * 从班级里面移出学生
	 * 
	 * @param studentId
	 * @param clazzId
	 */
	void delStudent(Long studentId, Long clazzId);

	/**
	 * 批量获取作业统计
	 * 
	 * @param hkClazzIds
	 * @return
	 */
	Map<Long, HomeworkStat> mgetStat(Collection<Long> hkClazzIds);

	/**
	 * 查询学生
	 * 
	 * @param name
	 *            登录名或者用户名
	 * @param p
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> queryStudent(String name, Pageable p);

	/**
	 * 班级添加学生
	 * 
	 * @param studentId
	 *            学生id
	 * @param clazzId
	 *            班级id
	 */
	void addStu(Long studentId, Long clazzId);

	/**
	 * 老师班级名称是否存在
	 * 
	 * @param teacherId
	 *            老师id
	 * @param name
	 *            班级名称
	 * @return
	 */
	boolean classNameIsExist(Long teacherId, String name);

	/**
	 * 获取学生班级
	 * 
	 * @param studentId
	 * @param classId
	 * @return
	 */
	HomeworkStudentClazz getHK(Long studentId, Long classId);

	/**
	 * 获取excel的内容,发现不合格数据,直接失败
	 * 
	 * @param request
	 * @return
	 */
	Map<String, Object> getWb(HttpServletRequest request);

	/**
	 * 
	 * 保存数据
	 * 
	 * @param list
	 *            excel解析出来的内容
	 */
	void save(List<List<String>> list, Long clazzId);

}
