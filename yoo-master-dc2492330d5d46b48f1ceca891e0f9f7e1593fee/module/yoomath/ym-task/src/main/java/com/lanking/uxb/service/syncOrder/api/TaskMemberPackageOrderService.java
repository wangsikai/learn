/**
 * 
 */
package com.lanking.uxb.service.syncOrder.api;

import java.util.Date;

import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
public interface TaskMemberPackageOrderService {

	/**
	 * 分页查询订单（未支付）
	 *
	 * @param cursorPageable
	 *            分页条件
	 * @return 分页查询结果
	 */
	CursorPage<Long, MemberPackageOrder> findMemberPackageOrderByNotPay(CursorPageable<Long> cursorPageable,
			Date nowTime);

	/**
	 * 订单支付返回更新订单.
	 * 
	 * @param memberPackageOrderID
	 *            订单ID
	 * @param paymentPlatformOrderCode
	 *            支付平台订单号
	 * @param paymentCode
	 *            支付流水号
	 * @param payTime
	 *            支付时间
	 * @return
	 */
	MemberPackageOrder updatePaymentCallback(long memberPackageOrderID, String paymentPlatformOrderCode,
			String paymentCode, Date payTime);

	/**
	 * 创建订单快照.
	 * 
	 * @param order
	 *            订单
	 * @param updateAt
	 *            更新时间
	 * @param updateID
	 *            更新人，系统更新时为空
	 * @return
	 */
	MemberPackageOrderSnapshot createOrderSnapshot(MemberPackageOrder order, Date updateAt, Long updateID);

	/**
	 * 更新订单状态.
	 * 
	 * @param memberPackageOrderID
	 *            订单ID
	 * @param updateID
	 *            更新人
	 * @param status
	 * @return
	 */
	MemberPackageOrder updateOrderStatus(long memberPackageOrderID, Long updateID, MemberPackageOrderStatus status);

	/**
	 * 获取订单.
	 * 
	 * @param orderID
	 *            订单ID.
	 * @return
	 */
	MemberPackageOrder get(long orderID);

}
