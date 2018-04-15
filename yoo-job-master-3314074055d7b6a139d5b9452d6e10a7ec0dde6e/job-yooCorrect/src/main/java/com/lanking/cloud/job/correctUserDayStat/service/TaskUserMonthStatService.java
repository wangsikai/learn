package com.lanking.cloud.job.correctUserDayStat.service;

import java.util.List;

/**
 * 小悠快批月统计
 * 
 * @author peng.zhao
 * @version 2018-3-13
 */
public interface TaskUserMonthStatService {

	/**
	 * 执行统计
	 * 
	 * @param userIds 用户id
	 */
	void statUserCorrect(List<Long> userIds);
}
