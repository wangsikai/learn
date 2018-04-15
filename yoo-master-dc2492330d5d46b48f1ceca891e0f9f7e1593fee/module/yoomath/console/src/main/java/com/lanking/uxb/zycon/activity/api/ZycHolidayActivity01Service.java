package com.lanking.uxb.zycon.activity.api;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.uxb.zycon.mall.form.LotterySeasonForm;

/**
 * 暑期作业活动相关接口.
 * 
 * @since 教师端 v1.2.0
 *
 */
public interface ZycHolidayActivity01Service {

	/**
	 * 查找活动.
	 * 
	 * @param id
	 * @return
	 */
	HolidayActivity01 get(long id);

	/**
	 * 初始化活动数据
	 * 
	 * @param seasonId
	 *            抽奖期别
	 */
	void init(long seasonId,LotterySeasonForm form);

	/**
	 * 清空所有练习及练习所属的错题等.
	 * 
	 * @param activityCode
	 *            活动代码
	 */
	void deleteAllExerciseAndQuestion(Long activityCode);

	/**
	 * 获取当前有几个假期作业抽奖<br>
	 * 正常情况下应该只有1个抽奖活动
	 * 
	 * @return
	 */
	Long countHolidayActivity01();
}
