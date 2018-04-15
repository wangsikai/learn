package com.lanking.uxb.service.member.api;

import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;

/**
 * 会员订单相关接口.
 * 
 * @author wlche
 *
 */
public interface ZyMMemberOrderService {

	/**
	 * 更新IAP支付订单状态.
	 * 
	 * @param memberPackageOrder
	 *            订单
	 * @param status
	 *            状态
	 * @param receiptData
	 *            苹果凭证信息
	 */
	void updateIAPOrder(MemberPackageOrder memberPackageOrder, MemberPackageOrderStatus status, String receiptData);
}
