package com.lanking.uxb.service.mall.api;

import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSettlement;

/**
 * 会员套餐订单结算表接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年2月27日
 */
public interface MemberPackageOrderSettlementService {

	/**
	 * 创建会员订单结算数据.
	 * <p>
	 * 创建会员时调用，初始化结算数据.
	 * </p>
	 * 
	 * @param order
	 *            会员订单.
	 * @param memberCount
	 *            订单会员数量
	 * @return
	 */
	void createByOrder(MemberPackageOrder order, int memberCount);

	/**
	 * 根据订单获取结算数据.
	 * 
	 * @param orderId
	 *            订单编号
	 * @return
	 */
	MemberPackageOrderSettlement findByOrder(long orderId);
}
