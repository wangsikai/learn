package com.lanking.uxb.service.zuoye.api;

import java.util.List;
import java.util.Map;

/**
 * 学生知识点相关统计接口
 * 
 * @since yoomath V1.4
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月21日
 */
public interface ZyHkStuClazzKnowpointStatService {
	/**
	 * 获取学生知识点统计
	 * 
	 * @since yoomath V1.4
	 * @param studentId
	 *            学生ID
	 * @param classId
	 *            班级ID
	 * @return List.Map
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getStuKnowpointStat(Long studentId, Long classId);

	/**
	 * 获取某个学生某个班级正确率低的五个知识点
	 * 
	 * @since yoomath V1.4.1
	 * @param studentId
	 *            学生ID
	 * @param classId
	 *            班级ID
	 * @return List.Map
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getStuLowKnowpointStat(Long studentId, Long classId);

}
