package com.lanking.uxb.service.mall.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.uxb.service.mall.api.MemberPackageOrderService;
import com.lanking.uxb.service.mall.api.MemberPackageOrderSettlementService;
import com.lanking.uxb.service.mall.api.MemberPackageService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.cache.CallbackOrderCache;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentCallbackProvider;
import com.lanking.uxb.service.payment.weixin.response.PayCallbackResult;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.api.UserMemberService;

@Service
public class MemberPackageWXPaymentCallbackProvider implements WXPaymentCallbackProvider {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberPackageOrderService memberPackageOrderService;
	@Autowired
	private MemberPackageService memberPackageService;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private CallbackOrderCache callbackOrderCache;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private MemberPackageOrderSettlementService memberPackageOrderSettlementService;

	@Override
	public OrderPayBusinessSpace getBusinessSpaceName() {
		return OrderPayBusinessSpace.MEMBER_PACKAGE;
	}

	@Override
	public boolean accept(OrderPayBusinessSpace space) {
		return space == this.getBusinessSpaceName();
	}

	@Override
	public void handleUnifiedPayResult(PayCallbackResult result) {
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
		String outTradeNo = result.getOutTradeNo(); // 本地订单ID
		String timeEnd = result.getTimeEnd(); // 交易付款时间
		String transactionId = result.getTransactionId(); // 微信支付订单号
		String resultCode = result.getResultCode(); // 交易状态

		long orderID = Long.parseLong(outTradeNo);
		if ("SUCCESS".equalsIgnoreCase(resultCode)) {
			// 交易成功

			// 判断订单是否已被处理中
			if (callbackOrderCache.hasProcessing(OrderBusinessSource.USER_MEMBER, orderID)) {
				return;
			} else {
				callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderID);
			}

			MemberPackageOrder order = memberPackageOrderService.get(orderID);
			if (order.getStatus() == MemberPackageOrderStatus.NOT_PAY) {
				logger.info("[会员套餐] 微信购买, orderID = " + orderID);

				Date payTime = null;
				try {
					payTime = formate.parse(timeEnd);
				} catch (ParseException e) {
					payTime = new Date();
					logger.error(e.getMessage(), e);
				}

				// 支付订单
				memberPackageOrderService.updatePaymentCallback(orderID, null, transactionId, payTime);

				// 完成订单
				order = memberPackageOrderService.updateOrderStatus(orderID, null, MemberPackageOrderStatus.COMPLETE);

				// 处理会员数据
				MemberPackage memberPackage = memberPackageService.get(order.getMemberPackageId());
				userMemberService.createOrRenew(order.getUserId(), payTime, memberPackage, orderID, null);

				// 用户会员开通动作行为
				userActionService.asyncAction(UserAction.OPEN_VIP, order.getUserId(), null);

				// 处理渠道统计
				memberPackageOrderSettlementService.createByOrder(order, 1);
			}
			callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderID);
		} else {
			// 交易失败
			memberPackageOrderService.updateOrderStatus(orderID, null, MemberPackageOrderStatus.FAIL);
		}
	}

}
