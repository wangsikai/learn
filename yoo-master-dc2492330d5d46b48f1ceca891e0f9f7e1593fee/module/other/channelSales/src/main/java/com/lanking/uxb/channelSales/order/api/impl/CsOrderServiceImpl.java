package com.lanking.uxb.channelSales.order.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderType;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageService;
import com.lanking.uxb.channelSales.openmember.api.CsMemberPackageOrderService;
import com.lanking.uxb.channelSales.openmember.api.CsUserMemberService;
import com.lanking.uxb.channelSales.order.api.CsOrderService;
import com.lanking.uxb.channelSales.order.form.OrderQueryForm;
import com.lanking.uxb.service.payment.alipay.client.AlipayClient;
import com.lanking.uxb.service.payment.alipay.client.AlipayConfig;
import com.lanking.uxb.service.payment.cache.CallbackOrderCache;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.api.UserService;

import httl.util.StringUtils;

/**
 * @author zemin.song
 * @since 2.5.0
 */
@Service
@Transactional(readOnly = true)
public class CsOrderServiceImpl implements CsOrderService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	@Qualifier("MemberPackageOrderRepo")
	private Repo<MemberPackageOrder, Integer> repo;
	@Autowired
	private AlipayClient alipayClient;
	@Autowired
	private WXPaymentService wxPaymentService;
	@Autowired
	private CsMemberPackageOrderService csMemberPackageOrderService;
	@Autowired
	private CallbackOrderCache callbackOrderCache;
	@Autowired
	private CsMemberPackageService csMemberPackageService;
	@Autowired
	private CsUserMemberService csUserMemberService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private UserService userService;

	@Override
	public Page<Map> memberPackageOrderQuery(OrderQueryForm query, Pageable p) {
		// 状态为完成
		Params params = Params.param("type", query.getType().getValue());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -2);
		Date date = cal.getTime();
		params.put("qualifier", date);
		if (null != query.getStatus()) {
			params.put("status", query.getStatus().getValue());
			params.put("allStatus", false);
		} else {
			params.put("status", -1);
			params.put("allStatus", true);
		}
		// 订单号
		if (StringUtils.isNotEmpty(query.getOrderCode())) {
			params.put("orderCode", query.getOrderCode());
		}
		// 支付单号
		if (StringUtils.isNotEmpty(query.getPayCode())) {
			params.put("payCode", query.getPayCode());
		}
		// 渠道
		if (null != query.getChannelCode()) {
			params.put("channelCode", query.getChannelCode());
		}
		if (StringUtils.isNotEmpty(query.getChannelName())) {
			params.put("channelName", "%" + query.getChannelName() + "%");
		}
		// 账户名字
		if (StringUtils.isNotBlank(query.getAccountName())) {
			if (query.getType() == MemberPackageOrderType.USER) {
				params.put("queryNameType", 1);
			} else {
				params.put("queryNameType", 2);
			}
			params.put("accountName", "%" + query.getAccountName() + "%");
		}
		if (StringUtils.isNotEmpty(query.getStartDate())) {
			params.put("startAt", query.getStartDate());
		}
		if (StringUtils.isNotEmpty(query.getEndDate())) {
			params.put("endAt", query.getEndDate());
		}
		if (null != query.getUserType()) {
			params.put("userType", query.getUserType().getValue());
		}
		if (null != query.getMemberType()) {
			params.put("memberType", query.getMemberType().getValue());
		}
		// 支付方式
		if (null != query.getPayMod()) {
			params.put("payMod", query.getPayMod().getValue());
		} else {
			params.put("payMod", 0);
		}
		// 支付平台
		if (null != query.getPaymentPlatformCode()) {
			params.put("paymentPlatformCode", query.getPaymentPlatformCode());
		} else {
			params.put("paymentPlatformCode", 0);
		}
		// 支付方式(微信)
		if (null != query.getThirdPaymentMethod()) {
			params.put("thirdPaymentMethod", query.getThirdPaymentMethod().getValue());
		} else {
			params.put("thirdPaymentMethod", 0);
		}
		return repo.find("$csSearchUserMemberOrders", params).fetch(p, Map.class);
	}

	@Override
	public Page<Map> fallibleQuestionPrintOrderQuery(OrderQueryForm query, Pageable p) {
		// 状态为完成
		Params params = Params.param("", "");
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("key", "%" + query.getKey() + "%");
		}
		if (null != query.getStartDate()) {
			params.put("startAt", query.getStartDate());
		}
		if (null != query.getEndDate()) {
			params.put("endAt", query.getEndDate());
		}
		return repo.find("$csSearchFallibleQuestionPrintOrders", params).fetch(p, Map.class);
	}

	@Override
	public Page<Map> resourcesGoodsOrderQuery(OrderQueryForm query, Pageable p) {
		Params params = Params.param("status", GoodsOrderStatus.COMPLETE.getValue());
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("key", "%" + query.getKey() + "%");
		}
		if (null != query.getStartDate()) {
			params.put("startAt", query.getStartDate());
		}
		if (null != query.getEndDate()) {
			params.put("endAt", query.getEndDate());
		}
		if (null != query.getPayMod()) {
			params.put("paymod", query.getPayMod().getValue());
		}
		return repo.find("$csSearchResourcesGoodsOrders", params).fetch(p, Map.class);
	}

	@Override
	public List<String> searchUserByOrder(Long orderId) {
		Params params = Params.param("orderId", orderId);
		return repo.find("$csSearchUserByOrder", params).list(String.class);
	}

	@Override
	public List<Map> searchToOneOrderUserByOrderId(List<Long> orderIds) {
		Params params = Params.param("orderId", orderIds);
		return repo.find("$csSearchToOneOrderUser", params).list(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryMemberCardList(OrderQueryForm query, Pageable pageable) {
		Params params = Params.param("status", GoodsOrderStatus.COMPLETE.getValue());
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("keystr", "%" + query.getKey() + "%");
		}
		if (null != query.getStartDate()) {
			params.put("startAt", query.getStartDate());
		}
		if (null != query.getEndDate()) {
			params.put("endAt", query.getEndDate());
		}
		return repo.find("$queryMemberCardList", params).fetch(pageable, Map.class);
	}

	@Transactional
	@Override
	public void checkPayCode(Map map) {
		Long orderId = Long.valueOf(map.get("id").toString());
		// 判定平台
		// paymentPlatformCode ==1为微信
		if (map.get("payment_platform_code").toString().equals("1")) {
			String appid = "";
			if (map.get("third_payment_method").toString().equals("3")) {
				if (userService.get(Long.valueOf(map.get("user_id").toString())).getUserType() == UserType.TEACHER) {
					appid = Env.getString("weixin.pay.mobile.teacher.appid");
				} else {
					appid = Env.getString("weixin.pay.mobile.student.appid");
				}
			}
			if (map.get("third_payment_method").toString().equals("2")) {
				appid = Env.getString("weixin.pay.appid");
			}
			try {
				com.lanking.uxb.service.payment.weixin.response.OrderQueryResult result = wxPaymentService
						.orderQuery(appid, null, String.valueOf(orderId));
				final String return_code = result.getReturnCode(); // 返回状态
				String trade_state = result.getTradeState(); // 交易状态
				final String timeEnd = result.getTimeEnd(); // 交易付款时间
				final String transactionId = result.getTransactionId(); // 微信支付订单号
				if ("SUCCESS".equalsIgnoreCase(return_code) && ("SUCCESS".equalsIgnoreCase(trade_state))) {
					// 判断订单是否已被处理中
					if (callbackOrderCache.hasProcessing(OrderBusinessSource.USER_MEMBER, orderId)) {
						return;
					} else {
						callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);
					}
					final SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
					Date payTime = null;
					try {
						payTime = formate.parse(timeEnd);
					} catch (ParseException e) {
						payTime = new Date();
					}
					// 支付订单
					csMemberPackageOrderService.updatePaymentCallback(orderId, null, transactionId, payTime);

					// 完成订单
					MemberPackageOrder order = csMemberPackageOrderService.updateOrderStatus(orderId, null,
							MemberPackageOrderStatus.COMPLETE);
					map.put("status", MemberPackageOrderStatus.COMPLETE.getValue());

					// 处理会员数据
					MemberPackage memberPackage = csMemberPackageService.get(order.getMemberPackageId());
					csUserMemberService.createOrRenew(order.getUserId(), payTime, memberPackage, order.getId(), null);

					callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, order.getId());

					// 用户会员开通动作行为
					userActionService.asyncAction(UserAction.OPEN_VIP, order.getUserId(), null);
				}
			} catch (Exception e) {
				logger.error("查询微信订单态出错！", e);
				callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);
				return;
			}
		}
		// paymentPlatformCode ==2为支付宝
		if (map.get("payment_platform_code").toString().equals("2")) {
			try {
				com.lanking.uxb.service.payment.alipay.response.OrderQueryResult result = alipayClient
						.orderQuery(AlipayConfig.partner, String.valueOf(orderId), null);
				final String isSuccess = result.getIsSuccess(); // 返回状态
				if ("F".equals(isSuccess)) {
					return;
				}
				final String trade_status = result.getResponse().getTrade().getTrade_status(); // 返回状态
				final String gmt_payment = result.getResponse().getTrade().getGmt_payment(); // 交易付款时间
				final String trade_no = result.getResponse().getTrade().getTrade_no(); // 支付宝流水号
				if ("T".equals(isSuccess) && "TRADE_SUCCESS".equals(trade_status)) {

					// 判断订单是否已被处理中
					if (callbackOrderCache.hasProcessing(OrderBusinessSource.USER_MEMBER, orderId)) {
						return;
					} else {
						callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);
					}
					final SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
					Date payTime = null;
					try {
						payTime = formate.parse(gmt_payment);
					} catch (ParseException e) {
						payTime = new Date();
					}
					// 支付订单
					csMemberPackageOrderService.updatePaymentCallback(orderId, null, trade_no, payTime);

					// 完成订单
					MemberPackageOrder order = csMemberPackageOrderService.updateOrderStatus(orderId, null,
							MemberPackageOrderStatus.COMPLETE);
					map.put("status", MemberPackageOrderStatus.COMPLETE.getValue());

					// 处理会员数据
					MemberPackage memberPackage = csMemberPackageService.get(order.getMemberPackageId());
					csUserMemberService.createOrRenew(order.getUserId(), payTime, memberPackage, order.getId(), null);

					callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, order.getId());

					// 用户会员开通动作行为
					userActionService.asyncAction(UserAction.OPEN_VIP, order.getUserId(), null);

				}

			} catch (Exception e) {
				logger.error("查询支付宝订单状态出错！", e);
				// 订单处理完成
				callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);
				// 订单查询异常
				return;
			}
		}
	}
}
