package com.lanking.uxb.zycon.holiday.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryForm;

/**
 * 假期作业题目答案归档
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public interface ZycHolidayHomeworkService {
	/**
	 * 查询作业
	 *
	 * @param form
	 *            {@link HomeworkQueryForm}
	 * @return {@link Page}
	 */
	Page<HolidayHomework> page(HomeworkQueryForm form);

	/**
	 * 
	 * 批量查询作业下发数量
	 *
	 * @param ids
	 *            假期作业IDs
	 * 
	 * @return {@link Map<Long, Long>}
	 */
	Map<Long, Long> getDistribute(Collection<Long> ids);

	/**
	 * 
	 * 批量查询当前学生作业专项提交数量
	 *
	 * @param id
	 *            假期作业ID
	 * 
	 * @return {@link Map}
	 */
	Map<Long, Long> getStuHomeworkItmeSubmitCount(Collection<Long> ids);

}
