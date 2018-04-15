package com.lanking.uxb.service.teachersDay01.api;

import java.util.List;

/**
 * 学生打标签相关接口
 * 
 * @author wangsenhao
 *
 */
public interface TeachersDayActiviy01StudentTagTeacherService {

	/**
	 * 打标签
	 * 
	 * @param tagCodes
	 * @param studentId
	 * @param teacherId
	 */
	void setTag(List<Long> tagCodes, Long studentId, Long teacherId);

	/**
	 * 获取学生给该老师打的标签
	 * 
	 * @param studentId
	 * @param teacherId
	 * @return
	 */
	List<Long> findTagList(Long studentId, Long teacherId);

	/**
	 * 给教师打标签的人数
	 * 
	 * @param teacherId
	 */
	long numberOfSetTag(Long teacherId);
}
