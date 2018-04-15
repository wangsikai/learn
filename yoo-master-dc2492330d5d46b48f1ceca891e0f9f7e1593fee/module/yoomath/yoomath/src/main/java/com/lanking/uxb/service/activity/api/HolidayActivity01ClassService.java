package com.lanking.uxb.service.activity.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Class;
import com.lanking.uxb.service.activity.value.VHolidayActivity01Class;

/**
 * 假期活动01-参与活动的班级service.
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 *
 * @version 2017年6月15日
 */
public interface HolidayActivity01ClassService {

	HolidayActivity01Class get(long id);

	/**
	 * 获得教师用户名下的班级数据.
	 * 
	 * @param userId
	 *            用户id
	 * @param code
	 *            活动code
	 * @return
	 */
	List<VHolidayActivity01Class> getByUserId(Long userId, Long code);

	/**
	 * 获得教师用户名下的班级数据.
	 * 
	 * @param userId
	 *            用户id
	 */
	List<HolidayActivity01Class> getClassByUserId(long userId, long activityCode);

	/**
	 * 创建班级数据.
	 * 
	 * @param clazzs
	 */
	void create(Collection<HolidayActivity01Class> clazzs);

	/**
	 * 通过ID查询活动班级统计信息.
	 * 
	 * @param id
	 *            id
	 * @return
	 */
	VHolidayActivity01Class getClassById(long id);
}
