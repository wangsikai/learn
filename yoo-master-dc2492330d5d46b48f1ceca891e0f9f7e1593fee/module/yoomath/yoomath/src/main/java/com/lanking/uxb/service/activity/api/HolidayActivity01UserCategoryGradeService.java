package com.lanking.uxb.service.activity.api;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01UserCategoryGrade;

/**
 * 假期活动01接口
 * 
 * @author wangsenhao
 *
 */
public interface HolidayActivity01UserCategoryGradeService {
	/**
	 * 获取假期活动老师选择的教材和版本对象
	 * 
	 * @param code
	 * @return
	 */
	HolidayActivity01UserCategoryGrade get(long code,long userId);
	
	/**
	 * 初始化用户选择的教材版本记录
	 * 
	 * @param
	 * @return
	 */
	void create(HolidayActivity01UserCategoryGrade userCategoryGrade);
	
	/**
	 * 更新用户选择的教材版本记录
	 * 
	 * @param
	 * @return
	 */
	void update(HolidayActivity01UserCategoryGrade userCategoryGrade);

}
