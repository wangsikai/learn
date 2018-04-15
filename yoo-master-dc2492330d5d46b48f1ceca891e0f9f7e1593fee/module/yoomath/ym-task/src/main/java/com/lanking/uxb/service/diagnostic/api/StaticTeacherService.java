package com.lanking.uxb.service.diagnostic.api;

import com.lanking.cloud.domain.yoo.user.Teacher;

/**
 * 诊断统计任务教师服务接口.
 * 
 * @author wlche
 *
 */
public interface StaticTeacherService {

	/**
	 * 获得教师.
	 * 
	 * @param id
	 *            教师ID
	 * @return
	 */
	Teacher get(long id);
}
