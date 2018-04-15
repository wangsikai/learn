package com.lanking.uxb.service.honor.api;

import java.util.Date;

import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 成长值log相关接口
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface GrowthLogService {
	/**
	 * 获取默认唯一成长值记录
	 * 
	 * @param action
	 *            growthAction {@link GrowthAction}
	 * @param userId
	 *            用户ID
	 * @return {@link GrowthLog}
	 */
	GrowthLog find(GrowthAction action, long userId);

	/**
	 * 获取默认唯一积分记录
	 * 
	 * @param ruleCode
	 *            成长值操作类型的code
	 * @param userId
	 *            用户ID
	 * @return {@link GrowthLog}
	 */
	GrowthLog find(int ruleCode, long userId);

	/**
	 * 保存
	 * 
	 * @since 2.8
	 * @param growthLog
	 *            {@link GrowthLog}
	 * @return {@link GrowthLog}
	 */
	GrowthLog save(GrowthLog growthLog);

	/**
	 * 获取是否已经签到过
	 * 
	 * @param action
	 * @param userId
	 * @return
	 */
	boolean getCheck(GrowthAction action, long userId);

	/**
	 * 查询最近一个月成长日志
	 * 
	 * @param p
	 * @param userId
	 * @return
	 */
	Page<GrowthLog> queryGrowLog(Pageable p, long userId);

	/**
	 * 学生做作业，如果已经存在一条记录，则直接更新成长值
	 * 
	 * @param coinsLog
	 */
	void updateGrowthValue(GrowthLog coinsLog);

	/**
	 * 用户某行为成长值操作次数
	 * 
	 * @param code
	 *            growth_rule code
	 * @param userId
	 *            用户ID
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	Integer countActionByUser(int code, long userId, Date start, Date end);

	/**
	 * 获取用户最新一条签到记录
	 * 
	 * @param userId
	 *            用户ID
	 * @param ruleCode
	 *            规则编号
	 * @return
	 */
	GrowthLog getLastestCheckIn(long userId, int ruleCode);

}
