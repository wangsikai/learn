package com.lanking.uxb.service.activity.api;

import java.util.Map;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.uxb.service.activity.form.HolidayActivityOrderSaveForm;

/**
 * 假期活动01接口
 * 
 * @author wangsenhao
 *
 */
public interface HolidayActivity01Service {

	/**
	 * 获取假期活动配置
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	Map<String, Object> getActivityCfg(long code);

	/**
	 * 获取假期活动对象
	 * 
	 * @param code
	 * @return
	 */
	HolidayActivity01 get(long code);

	/**
	 * 获取参与假期活动用户新增抽奖次数
	 * 
	 * @param code
	 *            活动code
	 * @param userId
	 *            用户id
	 * @return
	 */
	int getNewLuckyDraw(long code, long userId);

	/**
	 * 抽奖首页数据
	 * 
	 * @param code
	 *            活动code
	 * @param userId
	 *            用户id
	 * @return
	 */
	Map<String, Object> getLotteryIndex(long code, long userId);

	/**
	 * 抽奖
	 * 
	 * @param code
	 *            活动code
	 * @param userId
	 *            用户id
	 * @return
	 */
	Map<String, Object> getHolidayDraw(long code, long userId);

	/**
	 * 更新订单
	 * 
	 * @param order
	 *            订单信息
	 * @return
	 */
	CoinsGoodsOrder updateHolidayOrder(HolidayActivityOrderSaveForm order);
}
