package com.lanking.uxb.service.teachersDay01.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01Tag;
import com.lanking.cloud.domain.yoo.user.Sex;

/**
 * 教师节活动01-标签service.
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 *
 * @version 2017年8月28日
 */
public interface TeachersDayActiviy01TagService {

	TeachersDayActiviy01Tag get(long code);

	Map<Long, TeachersDayActiviy01Tag> mget(Collection<Long> codes);

	List<TeachersDayActiviy01Tag> mgetList(Collection<Long> codes);

	/**
	 * 通过性别查找对应的标签
	 * 
	 * @param sex
	 * @return
	 */
	List<TeachersDayActiviy01Tag> findList(Sex sex);
}
