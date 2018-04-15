package com.lanking.uxb.zycon.qs.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 教师Service
 *
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public interface ZycTeacherService {

	List<Teacher> mgetList(Collection<Long> ids);

	Map<Long, Teacher> mget(Collection<Long> ids);

	Teacher get(long id);

	/**
	 * 根据教师的登录名、手机、Email进行查找
	 *
	 * @param loginValue
	 *            登录名、手机、Email查询值
	 * @return 教师
	 */
	Teacher findByAccountNameOrMobileOrEmail(String loginValue);

	/**
	 * 获取所有老师
	 * 
	 * @param cursorPageable
	 * 
	 * @return
	 */
	CursorPage<Long, Teacher> getAll(CursorPageable<Long> cursorPageable);
}
