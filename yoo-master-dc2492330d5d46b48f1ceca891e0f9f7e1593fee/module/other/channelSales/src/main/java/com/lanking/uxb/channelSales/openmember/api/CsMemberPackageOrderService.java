package com.lanking.uxb.channelSales.openmember.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.uxb.channelSales.openmember.form.OpenMemberPackageForm;

import javax.servlet.http.HttpServletRequest;

/**
 * 套餐订单管理Service
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
public interface CsMemberPackageOrderService {

	/**
	 * 后台开通会员处理
	 *
	 * @param form
	 *            {@link OpenMemberPackageForm}
	 * @param userId
	 *            渠道商id
	 */
	void create(OpenMemberPackageForm form, long userId);

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
	 * 创建订单快照
	 * 
	 * @param order
	 * @param updateAt
	 * @param updateID
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
	 * 通过Excel开通会员
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @param channelCode
	 *            渠道商code(渠道商导入开通会员时，才会传此值，其他情况下不传此值)
	 * @param userType
	 *            开通会员的用户类型
	 * @param memberType
	 *            需要开通的会员类型
	 */
	List<Long> importMemberUser(HttpServletRequest request, Integer channelCode, UserType userType,
	        MemberType memberType);

}
