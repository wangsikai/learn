package com.lanking.uxb.service.mall.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import com.lanking.uxb.service.payment.alipay.api.AliPaymentCallbackProvider;
import com.lanking.uxb.service.payment.alipay.client.AlipayConfig;
import com.lanking.uxb.service.payment.alipay.response.PayCallbackResult;
import com.lanking.uxb.service.payment.cache.CallbackOrderCache;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.api.UserMemberService;

/**
 * 会员套餐支付宝支付回调处理.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月28日
 */
@Service
public class MemberPackageAliPaymentCallbackProvider implements AliPaymentCallbackProvider {
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
	public void handlePayCallbackResult(boolean isAsync, PayCallbackResult result, HttpServletRequest request,
			HttpServletResponse response) {
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String out_trade_no = result.getOut_trade_no(); // 本地订单ID
		String gmt_payment = result.getGmt_payment(); // 交易付款时间
		String trade_status = result.getTrade_status(); // 交易状态

		Long orderID = Long.parseLong(out_trade_no);
		if ("TRADE_SUCCESS".equalsIgnoreCase(trade_status) || "TRADE_PENDING".equalsIgnoreCase(trade_status)
				|| "TRADE_FINISHED".equalsIgnoreCase(trade_status)) {

			// 判断订单是否已被处理中
			if (callbackOrderCache.hasProcessing(OrderBusinessSource.USER_MEMBER, orderID)) {
				return;
			} else {
				callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderID);
			}

			MemberPackageOrder order = memberPackageOrderService.get(orderID);
			if (order.getStatus() == MemberPackageOrderStatus.NOT_PAY) {
				logger.info("[会员套餐] 支付宝购买, orderID = " + orderID);

				// 买家付款成功
				Date payTime = null;
				if (StringUtils.isNotBlank(gmt_payment)) {
					try {
						payTime = formate.parse(gmt_payment);
					} catch (ParseException e) {
						payTime = new Date();
						logger.error(e.getMessage(), e);
					}
				} else {
					payTime = new Date();
				}

				// 支付订单
				memberPackageOrderService.updatePaymentCallback(orderID, null, result.getTrade_no(), payTime);

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
		} else if ("TRADE_CLOSED".equalsIgnoreCase(trade_status)) {
			// 交易关闭未支付成功
			memberPackageOrderService.updateOrderStatus(orderID, null, MemberPackageOrderStatus.FAIL);
		}

		if (!isAsync) {
			// 非异步通知，需跳转页面
			try {
				response.sendRedirect(
						AlipayConfig.return_server + "/page/member/open-vip-result.html?o=" + out_trade_no);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
