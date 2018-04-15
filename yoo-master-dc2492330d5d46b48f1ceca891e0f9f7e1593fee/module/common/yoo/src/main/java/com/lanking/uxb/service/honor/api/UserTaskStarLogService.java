package com.lanking.uxb.service.honor.api;

import java.util.Date;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskStarLog;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
public interface UserTaskStarLogService {
	/**
	 * 根据日期、用户id查询数据
	 *
	 * @param date
	 *            查询日期
	 * @param userId
	 *            用户id
	 * @return {@link UserTaskStarLog}
	 */
	UserTaskStarLog query(Date date, long userId);

	/**
	 * 更新/创建用户星级宝箱数据
	 *
	 * @param userId
	 *            用户id
	 * @param star
	 *            星星数
	 * @param content
	 *            更新具体内容
	 * @param date
	 *            当前日期
	 * @return {@link UserTaskStarLog}
	 */
	UserTaskStarLog update(long userId, int star, String content, Date date);

	/**
	 * 领取宝箱更新操作
	 *
	 * @param userId
	 *            用户id
	 * @param star
	 *            星
	 * @param content
	 *            内容
	 * @param date
	 *            时间
	 * @param coinsValue
	 *            金币值
	 * @param growthValue
	 *            成长值
	 * @return {@link UserTaskStarLog}
	 */
	UserTaskStarLog earn(long userId, int star, String content, Date date, int coinsValue, int growthValue);
}
