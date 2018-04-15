package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSource;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.mall.form.CoinsGoodsOrderForm;

/**
 * 金币商品订单接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
public interface CoinsGoodsOrderService {

	CoinsGoodsOrder createOrder(CoinsGoodsOrderForm form);

	CoinsGoodsOrder createLotteryOrder(CoinsGoodsOrderForm form);

	CoinsGoodsOrderSnapshot createOrderSnapshot(CoinsGoodsOrder order);

	List<String> getLatestRecord(UserType userType, Long userId);

	CursorPage<Long, CoinsGoodsOrder> query(CoinsGoodsOrderQuery query, CursorPageable<Long> pageable);

	Page<CoinsGoodsOrder> queryPage(CoinsGoodsOrderQuery query, Pageable pageable);

	void delete(long id);

	CoinsGoodsOrder get(long id);

	/**
	 * 查询用户一期内未完成的订单列表
	 *
	 * @param userId
	 *            用户id
	 * @param seasonId
	 *            每期id
	 * @return {@link List}
	 */
	List<CoinsGoodsOrder> queryLotteryUnFinish(long userId, long seasonId);

	CoinsGoodsOrder fill(long id, String p0);

	long countTodayCoinsGoodsBuyCount(long coinsGoodsId, long userId);

	/**
	 * 获得抽奖活动订单数据.
	 * 
	 * @param activityCode
	 * @param userId
	 * @param pageable
	 * @return
	 */
	Page<CoinsGoodsOrder> queryLotteryActivityOrderPage(Long activityCode, Long userId, Pageable pageable);

	/**
	 * 填充P0信息（无校验）.
	 * 
	 * @param orderId
	 *            订单
	 * @param p0
	 */
	void fillLotteryActivityOrderP0(long orderId, String p0);

	/**
	 * 删除抽奖活动订单数据.
	 * 
	 * @param activityCode
	 */
	void deleteLotteryActivityOrders(long activityCode);

	@SuppressWarnings("rawtypes")
	List<Map> queryLotteryActivityOrder(CoinsGoodsOrderSource orderSource, Long userId, Long seasonId);

	/**
	 * 查询用户一期内未完成的订单列表,增加入参CoinsGoodsOrderSource
	 *
	 * @param userId
	 *            用户id
	 * @param seasonId
	 *            每期id
	 * @param orderSource
	 *            订单来源
	 * @return {@link List}
	 */
	List<CoinsGoodsOrder> queryLotteryUnFinish(long userId, long seasonId, CoinsGoodsOrderSource orderSource);

	Map<Long, CoinsGoodsOrder> mget(Collection<Long> ids);
}
