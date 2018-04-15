package com.lanking.uxb.zycon.mall.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrderSnapshot;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.mall.form.OrderForm;

/**
 * 金币兑换订单service
 * 
 * @since V2.0
 * @author wangsenhao
 *
 */
public interface ZycCoinsGoodsOrderService {
	/**
	 * 兑换管理列表
	 * 
	 * @param form
	 * 
	 * @param p
	 * @return
	 */
	Page<CoinsGoodsOrder> queryOrderList(OrderForm form, Pageable p);

	/**
	 * 更新订单
	 * 
	 * @param orderId
	 *            订单id
	 * @param status
	 *            订单状态
	 */
	void updateStatus(Long orderId, GoodsOrderStatus status, String sellerNotes, Long userId);

	/**
	 * 获取金币商品订单
	 * 
	 * @param id
	 * @return
	 */
	CoinsGoodsOrder get(Long id);

	/**
	 * 待兑换的个数
	 * 
	 * @return
	 */
	Long tobeExchageCount();

	/**
	 * 创建快照
	 * 
	 * @param order
	 * @return
	 */
	CoinsGoodsOrderSnapshot createOrderSnapshot(CoinsGoodsOrder order);

	/**
	 * 统计一段时间的各类型的商城兑换数据
	 *
	 * @param beginDate
	 *            查询开始时间
	 * @param endDate
	 *            查询结束时间
	 * @param type
	 *            虚拟商品类型
	 * @return {@link List}
	 */
	List<Map> statisticTypeData(String beginDate, String endDate, Integer type);

	/**
	 * 统计商品具体兑换情况
	 *
	 * @param beginDate
	 *            查询开始时间
	 * @param endDate
	 *            查询结束时间
	 * @param type
	 *            {@link CoinsGoodsType}
	 * @param pageable
	 *            分页信息
	 * @return {@link Page}
	 */
	Page<Map> statisticByName(String beginDate, String endDate, Integer type, Pageable pageable);

	/**
	 * 根据条件查询每个中奖商品的统计
	 * 
	 * @param query
	 * @return
	 */
	List<Map> queryLotteryOrderStatis(ZycLotteryRecordQuery query);

	/**
	 * 抽奖商品总体统计(参与用户、总抽奖次数、总收入金币、总奖品价值金币、净收入)
	 * 
	 * @param query
	 * @return
	 */
	Map lotteryTotalStatis(ZycLotteryRecordQuery query);

	/**
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<Map> lotteryUserRecordList(ZycLotteryRecordQuery query, Pageable pageable);
}
