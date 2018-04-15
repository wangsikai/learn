package com.lanking.uxb.service.activity.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ClassUser;

/**
 * 假期活动01-参与活动的班级学生service.
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 *
 * @version 2017年6月15日
 */
public interface HolidayActivity01ClassUserService {

	HolidayActivity01ClassUser get(long id);

	/**
	 * 获得班级下的所有用户数据.
	 * 
	 * @param classId
	 *            班级id
	 * @param code
	 *            活动code
	 * @return
	 */
	List<HolidayActivity01ClassUser> getByClass(Long classId, Long code);

	/**
	 * 初始化班级下用户数据
	 * 
	 * @param
	 * @return
	 */
	void create(Collection<HolidayActivity01ClassUser> classUser);

}
