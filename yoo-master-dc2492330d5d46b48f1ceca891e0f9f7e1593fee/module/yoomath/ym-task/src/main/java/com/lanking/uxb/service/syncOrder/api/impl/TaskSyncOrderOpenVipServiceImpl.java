/**
 * 
 */
package com.lanking.uxb.service.syncOrder.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.order.ThirdPaymentMethod;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.mall.api.MemberPackageOrderSettlementService;
import com.lanking.uxb.service.payment.alipay.client.AlipayClient;
import com.lanking.uxb.service.payment.alipay.client.AlipayConfig;
import com.lanking.uxb.service.payment.cache.CallbackOrderCache;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.syncOrder.api.TaskMemberPackageOrderService;
import com.lanking.uxb.service.syncOrder.api.TaskMemberPackageService;
import com.lanking.uxb.service.syncOrder.api.TaskSyncOrderOpenVipService;
import com.lanking.uxb.service.syncOrder.api.TaskUserMemberService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.api.UserService;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
@Transactional(readOnly = true)
@Service
public class TaskSyncOrderOpenVipServiceImpl implements TaskSyncOrderOpenVipService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AlipayClient alipayClient;
	@Autowired
	private WXPaymentService wxPaymentService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private CallbackOrderCache callbackOrderCache;
	@Autowired
	private TaskMemberPackageOrderService taskMemberPackageOrderService;
	@Autowired
	private TaskMemberPackageService taskMemberPackageService;
	@Autowired
	private TaskUserMemberService userMemberService;
	@Autowired
	private UserService userService;
	@Autowired
	private MemberPackageOrderSettlementService memberPackageOrderSettlementService;

	@Transactional
	@Override
	public void checkPayCode(MemberPackageOrder order) {
		// 判定平台
		// paymentPlatformCode ==1为微信
		if (order.getPaymentPlatformCode() == 1) {
			String appid = "";
			if (order.getThirdPaymentMethod() == ThirdPaymentMethod.WX_APP) {
				if (userService.get(order.getUserId()).getUserType() == UserType.TEACHER) {
					appid = Env.getString("weixin.pay.mobile.teacher.appid");
				} else {
					appid = Env.getString("weixin.pay.mobile.student.appid");
				}
			}
			if (order.getThirdPaymentMethod() == ThirdPaymentMethod.WX_NATIVE) {
				appid = Env.getString("weixin.pay.appid");
			}
			if (appid == "") {
				return;
			}

			try {
				com.lanking.uxb.service.payment.weixin.response.OrderQueryResult result = wxPaymentService
						.orderQuery(appid, null, String.valueOf(order.getId()));
				final String return_code = result.getReturnCode(); // 返回状态
				String trade_state = result.getTradeState(); // 交易状态
				final String timeEnd = result.getTimeEnd(); // 交易付款时间
				final String transactionId = result.getTransactionId(); // 微信支付订单号
				if ("SUCCESS".equalsIgnoreCase(return_code) && ("SUCCESS".equalsIgnoreCase(trade_state))) {
					// 判断订单是否已被处理中
					if (callbackOrderCache.hasProcessing(OrderBusinessSource.USER_MEMBER, order.getId())) {
						return;
					} else {
						callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.USER_MEMBER, order.getId());
					}
					final SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
					Date payTime = null;
					try {
						payTime = formate.parse(timeEnd);
					} catch (ParseException e) {
						payTime = new Date();
					}
					// 支付订单
					taskMemberPackageOrderService.updatePaymentCallback(order.getId(), null, transactionId, payTime);

					// 完成订单
					order = taskMemberPackageOrderService.updateOrderStatus(order.getId(), null,
							MemberPackageOrderStatus.COMPLETE);

					// 处理会员数据
					MemberPackage memberPackage = taskMemberPackageService.get(order.getMemberPackageId());
					userMemberService.createOrRenew(order.getUserId(), payTime, memberPackage, order.getId(), null);

					callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, order.getId());

					// 用户会员开通动作行为
					userActionService.asyncAction(UserAction.OPEN_VIP, order.getUserId(), null);

					// 统计渠道财务数据
					memberPackageOrderSettlementService.createByOrder(order, 1);
				}
			} catch (Exception e) {
				logger.error("查询微信订单态出错！", e);
				callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, order.getId());
				return;
			}
		}
		// paymentPlatformCode ==2为支付宝
		if (order.getPaymentPlatformCode() == 2) {
			try {
				com.lanking.uxb.service.payment.alipay.response.OrderQueryResult result = alipayClient
						.orderQuery(AlipayConfig.partner, String.valueOf(order.getId()), null);
				final String isSuccess = result.getIsSuccess(); // 返回状态
				if ("F".equals(isSuccess)) {
					return;
				}
				final String trade_status = result.getResponse().getTrade().getTrade_status(); // 返回状态
				final String gmt_payment = result.getResponse().getTrade().getGmt_payment(); // 交易付款时间
				final String trade_no = result.getResponse().getTrade().getTrade_no(); // 支付宝流水号
				if ("T".equals(isSuccess) && "TRADE_SUCCESS".equals(trade_status)) {

					// 判断订单是否已被处理中
					if (callbackOrderCache.hasProcessing(OrderBusinessSource.USER_MEMBER, order.getId())) {
						return;
					} else {
						callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.USER_MEMBER, order.getId());
					}
					final SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
					Date payTime = null;
					try {
						payTime = formate.parse(gmt_payment);
					} catch (ParseException e) {
						payTime = new Date();
					}
					// 支付订单
					taskMemberPackageOrderService.updatePaymentCallback(order.getId(), null, trade_no, payTime);

					// 完成订单
					order = taskMemberPackageOrderService.updateOrderStatus(order.getId(), null,
							MemberPackageOrderStatus.COMPLETE);

					// 处理会员数据
					MemberPackage memberPackage = taskMemberPackageService.get(order.getMemberPackageId());
					userMemberService.createOrRenew(order.getUserId(), payTime, memberPackage, order.getId(), null);

					callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, order.getId());

					// 用户会员开通动作行为
					userActionService.asyncAction(UserAction.OPEN_VIP, order.getUserId(), null);

					// 统计渠道财务数据
					memberPackageOrderSettlementService.createByOrder(order, 1);
				}

			} catch (Exception e) {
				logger.error("查询支付宝订单状态出错！", e);
				// 订单处理完成
				callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, order.getId());
				// 订单查询异常
				return;
			}
		}
	}
}
