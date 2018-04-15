package com.lanking.uxb.service.mall.resource;

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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.MemberPackageCardStatus;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.order.PayMode;
import com.lanking.cloud.domain.yoo.order.ThirdPaymentMethod;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.mall.api.MemberPackageCardService;
import com.lanking.uxb.service.mall.api.MemberPackageOrderService;
import com.lanking.uxb.service.mall.api.MemberPackageOrderSettlementService;
import com.lanking.uxb.service.mall.api.MemberPackageService;
import com.lanking.uxb.service.mall.business.MemberPackageOrderPageSource;
import com.lanking.uxb.service.mall.convert.MemberPackageConvert;
import com.lanking.uxb.service.mall.ex.MemberPackagePaymentException;
import com.lanking.uxb.service.mall.value.VMemberPackage;
import com.lanking.uxb.service.payment.cache.CallbackOrderCache;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.payment.weixin.response.OrderQueryResult;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 会员购买相关
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月26日
 */
@RestController
@RequestMapping(value = "zy/mall/pay/mempackage")
public class ZyMemberPackagePaymentController extends AbstractMemberPackagePaymentController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberPackageService memberPackageService;
	@Autowired
	private MemberPackageConvert memberPackageConvert;
	@Autowired
	private MemberPackageOrderService memberPackageOrderService;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private WXPaymentService wxPaymentService;
	@Autowired
	private CallbackOrderCache callbackOrderCache;
	@Autowired
	private MemberPackageCardService memberPackageCardService;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private MemberPackageOrderSettlementService memberPackageOrderSettlementService;
	@Autowired
	private UserConvert userConvert;

	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

	/**
	 * 获得教师套餐.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = "TEACHER")
	@RequestMapping(value = "tea")
	public Value queryTeaMemberPackage() {
		List<MemberPackage> memberPackages = memberPackageService.queryMemberPackage(UserType.TEACHER, MemberType.VIP);
		List<VMemberPackage> vMemberPackages = memberPackageConvert.to(memberPackages);

		User user = accountService.getUserByUserId(Security.getUserId());
		Integer channelCode = user.getUserChannelCode() == null ? UserChannel.YOOMATH : user.getUserChannelCode();

		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("memberPackages", channelCode == UserChannel.YOOMATH ? Lists.newArrayList() : vMemberPackages);
		map.put("isChannelUser", !(channelCode == UserChannel.YOOMATH)); // 是否是渠道用户
		return new Value(map);
	}

	/**
	 * 获得学生套餐.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = "STUDENT")
	@RequestMapping(value = "stu")
	public Value queryStuMemberPackage() {
		User user = accountService.getUserByUserId(Security.getUserId());
		VUser vuser = userConvert.to(user);
		Integer channelCode = user.getUserChannelCode() == null ? UserChannel.YOOMATH : user.getUserChannelCode();
		Map<String, Object> map = new HashMap<String, Object>(2);
		List<MemberPackage> memberPackages = new ArrayList<MemberPackage>();
		// 不是渠道用户，自主注册用户
		if (channelCode == UserChannel.YOOMATH) {
			memberPackages = memberPackageService.queryMemberPackageByAutoRegister(UserType.STUDENT, MemberType.VIP);
		} else {
			memberPackages = memberPackageService.queryMemberPackageByGroup(UserType.STUDENT, MemberType.VIP,
					vuser.getSchool() == null ? null : vuser.getSchool().getId(), channelCode);
		}
		List<VMemberPackage> vMemberPackages = memberPackageConvert.to(memberPackages);
		map.put("memberPackages", vMemberPackages);
		map.put("isChannelUser", !(channelCode == UserChannel.YOOMATH)); // 是否是渠道用户
		return new Value(map);
	}

	/**
	 * 创建会员套餐订单.
	 * 
	 * @param source
	 *            来源
	 * @param memberPackageID
	 *            会员套餐
	 * @param paymentPlatformCode
	 *            支付平台
	 * @param attachData
	 *            附加数据
	 * @param isContinue
	 *            是否为续费
	 * @param cardCode
	 *            会员卡付费
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping("createOrder")
	@MemberAllowed
	public Value createOrder(MemberPackageOrderPageSource source, Long memberPackageID, Integer paymentPlatformCode,
			String attachData, Boolean isContinue, String cardCode) {
		if (null == memberPackageID && StringUtils.isBlank(cardCode)) {
			return new Value(new MissingArgumentException());
		}
		long userID = Security.getUserId();
		PayMode payMode = PayMode.ONLINE;
		MemberType saveMemberType = MemberType.VIP;
		Map<String, Object> map = new HashMap<String, Object>(10);
		MemberType memberType = SecurityContext.getMemberType() == null ? MemberType.NONE
				: SecurityContext.getMemberType();

		JSONObject attachDataJSON = new JSONObject();
		attachDataJSON.put("attach", attachData);
		attachDataJSON.put("source", source);

		// 会员卡激活套餐
		MemberPackageCard card = null;
		if (StringUtils.isNotBlank(cardCode)) {
			paymentPlatformCode = null;
			cardCode = cardCode.toUpperCase();
			isContinue = true;
			String accountId = String.valueOf(Security.getAccountId());
			long errorCount = accountCacheService.getMemberpackageCardActiveTime(accountId);
			if (errorCount >= 5) {
				// 试错频繁
				return new Value(
						new MemberPackagePaymentException(MemberPackagePaymentException.MEMPACK_CARD_ERROR_FREQUENT));
			}
			card = memberPackageCardService.get(cardCode);
			if (card == null) {
				accountCacheService.setMemberpackageCardActiveTime(accountId, errorCount + 1, 10);
				// 卡号不存在
				return new Value(
						new MemberPackagePaymentException(MemberPackagePaymentException.MEMPACK_CARD_NOT_FOUND));
			} else if ((memberType != MemberType.NONE && memberType != card.getMemberType())
					|| (card.getUserType() != UserType.NULL && Security.getUserType() != card.getUserType())) {
				// 卡号类型不匹配
				return new Value(
						new MemberPackagePaymentException(MemberPackagePaymentException.MEMPACK_CARD_TYPE_ERROR));
			}
			if (card.getStatus() == MemberPackageCardStatus.DISABLE) {
				// 卡冻结
				return new Value(new MemberPackagePaymentException(MemberPackagePaymentException.MEMPACK_CARD_DISABLE));
			} else if (card.getStatus() == MemberPackageCardStatus.DELETE) {
				// 卡已被使用
				return new Value(new MemberPackagePaymentException(MemberPackagePaymentException.MEMPACK_CARD_USED));
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(card.getEndAt());
			cal.add(Calendar.DAY_OF_YEAR, 1);
			if (cal.getTime().getTime() < System.currentTimeMillis()) {
				// 卡过期
				map.put("errflag", 1);
				map.put("errCode", MemberPackagePaymentException.MEMPACK_CARD_TIME_OUT);
				map.put("endAt", card.getEndAt());
				return new Value(map);
			}
			payMode = PayMode.VIRTUAL_CARD;
			saveMemberType = card.getMemberType();
			map.put("card", card);
		} else {
			MemberPackage memberPackage = memberPackageService.get(memberPackageID);
			if (memberPackage == null || memberPackage.getStatus() != Status.ENABLED) {
				return new Value(new MemberPackagePaymentException(MemberPackagePaymentException.MEMPACK_UNPUBLISH));
			}

			UserMember userMember = userMemberService.findByUserId(userID);
			if (memberType == MemberType.SCHOOL_VIP && memberPackage.getMemberType() == MemberType.VIP) {
				// 已是校级会员无法续费开通普通会员
				return new Value(new MemberPackagePaymentException(MemberPackagePaymentException.MEMPACK_NOT_MATCH));
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
								new MemberPackagePaymentException(MemberPackagePaymentException.MEMPACK_BUY_DUPLICATE));
					}
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
				}
			}

			// since 2017-01-24 cwl 获得最近的未完成微信订单，只有超过验证码有效期再重新创建订单，减少废单
			if (paymentPlatformCode == 1) {
				MemberPackageOrder lastOrder = memberPackageOrderService.getWXLastNotpayOrder(userID, memberPackageID);
				int invalidTime = Env.getDynamicInt("weixin.pay.qrcode.timeout"); //
				// 微信支付验证码有效时间（分钟）
				if (lastOrder != null
						&& System.currentTimeMillis() - lastOrder.getOrderAt().getTime() < invalidTime * 60 * 1000) {
					// 小于验证码失效时间
					map.put("orderID", lastOrder.getId());
					return new Value(map);
				}
			}
		}

		// 创建订单
		try {
			ThirdPaymentMethod thirdPaymentMethod = ThirdPaymentMethod.DEFAULT;
			if (StringUtils.isBlank(cardCode) && paymentPlatformCode == 1) {
				thirdPaymentMethod = ThirdPaymentMethod.WX_NATIVE;
			}
			MemberPackageOrder memberPackageOrder = memberPackageOrderService.createOrder(userID, memberPackageID,
					saveMemberType, payMode, paymentPlatformCode, thirdPaymentMethod, attachDataJSON.toJSONString(),
					card);
			map.put("orderID", memberPackageOrder.getId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Value(new ServerException());
		}

		return new Value(map);
	}

	/**
	 * 查询微信订单状态.
	 * 
	 * @param memberPackageOrderID
	 *            本地订单ID
	 * @return
	 */
	@RequestMapping("queryWXOrder")
	public Value queryWXOrder(final Long memberPackageOrderID) {
		if (null == memberPackageOrderID) {
			return new Value(new MissingArgumentException());
		}

		try {
			final SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
			String appid = Env.getString("weixin.pay.appid");
			OrderQueryResult result = wxPaymentService.orderQuery(appid, null, String.valueOf(memberPackageOrderID));
			final String return_code = result.getReturnCode(); // 返回状态
			String trade_state = result.getTradeState(); // 交易状态
			final String timeEnd = result.getTimeEnd(); // 交易付款时间
			final String transactionId = result.getTransactionId(); // 微信支付订单号

			if ("SUCCESS".equalsIgnoreCase(return_code) && ("SUCCESS".equalsIgnoreCase(trade_state))) {
				// 交易成功

				// 判断订单是否已被处理中
				if (callbackOrderCache.hasProcessing(OrderBusinessSource.USER_MEMBER, memberPackageOrderID)) {
					return new Value(0);
				} else {
					callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.USER_MEMBER, memberPackageOrderID);
				}
				logger.info("[会员套餐] 微信购买, orderID = " + memberPackageOrderID);

				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						Date payTime = null;
						try {
							payTime = formate.parse(timeEnd);
						} catch (ParseException e) {
							payTime = new Date();
							logger.error(e.getMessage(), e);
						}

						// 支付订单
						memberPackageOrderService.updatePaymentCallback(memberPackageOrderID, null, transactionId,
								payTime);

						// 完成订单
						MemberPackageOrder order = memberPackageOrderService.updateOrderStatus(memberPackageOrderID,
								null, MemberPackageOrderStatus.COMPLETE);

						// 处理会员数据
						MemberPackage memberPackage = memberPackageService.get(order.getMemberPackageId());
						userMemberService.createOrRenew(order.getUserId(), payTime, memberPackage, memberPackageOrderID,
								null);

						callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER,
								memberPackageOrderID);

						// 用户会员开通动作行为
						userActionService.asyncAction(UserAction.OPEN_VIP, order.getUserId(), null);

						// 处理渠道统计
						memberPackageOrderSettlementService.createByOrder(order, 1);
					}
				});
				return new Value(1);
			} else if ("REVOKED".equalsIgnoreCase(trade_state) || "PAYERROR".equalsIgnoreCase(trade_state)
					|| "CLOSED".equalsIgnoreCase(trade_state)) {
				// 交易失败
				fixedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						memberPackageOrderService.updateOrderStatus(memberPackageOrderID, null,
								MemberPackageOrderStatus.FAIL);
					}
				});
			} else {
				return new Value(2);
			}
		} catch (Exception e) {
			logger.error("查询微信订单状态出错！", e);
			// 订单处理完成
			callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.USER_MEMBER, memberPackageOrderID);
			return new Value(new ServerException());
		}
		return new Value(0);
	}

	/**
	 * 订单支付完成.
	 * 
	 * @param orderID
	 *            订单ID
	 * @return
	 */
	@RequestMapping("complete")
	public Value complete(Long orderID) {
		if (null == orderID || orderID < 1) {
			return new Value(new MissingArgumentException());
		}
		MemberPackageOrder memberPackageOrder = memberPackageOrderService.get(orderID);
		if (memberPackageOrder == null) {
			return new Value(new EntityNotFoundException());
		}
		if (memberPackageOrder.getUserId() != Security.getUserId()) {
			return new Value(new EntityNotFoundException());
		}

		long memberPackageId = memberPackageOrder.getMemberPackageId();

		Map<String, Object> map = new HashMap<String, Object>(8);
		map.put("userID", Security.getUserId());
		map.put("status", memberPackageOrder.getStatus());
		map.put("totalPrice", memberPackageOrder.getTotalPrice().doubleValue());
		map.put("attach", memberPackageOrder.getAttachData());
		map.put("memberPackage", memberPackageConvert.to(memberPackageService.get(memberPackageId)));

		if (StringUtils.isNotBlank(memberPackageOrder.getAttachData())) {
			JSONObject attachDataJSON = JSONObject.parseObject(memberPackageOrder.getAttachData());
			map.put("attach", attachDataJSON.getString("attach"));
			map.put("source", attachDataJSON.getString("source"));
		}

		UserMember userMember = userMemberService.findByUserId(Security.getUserId());
		map.put("userMember", userMember);

		// 会员卡支付订单
		if (memberPackageOrder.getPayMod() == PayMode.VIRTUAL_CARD) {
			MemberPackageCard card = memberPackageCardService.get(memberPackageOrder.getVirtualCardCode());
			map.put("card", card);
		}
		return new Value(map);
	}

	/**
	 * 获得会员卡.
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping("getMemberPackageCard")
	public Value getMemberPackageCard(String code) {
		if (StringUtils.isNotBlank(code)) {
			MemberPackageCard card = memberPackageCardService.get(code);
			return new Value(card);
		}
		return new Value();
	}

	/**
	 * 废单使用.
	 * 
	 * @param orderID
	 *            订单ID.
	 * @return
	 */
	@RequestMapping("deleteOrder")
	public Value deleteOrder(Long orderID) {
		if (orderID != null) {
			MemberPackageOrder order = memberPackageOrderService.get(orderID);
			if (order != null && order.getUserId() == Security.getUserId()
					&& order.getStatus() == MemberPackageOrderStatus.NOT_PAY) {
				memberPackageOrderService.deleteOrder(orderID);
			}
		}

		return new Value();
	}
}
