package com.lanking.uxb.service.member.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.MemberPackageCardStatus;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.PaymentPlatform;
import com.lanking.cloud.domain.yoo.order.ThirdPaymentMethod;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.RSACoder;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.mall.api.MemberPackageCardService;
import com.lanking.uxb.service.mall.api.MemberPackageOrderService;
import com.lanking.uxb.service.mall.api.MemberPackageOrderSettlementService;
import com.lanking.uxb.service.mall.api.MemberPackagePaymentService;
import com.lanking.uxb.service.mall.api.MemberPackageService;
import com.lanking.uxb.service.mall.convert.MemberPackageConvert;
import com.lanking.uxb.service.member.api.ZyMMemberOrderService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.alipay.client.AlipayClient;
import com.lanking.uxb.service.payment.alipay.client.AlipayConfig;
import com.lanking.uxb.service.payment.alipay.client.AlipayCore;
import com.lanking.uxb.service.payment.cache.CallbackOrderCache;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.payment.weixin.client.WXPaymentClient;
import com.lanking.uxb.service.payment.weixin.response.OrderQueryResult;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserMemberConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.user.value.VUserMember;
import com.tencent.common.Signature;

/**
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年10月27日
 */
@RestController
@RequestMapping("zy/m/member")
public class ZyMMemberController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberPackageService memberPackageService;
	@Autowired
	private MemberPackageConvert memberPackageConvert;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private UserMemberConvert userMemberConvert;
	@Autowired
	private MemberPackageOrderService memberPackageOrderService;
	@Autowired
	private MemberPackageCardService memberPackageCardService;
	@Autowired
	private WXPaymentService wxPaymentService;
	@Autowired
	private CallbackOrderCache callbackOrderCache;
	@Autowired
	private AlipayClient alipayClient;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private MemberPackageOrderSettlementService memberPackageOrderSettlementService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private ZyMMemberOrderService memberOrderService;
	@Autowired
	private MemberPackagePaymentService memberPackagePaymentService;
	@Autowired
	private WXPaymentClient wxPaymentClient;

	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

	@MasterSlaveDataSource(type = "MS")
	@MemberAllowed
	@RequestMapping(value = "memberPackages")
	public Value memberPackages() {
		ValueMap vm = ValueMap.value();
		// 会员信息
		UserMember userMember = userMemberService.$findByUserId(Security.getUserId());
		vm.put("userMember", userMemberConvert.to(userMember));
		List<MemberPackage> memberPackages = new ArrayList<MemberPackage>();
		if (Security.getUserType() == UserType.TEACHER) {
			memberPackages = memberPackageService.queryMemberPackage(UserType.TEACHER, MemberType.VIP);
		} else {
			User user = accountService.getUserByUserId(Security.getUserId());
			VUser vuser = userConvert.to(user);
			Integer channelCode = user.getUserChannelCode() == null ? UserChannel.YOOMATH : user.getUserChannelCode();
			// 不是渠道用户，自主注册用户
			if (channelCode == UserChannel.YOOMATH) {
				memberPackages = memberPackageService.queryMemberPackageByAutoRegister(UserType.STUDENT,
						MemberType.VIP);
			} else {
				memberPackages = memberPackageService.queryMemberPackageByGroup(UserType.STUDENT, MemberType.VIP,
						vuser.getSchool() == null ? null : vuser.getSchool().getId(), channelCode);
			}
		}
		vm.put("memberPackages", memberPackageConvert.to(memberPackages));
		return new Value(vm);
	}

	/**
	 * 创建微信会员订单.
	 * 
	 * @param memberPackageID
	 *            会员套餐ID
	 * @param isContinue
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@MemberAllowed
	@RequestMapping(value = "createWXOrder")
	public Value createWXOrder(Long memberPackageID, Boolean isContinue, HttpServletRequest request,
			HttpServletResponse response) {
		// return new Value(new
		// YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_WX_NOT_SUPPORT));

		long userID = Security.getUserId();
		int paymentPlatformCode = PaymentPlatform.WECHAT.getValue(); // 微信支付

		// 校验
		isContinue = isContinue == null ? false : isContinue;
		Value value = this.commonOrderCheck(memberPackageID, paymentPlatformCode, isContinue);
		if (value != null) {
			return value;
		}

		String appid = "";
		String partnerid = "";
		if (Security.getUserType() == UserType.TEACHER) {
			appid = Env.getString("weixin.pay.mobile.teacher.appid");
			partnerid = Env.getString("weixin.pay.mobile.teacher.mchID");
		} else {
			appid = Env.getString("weixin.pay.mobile.student.appid");
			partnerid = Env.getString("weixin.pay.mobile.student.mchID");
		}

		Map<String, Object> map = new HashMap<String, Object>(8);
		try {
			// 本地订单
			MemberPackageOrder memberPackageOrder = memberPackageOrderService.createOrder(userID, memberPackageID,
					MemberType.VIP, PayMode.ONLINE, paymentPlatformCode, ThirdPaymentMethod.WX_APP, "", null);

			// 微信预支付订单
			UnifiedPayResult result = memberPackagePaymentService.getWXQRCodeImage(appid, memberPackageID,
					memberPackageOrder.getId(), OrderPayBusinessSpace.MEMBER_PACKAGE, "APP", null, request, response);
			if (result == null) {
				// 订单生成失败
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_FAILURE));
			}

			map.put("appid", appid); // 应用APPID
			map.put("partnerid", partnerid); // 商户号
			map.put("prepayid", result.getPrepayId()); // 微信预支付ID
			map.put("package", "Sign=WXPay");
			map.put("noncestr", result.getNonceStrRequest()); // 第一步产生的随机字串
			map.put("timestamp", (int) (System.currentTimeMillis() / 1000)); // 时间戳，精确到秒

			String sign = Signature.getSign(map, wxPaymentClient.getConfigure(appid)); // 签名
			map.put("sign", sign); // 签名
			map.put("orderId", memberPackageOrder.getId()); // 本地订单ID

			return new Value(map);
		} catch (YoomathMobileException e) {
			logger.error("客户端微信订单创建失败", e);
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_FAILURE));
		}
	}

	/**
	 * 创建支付宝会员订单.
	 * 
	 * @param memberPackageID
	 *            会员套餐ID
	 * @param isContinue
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@MemberAllowed
	@RequestMapping(value = "createAlipayOrder")
	public Value createAlipayOrder(Long memberPackageID, Boolean isContinue, HttpServletRequest request,
			HttpServletResponse response) {
		long userID = Security.getUserId();
		int paymentPlatformCode = PaymentPlatform.ALIPAY.getValue(); // 支付宝支付

		// 校验
		Value value = this.commonOrderCheck(memberPackageID, paymentPlatformCode, isContinue);
		if (value != null) {
			return value;
		}

		Map<String, String> map = new HashMap<String, String>(8);
		try {
			// 本地订单
			MemberPackage memberPackage = memberPackageService.get(memberPackageID);
			MemberPackageOrder memberPackageOrder = memberPackageOrderService.createOrder(userID, memberPackageID,
					MemberType.VIP, PayMode.ONLINE, paymentPlatformCode, ThirdPaymentMethod.DEFAULT, "", null);

			String name = "会员套餐：";
			if (memberPackage.getMonth() % 12 == 0) {
				name += memberPackage.getMonth() / 12 + "年";
			} else {
				name += memberPackage.getMonth() + "个月";
			}
			String totalPrice = String.format("%.2f", memberPackageOrder.getTotalPrice().doubleValue());

			// ..........移动支付............
			map.put("service", "mobile.securitypay.pay");
			map.put("partner", AlipayConfig.partner);
			map.put("_input_charset", "UTF-8");
			map.put("notify_url", AlipayConfig.notify_url);
			map.put("out_trade_no", String.valueOf(memberPackageOrder.getId()));
			map.put("subject", name);
			map.put("payment_type", "1");
			map.put("it_b_pay", "2h"); // 2h限制
			map.put("seller_id", AlipayConfig.seller_id);
			map.put("total_fee", totalPrice);
			map.put("extra_common_param", String.valueOf(OrderPayBusinessSpace.MEMBER_PACKAGE.getValue())); // 标记本地业务
			map.put("body", name);

			AlipayCore.paraFilter(map);
			String sign = alipayClient.buildRequestMysign(map);
			map.put("sign_type", "RSA");
			map.put("sign", sign);
			map.put("encodeStr", AlipayCore.createLinkEncodeString(map));
			Map<String, String> returnMap = new HashMap<String, String>(6);
			returnMap.put("encodeStr", map.get("encodeStr"));
			returnMap.put("sign", map.get("sign"));
			returnMap.put("orderId", map.get("out_trade_no"));
			returnMap.put("total_fee", totalPrice);

			return new Value(returnMap);
		} catch (Exception e) {
			logger.error("客户端支付宝订单创建失败", e);
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_FAILURE));
		}
	}

	/**
	 * 创建支付宝会员订单（H5支付）.
	 * 
	 * @param memberPackageId
	 *            会员套餐ID
	 * @param isContinue
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@MemberAllowed
	@RequestMapping(value = "createH5AlipayOrder")
	public Value createH5AlipayOrder(Long memberPackageId, Boolean isContinue, HttpServletRequest request,
			HttpServletResponse response) {
		long userID = Security.getUserId();
		int paymentPlatformCode = PaymentPlatform.ALIPAY.getValue(); // 支付宝支付

		// 校验
		Value value = this.commonOrderCheck(memberPackageId, paymentPlatformCode, isContinue);
		if (value != null) {
			return value;
		}

		Map<String, Object> returnMap = new HashMap<String, Object>(2);
		try {
			// 本地订单
			MemberPackage memberPackage = memberPackageService.get(memberPackageId);
			if (memberPackage == null) {
				return new Value(new EntityNotFoundException());
			}
			MemberPackageOrder memberPackageOrder = memberPackageOrderService.createOrder(userID, memberPackageId,
					MemberType.VIP, PayMode.ONLINE, paymentPlatformCode, ThirdPaymentMethod.DEFAULT, "", null);

			// ..........H5支付............
			String sign = RSACoder.encryptBASE64(memberPackageOrder.getId().toString().getBytes()).replaceAll("\\s", "")
					.trim();
			returnMap.put("orderId", memberPackageOrder.getId());
			returnMap.put("sign", sign);

			return new Value(returnMap);
		} catch (Exception e) {
			logger.error("H5支付宝订单创建失败", e);
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_FAILURE));
		}
	}

	/**
	 * 共用订单校验.
	 * 
	 * @param memberPackageID
	 * @param paymentPlatformCode
	 * @param isContinue
	 * @return
	 */
	private Value commonOrderCheck(Long memberPackageId, Integer paymentPlatformCode, Boolean isContinue) {
		if (null == memberPackageId || paymentPlatformCode == null) {
			return new Value(new MissingArgumentException());
		}
		MemberPackage memberPackage = memberPackageService.get(memberPackageId);
		if (memberPackage == null || memberPackage.getStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_UNPUBLISH));
		}

		long userID = Security.getUserId();
		MemberType memberType = SecurityContext.getMemberType();
		UserMember userMember = userMemberService.findByUserId(userID);
		if (memberType == MemberType.SCHOOL_VIP && memberPackage.getMemberType() == MemberType.VIP) {
			// 已是校级会员无法续费开通普通会员
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_NOT_MATCH));
		}

		// 非续费需要控制套餐购买情况，避免重复购买
		if (userMember != null && (isContinue == null || !isContinue)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			try {
				Date today = format.parse(format.format(new Date()));
				Date start = format.parse(format.format(userMember.getStartAt()));
				Date end = format.parse(format.format(userMember.getEndAt()));
				if (start.compareTo(today) <= 0 && end.compareTo(today) >= 0) {
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_BUY_DUPLICATE));
				}
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
		}

		return null;
	}

	/**
	 * 查询订单状态.
	 * 
	 * @param orderId
	 *            本地订单ID
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@MemberAllowed
	@RequestMapping(value = "queryOrderStatus")
	public Value queryOrderStatus(final Long orderId, Boolean payflag) {
		if (null == orderId) {
			return new Value(new MissingArgumentException());
		}

		MemberPackageOrder order = memberPackageOrderService.get(orderId);
		if (payflag == null) {
			long userID = Security.getUserId();
			if (order == null || order.getUserId() != userID) {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_NOTFOUND));
			}
		}

		// 若查询本地订单仍为未支付状态，需要主动调用一次支付平台查询
		if (order.getStatus() == MemberPackageOrderStatus.NOT_PAY && order.getPaymentPlatformCode() != null) {
			String appid = "";
			if (Security.getUserType() == UserType.TEACHER) {
				appid = Env.getString("weixin.pay.mobile.teacher.appid");
			} else {
				appid = Env.getString("weixin.pay.mobile.student.appid");
			}

			if (order.getPaymentPlatformCode() == 1) {
				// 微信订单
				if (payflag != null && payflag) {
					appid = Env.getString("weixin.appid.zuoye");
				}
				return this.queryWXOrder(appid, orderId);
			} else if (order.getPaymentPlatformCode() == 2) {
				// 支付宝订单
				return this.queryAlipayOrder(orderId);
			}
		}
		Map<String, Object> returnMap = Maps.newHashMap();
		returnMap.put("status", order.getStatus());
		return new Value(returnMap);
	}

	/**
	 * 查询微信订单.
	 * 
	 * @param appid
	 * @param orderId
	 * @return
	 */
	public Value queryWXOrder(String appid, final long orderId) {
		try {
			Map<String, Object> returnMap = Maps.newHashMap();
			OrderQueryResult result = wxPaymentService.orderQuery(appid, null, String.valueOf(orderId));
			if (result == null) {
				returnMap.put("status", MemberPackageOrderStatus.PROCESSING);
				return new Value(returnMap);
			}
			final String return_code = result.getReturnCode(); // 返回状态
			String trade_state = result.getTradeState(); // 交易状态
			final String timeEnd = result.getTimeEnd(); // 交易付款时间
			final String transactionId = result.getTransactionId(); // 微信支付订单号

			MemberPackageOrder order = memberPackageOrderService.get(orderId);
			if (order.getStatus() == MemberPackageOrderStatus.COMPLETE
					|| order.getStatus() == MemberPackageOrderStatus.FAIL) {
				returnMap.put("status", order.getStatus());
				return new Value(returnMap);
			}

			// 交易成功
			if ("SUCCESS".equalsIgnoreCase(return_code) && ("SUCCESS".equalsIgnoreCase(trade_state))) {

				// 判断订单是否已被处理中
				if (callbackOrderCache.hasProcessing(OrderBusinessSource.USER_MEMBER, orderId)) {
					returnMap.put("status", MemberPackageOrderStatus.COMPLETE);
					return new Value(returnMap);
				} else {
					callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);
				}
				logger.info("[会员套餐] 移动端微信购买, orderID = " + orderId);
				final SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
				// fixedThreadPool.execute(new Runnable() {
				// @Override
				// public void run() {
				Date payTime = null;
				try {
					payTime = formate.parse(timeEnd);
				} catch (ParseException e) {
					payTime = new Date();
					logger.error(e.getMessage(), e);
				}

				// 支付订单
				memberPackageOrderService.updatePaymentCallback(orderId, null, transactionId, payTime);

				// 完成订单
				order = memberPackageOrderService.updateOrderStatus(orderId, null, MemberPackageOrderStatus.COMPLETE);

				// 处理会员数据
				MemberPackage memberPackage = memberPackageService.get(order.getMemberPackageId());
				userMemberService.createOrRenew(order.getUserId(), payTime, memberPackage, orderId, null);

				callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);

				// 用户会员开通动作行为
				userActionService.asyncAction(UserAction.OPEN_VIP, order.getUserId(), null);

				// 处理渠道统计
				memberPackageOrderSettlementService.createByOrder(order, 1);
				// }
				// });
				returnMap.put("status", MemberPackageOrderStatus.COMPLETE);
				return new Value(returnMap);
			} else if ("REVOKED".equalsIgnoreCase(trade_state) || "PAYERROR".equalsIgnoreCase(trade_state)
					|| "CLOSED".equalsIgnoreCase(trade_state)) {
				// 交易失败
				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						memberPackageOrderService.updateOrderStatus(orderId, null, MemberPackageOrderStatus.FAIL);
					}
				});
				returnMap.put("status", MemberPackageOrderStatus.FAIL);
				return new Value(returnMap);
			} else if ("NOTPAY".equalsIgnoreCase(trade_state)) {
				returnMap.put("status", MemberPackageOrderStatus.NOT_PAY);
				return new Value(returnMap);
			} else {
				returnMap.put("status", MemberPackageOrderStatus.FAIL);
				return new Value(returnMap);
			}
		} catch (Exception e) {
			logger.error("查询微信订单状态出错！", e);
			// 订单处理完成
			callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);
			return new Value(new ServerException());
		}
	}

	/**
	 * 查询支付宝订单.
	 * 
	 * @param orderId
	 * @return
	 */
	public Value queryAlipayOrder(final long orderId) {
		try {
			Map<String, Object> returnMap = Maps.newHashMap();

			com.lanking.uxb.service.payment.alipay.response.OrderQueryResult result = alipayClient
					.orderQuery(AlipayConfig.partner, String.valueOf(orderId), null);
			if (result == null) {
				returnMap.put("status", MemberPackageOrderStatus.PROCESSING);
				return new Value(returnMap);
			}

			final String isSuccess = result.getIsSuccess(); // 返回状态
			if (!"T".equals(isSuccess)) {
				// logger.error("支付宝订单查询失败！" + result.getError());
				returnMap.put("status", MemberPackageOrderStatus.PROCESSING);
				return new Value(returnMap);
			}

			final String trade_status = result.getResponse().getTrade().getTrade_status(); // 返回状态
			final String gmt_payment = result.getResponse().getTrade().getGmt_payment(); // 交易付款时间
			final String trade_no = result.getResponse().getTrade().getTrade_no(); // 支付宝流水号

			MemberPackageOrder order = memberPackageOrderService.get(orderId);
			if (order.getStatus() == MemberPackageOrderStatus.COMPLETE
					|| order.getStatus() == MemberPackageOrderStatus.FAIL) {
				returnMap.put("status", order.getStatus());
				return new Value(returnMap);
			}

			if ("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)
					|| "WAIT_SELLER_SEND_GOODS".equals(trade_status)) {
				// 判断订单是否已被处理中
				if (callbackOrderCache.hasProcessing(OrderBusinessSource.USER_MEMBER, orderId)) {
					returnMap.put("status", MemberPackageOrderStatus.COMPLETE);
					return new Value(returnMap);
				} else {
					callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);
				}
				logger.info("[会员套餐] 移动端支付宝购买, orderID = " + orderId);
				// fixedThreadPool.execute(new Runnable() {
				// @Override
				// public void run() {
				Date payTime = null;
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					payTime = format.parse(gmt_payment);
				} catch (ParseException e) {
					payTime = new Date();
					logger.error(e.getMessage(), e);
				}

				// 支付订单
				memberPackageOrderService.updatePaymentCallback(orderId, null, trade_no, payTime);

				// 完成订单
				order = memberPackageOrderService.updateOrderStatus(orderId, null, MemberPackageOrderStatus.COMPLETE);

				// 处理会员数据
				MemberPackage memberPackage = memberPackageService.get(order.getMemberPackageId());
				userMemberService.createOrRenew(order.getUserId(), payTime, memberPackage, orderId, null);

				callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);

				// 用户会员开通动作行为
				userActionService.asyncAction(UserAction.OPEN_VIP, order.getUserId(), null);

				// 处理渠道统计
				memberPackageOrderSettlementService.createByOrder(order, 1);
				// }
				// });
				returnMap.put("status", MemberPackageOrderStatus.COMPLETE);
				return new Value(returnMap);
			} else if ("TRADE_CLOSED".equals(trade_status) || "TRADE_REFUSE".equals(trade_status)
					|| "TRADE_REFUSE_DEALING".equals(trade_status) || "TRADE_CANCEL".equals(trade_status)) {
				returnMap.put("status", MemberPackageOrderStatus.FAIL);
				return new Value(returnMap);
			} else {
				returnMap.put("status", MemberPackageOrderStatus.PROCESSING);
				return new Value(returnMap);
			}
		} catch (Exception e) {
			logger.error("查询支付宝订单状态出错！", e);
			// 订单处理完成
			callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, orderId);
			return new Value(new ServerException());
		}
	}

	/**
	 * 使用会员卡开通会员
	 *
	 * @param cardNo
	 *            卡号
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "card", method = { RequestMethod.GET, RequestMethod.POST })
	public Value card(String cardNo) {
		if (StringUtils.isBlank(cardNo) || cardNo.length() != 16) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMCARD_ILLEGAL));
		}

		String accountId = String.valueOf(Security.getAccountId());
		long errorCount = accountCacheService.getMemberpackageCardActiveTime(accountId);
		if (errorCount >= 5) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMCARD_FREQUENT));
		}

		MemberPackageCard memberPackageCard = memberPackageCardService.get(cardNo);
		if (memberPackageCard == null) {
			accountCacheService.setMemberpackageCardActiveTime(accountId, errorCount + 1, 10);
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMCARD_ILLEGAL));
		} else if (memberPackageCard.getStatus() == MemberPackageCardStatus.DELETE) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMCARD_USED));
		} else if (memberPackageCard.getStatus() == MemberPackageCardStatus.DISABLE) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMCARD_FREEZE));
		} else if (memberPackageCard.getUserType() != Security.getUserType()) {
			// 3.0.2 -> 会员卡卡号用户类型不一致
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMCARD_USERTYPE_ERROR));
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(memberPackageCard.getEndAt());
		cal.add(Calendar.DAY_OF_YEAR, 1);
		if (cal.getTime().getTime() < System.currentTimeMillis()) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMCARD_EXPIRE));
		}

		memberPackageOrderService.createOrder(Security.getUserId(), null, MemberType.VIP, PayMode.VIRTUAL_CARD, null,
				ThirdPaymentMethod.DEFAULT, null, memberPackageCard);

		UserMember userMember = userMemberService.findByUserId(Security.getUserId());
		VUserMember vum = userMemberConvert.to(userMember);

		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("userMember", vum);
		retMap.put("months", memberPackageCard.getMonth());

		return new Value(retMap);

	}

	/**
	 * 创建代付订单.
	 * 
	 * @since 学生客户端 v1.4.2
	 * @param memberPackageID
	 *            套餐ID
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "createPayOrder", method = { RequestMethod.GET, RequestMethod.POST })
	public Value createPayOrder(Long memberPackageID) {
		if (memberPackageID == null) {
			return new Value(new MissingArgumentException());
		}
		MemberPackage memberPackage = memberPackageService.get(memberPackageID);
		if (memberPackage == null || memberPackage.getStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_UNPUBLISH));
		}
		MemberType memberType = SecurityContext.getMemberType();
		if (memberType == MemberType.SCHOOL_VIP && memberPackage.getMemberType() == MemberType.VIP) {
			// 已是校级会员无法续费开通普通会员
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_NOT_MATCH));
		}

		long userID = Security.getUserId();
		Map<String, Object> map = new HashMap<String, Object>(8);
		try {
			// 本地订单
			MemberPackageOrder memberPackageOrder = memberPackageOrderService.createOrder(userID, memberPackageID,
					MemberType.VIP, PayMode.ONLINE, null, null, "", null);
			map.put("orderId", memberPackageOrder.getId()); // 本地订单ID
			Parameter parameter = parameterService.get(Product.YOOMATH, "stu.pay-by-other.h5.url",
					String.valueOf(memberPackageOrder.getId()));
			map.put("payH5Url", parameter == null ? "" : parameter.getValue());

			return new Value(map);
		} catch (YoomathMobileException e) {
			logger.error("客户端微信订单创建失败", e);
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_FAILURE));
		}
	}

	/**
	 * 获取IAP内置套餐列表详细数据.
	 * <p>
	 * 该方式仅限于自主注册用户使用。
	 * </p>
	 * 
	 * @since 学生端 v1.4.7 2017-09-12
	 * @param memberPackageIds
	 *            套餐ID集合，英文逗号间隔
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@MemberAllowed
	@RequestMapping(value = "queryIAPMemeberPackages")
	public Value queryIAPMemeberPackages(String memberPackageIds) {
		// if (StringUtils.isBlank(memberPackageIds)) {
		// return new Value(new MissingArgumentException());
		// }
		ValueMap vm = ValueMap.value();
		User user = accountService.getUserByUserId(Security.getUserId());
		if (user.getUserChannelCode() != UserChannel.YOOMATH) {
			// 非自主注册的用户应该走H5支付通道
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IOSIAP_USER_ERROR));
		}

		// 当前用户会员信息
		UserMember userMember = userMemberService.$findByUserId(user.getId());
		vm.put("userMember", userMemberConvert.to(userMember));

		List<MemberPackage> memberPackages = new ArrayList<MemberPackage>();
		if (Security.getUserType() == UserType.TEACHER) {
			memberPackages = memberPackageService.queryMemberPackage(UserType.TEACHER, MemberType.VIP);
		} else {
			memberPackages = memberPackageService.queryMemberPackageByAutoRegister(UserType.STUDENT, MemberType.VIP);
		}

		// 过滤套餐
		if (StringUtils.isNotBlank(memberPackageIds)) {
			for (int i = memberPackages.size() - 1; i >= 0; i--) {
				if (memberPackages.get(i).getId().toString().indexOf(memberPackageIds) == -1) {
					memberPackages.remove(i);
				}
			}
		}

		if (memberPackages.size() == 0) {
			// 已无套餐，苹果
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_UNPUBLISH));
		}

		vm.put("memberPackages", memberPackageConvert.to(memberPackages));

		return new Value(vm);
	}

	/**
	 * 生成苹果应用内支付订单.
	 * 
	 * @param memberPackageId
	 *            套餐ID
	 * @param isContinue
	 *            是否为续费
	 * @param request
	 * @param response
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@MemberAllowed
	@RequestMapping(value = "createIAPOrder")
	public Value createIAPOrder(Long memberPackageId, Boolean isContinue, HttpServletRequest request,
			HttpServletResponse response) {

		long userID = Security.getUserId();
		int paymentPlatformCode = PaymentPlatform.IAP.getValue(); // 苹果IAP支付

		User user = accountService.getUserByUserId(userID);
		if (user.getUserChannelCode() != UserChannel.YOOMATH) {
			// 非自主注册的用户应该走H5支付通道
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IOSIAP_USER_ERROR));
		}

		// 校验
		Value value = this.commonOrderCheck(memberPackageId, paymentPlatformCode, isContinue);
		if (value != null) {
			return value;
		}

		Map<String, Object> returnMap = new HashMap<String, Object>(6);
		try {
			// 本地订单
			MemberPackageOrder memberPackageOrder = memberPackageOrderService.createOrder(userID, memberPackageId,
					MemberType.VIP, PayMode.ONLINE, paymentPlatformCode, ThirdPaymentMethod.DEFAULT, "", null);

			returnMap.put("orderId", memberPackageOrder.getId());
		} catch (Exception e) {
			logger.error("客户端苹果IAP订单创建失败", e);
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_FAILURE));
		}

		return new Value(returnMap);
	}

	/**
	 * 更新苹果IAP订单.
	 * 
	 * @param orderId
	 *            订单ID
	 * @param status
	 *            订单支付状态
	 * @param receiptData
	 *            苹果验证凭据
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "updateIAPOrder")
	public Value updateIAPOrder(Long orderId, MemberPackageOrderStatus status, String receiptData) {
		if (orderId == null || status == null) {
			return new Value(new MissingArgumentException());
		}
		MemberPackageOrder memberPackageOrder = memberPackageOrderService.get(orderId);
		if (memberPackageOrder == null) {
			// 订单不存在
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_NOTFOUND));
		} else if (memberPackageOrder.getUserId() != Security.getUserId()) {
			// 没有权限
			return new Value(new NoPermissionException());
		} else if (memberPackageOrder.getStatus() != MemberPackageOrderStatus.NOT_PAY) {
			// 订单已被支付或处理过
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_MANAGED));
		} else if (memberPackageOrder.getPaymentPlatformCode() != PaymentPlatform.IAP.getValue()) {
			// 非苹果IAP支付
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_PLATFORM_ERROR));
		}

		try {
			memberOrderService.updateIAPOrder(memberPackageOrder, status, receiptData);
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}
}
