package com.lanking.uxb.zycon.mall.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.goods.lottery.CoinsLotterySeason;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 每期与奖品对应关系的接口
 * 
 * @author wangsenhao
 *
 */
public interface ZycCoinsLotterySeasonService {
	/**
	 * 获取单个season对象
	 * 
	 * @param id
	 * @return
	 */
	CoinsLotterySeason get(Long id);

	/**
	 * 查询期别的个数
	 * 
	 * @param code
	 * @return
	 */
	Long countSeason(Integer code);

	/**
	 * 通过期名查询
	 * 
	 * @param title
	 *            期名称
	 * @return
	 */
	CoinsLotterySeason getByTitleName(String title, Integer code);

	/**
	 * 获取最新的(当前一期)的期别信息
	 * 
	 * @return
	 */
	CoinsLotterySeason getLastest();

	/**
	 * 获取期别列表
	 * 
	 * @return
	 */
	List<CoinsLotterySeason> list();

	/**
	 * 通过活动编号查询期别列表
	 * 
	 * @param code
	 * @return
	 */
	List<CoinsLotterySeason> list(Integer code);

	/**
	 * 更新期别状态
	 * 
	 * @param status
	 * @param seasonId
	 */
	void updateSeasonStatus(Status status, Long seasonId);
}
