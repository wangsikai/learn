package com.lanking.uxb.service.honor.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 金币值log相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface CoinsLogService {
	/**
	 * 查询最近一个月金币日志
	 * 
	 * @param p
	 * @param userId
	 * @return
	 */
	Page<CoinsLog> queryCoinsLog(Pageable p, long userId);

	/**
	 * 一天内 某种行为对应获得的所有金币
	 * 
	 * @param CoinsAction
	 *            CoinsAction.XX
	 * @param date
	 *            当前时间
	 * @param userId
	 *            用户ID
	 * @return 金币总数
	 */
	Integer coinsCountByAction(CoinsAction coinsAction, Date date, Long userId);

	/**
	 * 获取默认唯一积分记录
	 * 
	 * @param action
	 *            Action {@link CoinsAction}
	 * @param userId
	 *            用户ID
	 * @return {@link CoinsLog}
	 */
	CoinsLog find(CoinsAction action, long userId);

	/**
	 * 获取默认唯一积分记录
	 * 
	 * @param ruleCode
	 *            成长值操作类型的code
	 * @param userId
	 *            用户ID
	 * @param bizId
	 *            biz Id
	 * @return {@link CoinsLog}
	 */
	CoinsLog find(int ruleCode, long userId, Long bizId);

	/**
	 * 查询日志列表
	 *
	 * @param ruleCode
	 *            ruleCode
	 * @param userId
	 *            用户id
	 * @param bizId
	 *            biz id
	 * @return {@link List}
	 */
	List<CoinsLog> findLogs(int ruleCode, long userId, Long bizId);

	/**
	 * 保存
	 * 
	 * @since 2.8
	 * @param coinsLog
	 *            {@link CoinsLog}
	 * @return {@link CoinsLog}
	 */
	CoinsLog save(CoinsLog coinsLog);

	/**
	 * 学生做作业，如果已经存在一条记录，则直接更新金币数
	 * 
	 * @param coinsLog
	 */
	void updateCoinsValue(CoinsLog coinsLog);

	/**
	 * 用户某行为金币操作次数
	 * 
	 * @param code
	 *            growth_rule code
	 * @param userId
	 *            用户ID
	 * @param start
	 *            日期范围限制 开始
	 * @param end
	 *            日期范围限制 结束
	 * @return
	 */
	Integer countActionByUser(int code, long userId, Date start, Date end);

}
