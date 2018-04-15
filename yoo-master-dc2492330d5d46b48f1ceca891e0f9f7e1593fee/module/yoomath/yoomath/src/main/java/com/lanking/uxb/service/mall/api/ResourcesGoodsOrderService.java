package com.lanking.uxb.service.mall.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.resources.ResourcesGoodsOrder;
import com.lanking.cloud.domain.yoo.order.resources.ResourcesGoodsOrderSnapshot;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.Value;

/**
 * 资源订单接口
 * 
 * @author zemin.song
 * @version 2016年9月2日
 */
public interface ResourcesGoodsOrderService {

	ResourcesGoodsOrder get(long id);

	Map<Long, ResourcesGoodsOrder> mget(Collection<Long> ids);

	/**
	 * 通过用户查询订单(试卷订单)
	 * 
	 * @param userId
	 *            当前用户
	 * @return
	 */
	Page<ResourcesGoodsOrder> getOrdersByUser(Long userId, int type, Pageable pageable);

	/**
	 * 通过用户及商品查询已完成的订单.
	 * 
	 * @param userID
	 *            用户
	 * @param goodsID
	 *            商品
	 * @return
	 */
	List<ResourcesGoodsOrder> findCompleteOrderByUserAndGoods(long userID, long goodsID);

	/**
	 * 创建订单.
	 * 
	 * @param userID
	 *            用户
	 * @param goodsID
	 *            商品
	 * @param payMod
	 *            支付方式
	 * @param paymentPlatformCode
	 *            支付平台
	 * @param attachData
	 *            附加数据
	 * @return
	 */
	ResourcesGoodsOrder createOrder(long userID, long goodsID, PayMode payMod, Integer paymentPlatformCode,
			String attachData);

	/**
	 * 创建订单快照.
	 * 
	 * @param resourcesGoodsOrder
	 *            资源订单
	 * @return
	 */
	ResourcesGoodsOrderSnapshot createOrderSnapshot(ResourcesGoodsOrder resourcesGoodsOrder);

	/**
	 * 订单支付返回更新订单.
	 * 
	 * @param resourcesGoodsOrderID
	 *            订单ID
	 * @param paymentPlatformOrderCode
	 *            支付平台订单号
	 * @param paymentCode
	 *            支付流水号
	 * @param payTime
	 *            支付时间
	 * @return
	 */
	ResourcesGoodsOrder updatePaymentCallback(long resourcesGoodsOrderID, String paymentPlatformOrderCode,
			String paymentCode, Date payTime);

	/**
	 * 更新订单状态.
	 * 
	 * @param resourcesGoodsOrderID
	 *            订单ID
	 * @param updateID
	 *            更新人
	 * @param status
	 * @return
	 */
	ResourcesGoodsOrder updateOrderStatus(long resourcesGoodsOrderID, Long updateID, GoodsOrderStatus status);

	/**
	 * 更新订单状态.
	 * 
	 * @param createId
	 *            创建用户
	 * @param id
	 *            订单ID
	 * @return
	 */
	Value delOrders(Long createId, Long id);
}
