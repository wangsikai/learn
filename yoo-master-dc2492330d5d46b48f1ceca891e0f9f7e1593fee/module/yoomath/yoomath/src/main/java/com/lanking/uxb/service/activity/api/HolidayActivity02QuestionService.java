package com.lanking.uxb.service.activity.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Question;

/**
 * 假期活动02接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface HolidayActivity02QuestionService {

	/**
	 * 保存题目
	 * 
	 * @param code
	 */
	void save(HolidayActivity02Question holidayActivity02Question);
	
	/**
	 * 获取pk题目id
	 * 
	 * @param code
	 */
	List<Long> getQuestionIds(Long activityCode,Long pkRecordId);
	
	/**
	 * 获取pk题目
	 * 
	 * @param code
	 */
	HolidayActivity02Question getQuestion(Long activityCode,Long pkRecordId);

}
