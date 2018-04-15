package com.lanking.uxb.channelSales.order.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.channelSales.order.form.OrderQueryForm;

/**
 * 订单管理
 *
 * @author zemin.song
 * @since 2.5.0
 */
public interface CsOrderService {

	/**
	 * 会员套餐
	 * 
	 * @param query
	 * @return Page<Map>
	 */
	Page<Map> memberPackageOrderQuery(OrderQueryForm query, Pageable pageable);

	/**
	 * 错题代印
	 * 
	 * @param query
	 * @return Page<Map>
	 */
	Page<Map> fallibleQuestionPrintOrderQuery(OrderQueryForm query, Pageable pageable);

	/**
	 * 试卷订单
	 * 
	 * @param query
	 * @return Page<Map>
	 */
	Page<Map> resourcesGoodsOrderQuery(OrderQueryForm query, Pageable pageable);

	/**
	 * 通过订单查询用户帐号
	 * 
	 * @param orderId
	 *            会员订单id
	 * @return List
	 */
	List<String> searchUserByOrder(Long orderId);

	/**
	 * 通过订单ID查询对应的一个用户
	 * 
	 * @param orderId
	 *            会员订单id
	 * @return List
	 */
	List<Map> searchToOneOrderUserByOrderId(List<Long> orderIds);

	/**
	 * 查询会员卡片激活记录
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page<Map> queryMemberCardList(OrderQueryForm query, Pageable pageable);

	/**
	 * 通过支付单号核对支付结果
	 * 
	 * @param map
	 *            包含订单信息的Map
	 * @return
	 */
	public void checkPayCode(Map map);
}
