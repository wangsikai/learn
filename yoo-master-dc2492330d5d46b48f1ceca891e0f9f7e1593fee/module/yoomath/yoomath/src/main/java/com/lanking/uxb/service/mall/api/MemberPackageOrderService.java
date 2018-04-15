package com.lanking.uxb.service.mall.api;

import java.util.Date;

import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.ThirdPaymentMethod;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;

/**
 * 会员订单套餐接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月27日
 */
public interface MemberPackageOrderService {

	/**
	 * 创建会员订单.
	 * 
	 * @param userID
	 *            用户
	 * @param memberPackageID
	 *            套餐
	 * @param memberType
	 *            会员类型
	 * @param payMode
	 *            支付模式
	 * @param paymentPlatformCode
	 *            支付平台
	 * @param thirdPaymentMethod
	 *            第三方支付方式
	 * @param attachData
	 *            附属数据
	 * @param card
	 *            会员卡
	 * @return
	 */
	MemberPackageOrder createOrder(long userID, Long memberPackageID, MemberType memberType, PayMode payMode,
			Integer paymentPlatformCode, ThirdPaymentMethod thirdPaymentMethod, String attachData,
			MemberPackageCard card);

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
	 * 获取订单.
	 * 
	 * @param orderID
	 *            订单ID.
	 * @return
	 */
	MemberPackageOrder get(long orderID);

	/**
	 * 获取订单快照.
	 * 
	 * @param snapshotOrderID
	 *            快照ID
	 * @return
	 */
	MemberPackageOrderSnapshot getSnapshot(long snapshotOrderID);

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
	 * 直接删除订单（废单使用，当前仅微信前台控制使用）
	 * 
	 * @param memberPackageOrderID
	 */
	void deleteOrder(long memberPackageOrderID);

	/**
	 * 获取用户最近一次的未支付会员套餐订单.
	 * 
	 * @param userId
	 *            用户ID
	 * @param memberPackageID
	 *            套餐ID
	 * @return
	 */
	MemberPackageOrder getWXLastNotpayOrder(long userId, long memberPackageID);

	/**
	 * 更新待支付订单数据.
	 * 
	 * @param orderId
	 *            订单ID
	 * @param paymentPlatformCode
	 *            支付平台
	 * @param thirdPaymentMethod
	 *            第三方支付方式
	 * @return
	 */
	MemberPackageOrder updatePayOrderInfos(long orderId, int paymentPlatformCode, ThirdPaymentMethod thirdPaymentMethod);

	/**
	 * 刷新订单（相当于重新生成订单，为避免废单，更新创建时间，目前仅用于代付未支付的订单功能，其他功能请慎用）
	 * 
	 * @since 学生端1.4.2
	 * @param orderId
	 *            订单ID
	 */
	MemberPackageOrder refreshOrder(long orderId);
}
