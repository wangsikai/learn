package com.lanking.uxb.service.activity.api;

import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Answer;

/**
 * 假期活动02接口
 * 
 * @author qiuxue.jiang
 *
 */
public interface HolidayActivity02AnswerService {

	/**
	 * 保存答案记录
	 * 
	 * @param code
	 */
	void save(HolidayActivity02Answer answer);

	/**
	 * 获取真题对应的答案记录
	 * 
	 * @param code
	 */
	HolidayActivity02Answer getPastExamAnswer(Long questionsId);
}
