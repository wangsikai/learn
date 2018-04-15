package com.lanking.uxb.service.lottery.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 抽奖期别定时器
 *
 * @author xinyu.zhou
 * @since 2.4.0
 */
public interface TaskLotterySeasonService {
	/**
	 * 获得最新的抽奖一期
	 *
	 * @return {@link CoinsLotterySeason}
	 */
	CoinsLotterySeason getLatestSeason();

	/**
	 * 获取所有活动最新的一期
	 * 
	 * @return
	 */
	List<CoinsLotterySeason> findNewList();

	/**
	 * 更新期别的状态
	 * 
	 * @param id
	 *            期别id
	 * @param status
	 *            状态值
	 */
	void updateStatus(long id, Status status);

	/**
	 * 新建一期
	 *
	 * @param from
	 *            {@link CoinsLotterySeason}
	 * @return {@link CoinsLotterySeason}
	 */
	CoinsLotterySeason save(CoinsLotterySeason from);
}
