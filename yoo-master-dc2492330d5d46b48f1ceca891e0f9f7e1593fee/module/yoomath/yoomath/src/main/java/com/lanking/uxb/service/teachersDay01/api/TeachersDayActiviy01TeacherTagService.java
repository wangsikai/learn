package com.lanking.uxb.service.teachersDay01.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01TeacherTag;

/**
 * 教师节活动01-教师标签service.
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 *
 * @version 2017年8月28日
 */
public interface TeachersDayActiviy01TeacherTagService {

	/**
	 * 获得教师用户的所有标签.
	 * 
	 * @param teacherId
	 *            教师id
	 * @return
	 */
	List<TeachersDayActiviy01TeacherTag> getByTeacher(Long teacherId);

	/**
	 * 
	 * @param teacherId
	 * @param tagCode
	 */
	void save(Long teacherId, long tagCode);
}
