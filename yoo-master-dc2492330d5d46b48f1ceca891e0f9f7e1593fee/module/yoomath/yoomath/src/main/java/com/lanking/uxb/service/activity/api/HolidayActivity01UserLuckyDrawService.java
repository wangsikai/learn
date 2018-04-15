package com.lanking.uxb.service.activity.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01UserLuckyDraw;

/**
 * 假期活动01-参与活动的用户抽奖记录service.
 * 
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 *
 * @version 2017年6月16日
 */
public interface HolidayActivity01UserLuckyDrawService {

	HolidayActivity01UserLuckyDraw get(long id);

	/**
	 * 保存抽奖记录
	 * 
	 * @param record
	 *            活动记录
	 */
	void save(HolidayActivity01UserLuckyDraw record);

	/**
	 * 用户抽奖记录,可指定时间
	 * 
	 * @param code
	 *            活动code
	 * @param userId
	 *            用户id
	 * @param date
	 *            指定时间
	 * @return
	 */
	List<HolidayActivity01UserLuckyDraw> getByUser(long code, long userId, Date date);

	/**
	 * 奖池抽奖记录,可指定时间
	 * 
	 * @param code
	 *            活动code
	 * @param date
	 *            指定时间
	 * @return
	 */
	List<HolidayActivity01UserLuckyDraw> getByCode(long code, Date date);

	/**
	 * 用户抽奖记录统计
	 * 
	 * @param code
	 *            活动code
	 * @param userId
	 *            用户id
	 * @param date
	 *            指定时间
	 * @return
	 */
	long getCountByUser(long code, long userId, Date date);
}
