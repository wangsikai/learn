package com.lanking.uxb.service.member.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.order.PaymentPlatform;
import com.lanking.cloud.domain.yoo.order.ThirdPaymentMethod;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.util.RSACoder;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.file.util.QRCodeUtil;
import com.lanking.uxb.service.mall.api.MemberPackageOrderService;
import com.lanking.uxb.service.mall.api.MemberPackagePaymentService;
import com.lanking.uxb.service.mall.api.MemberPackageService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.weixin.client.WXPaymentClient;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.weixin.client.WXClient;
import com.lanking.uxb.service.thirdparty.weixin.response.UserAccessToken;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;

/**
 * 用户会员代付相关接口.
 * 
 * @since 学生端1.4.2
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月31日
 */
@RestController
@RequestMapping("zy/m/anpay/member")
public class ZyMMemberANPayController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberPackageOrderService memberPackageOrderService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private MemberPackageService memberPackageService;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private WXPaymentClient wxPaymentClient;
	@Autowired
	private WXClient wxclient;
	@Autowired
	private MemberPackagePaymentService memberPackagePaymentService;
	@Autowired
	private ZyMMemberController zyMMemberController;

	/**
	 * 获取代付订单数据.
	 * 
	 * @since 学生客户端 v1.4.2
	 * @param id
	 *            订单ID
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "getOrderPayInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getOrderPayInfo(Long id) {
		if (id == null) {
			return new Value(new MissingArgumentException());
		}
		MemberPackageOrder order = memberPackageOrderService.get(id);
		if (order == null) {
			return new Value(new EntityNotFoundException());
		}
		long userID = Security.getUserId();
		if (userID != order.getUserId()) {
			return new Value(new NoPermissionException());
		}
		// User user = accountService.getUserByUserId(userID);
		Account account = accountService.getAccountByUserId(userID);
		MemberPackage memberPackage = memberPackageService.get(order.getMemberPackageId());
		Map<String, Object> map = new HashMap<String, Object>(10);
		map.put("memberType", memberPackage.getMemberType());
		map.put("userType", memberPackage.getUserType());
		map.put("month", memberPackage.getMonth());
		map.put("extraMonth", memberPackage.getExtraMonth());
		map.put("totalPrice", order.getTotalPrice());
		map.put("name", account.getName());
		map.put("status", order.getStatus());
		try {
			String sign = RSACoder.encryptBASE64(id.toString().getBytes()).replaceAll("\\s", "").trim();
			map.put("completeUrl", Env.getString("m.orderpay.complete") + "?id=" + id + "&sign=" + sign);
		} catch (Exception e) {
			logger.error("获取代付订单数据", e);
		}

		boolean outTime = false;
		if (System.currentTimeMillis() - order.getOrderAt().getTime() >= 120 * 60 * 1000) {
			// 超过2小时失效
			outTime = true;
		}
		map.put("outTime", outTime);

		return new Value(map);
	}

	/**
	 * 获取代付订单二维码.
	 * 
	 * @since 学生客户端 v1.4.2
	 * @param id
	 *            订单ID
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getOrderPayQRCode", method = { RequestMethod.GET })
	public void getOrderPayQRCode(Long id, HttpServletRequest request, HttpServletResponse response) {
		if (id != null) {
			MemberPackageOrder order = memberPackageOrderService.get(id);
			if (order != null) {
				try {
					if (System.currentTimeMillis() - order.getOrderAt().getTime() >= 118 * 60 * 1000) {
						// 过期刷新
						order = memberPackageOrderService.refreshOrder(id);
					}
					String url = Env.getString("m.orderpay.path") + "?id=" + id + "&t=" + order.getOrderAt().getTime();
					BufferedImage bufferImage = QRCodeUtil.createQRCode(url, 260, 260, ErrorCorrectionLevel.L, 0);
					ImageIO.write(bufferImage, "jpg", response.getOutputStream());
				} catch (Exception e) {
					logger.error("获取代付订单二维码出错", e);
				}
			}
		}
	}

	/**
	 * 路径跳转判断.
	 * 
	 * @param id
	 *            订单ID
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "orderPayPath", method = { RequestMethod.GET })
	public void urlCheck(Long id, HttpServletRequest request, HttpServletResponse response) {
		if (id != null) {
			String agent = request.getHeader("User-Agent").toLowerCase();
			try {
				String sign = RSACoder.encryptBASE64(id.toString().getBytes()).replaceAll("\\s", "").trim();
				if (agent.indexOf("micromessenger") != -1) {
					// 微信
					String appid = Env.getString("weixin.appid.zuoye");
					String url = Env.getString("m.orderpay.url") + "?id=" + id + "__" + sign;
					String authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid
							+ "&redirect_uri=" + url
							+ "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
					response.sendRedirect(authUrl);

				} else if (agent.indexOf("alipayclient") != -1) {
					String url = Env.getString("m.orderpay.url") + "?id=" + id + "__" + sign;
					response.sendRedirect(url);
				}
			} catch (Exception e) {
				logger.error("会员订单代付-路径跳转判断出错", e);
			}
		}
	}

	/**
	 * 他人获取代付订单数据.
	 * 
	 * @since 学生客户端 v1.4.2
	 * @param id
	 *            订单ID
	 * @param sign
	 *            验证码
	 * @param complate
	 *            完成标记
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getOrderInfoToPay", method = { RequestMethod.POST })
	public Value getOrderInfoToPay(Long id, String sign, Boolean complate, HttpServletRequest request,
			HttpServletResponse response) {
		if (id == null || StringUtils.isBlank(sign)) {
			return new Value(new MissingArgumentException());
		}
		try {
			String signRight = RSACoder.encryptBASE64(id.toString().getBytes()).replaceAll("\\s", "");
			if (!signRight.equals(sign)) {
				return new Value(new NoPermissionException());
			}
		} catch (Exception e) {
			logger.error("获取代付订单数据出错", e);
		}

		MemberPackageOrder order = memberPackageOrderService.get(id);
		if (order == null) {
			return new Value(new EntityNotFoundException());
		}
		// User user = accountService.getUserByUserId(order.getUserId());
		Account account = accountService.getAccountByUserId(order.getUserId());
		MemberPackage memberPackage = memberPackageService.get(order.getMemberPackageId());
		Map<String, Object> map = new HashMap<String, Object>(16);
		map.put("memberType", memberPackage.getMemberType());
		map.put("userType", memberPackage.getUserType());
		map.put("month", memberPackage.getMonth());
		map.put("extraMonth", memberPackage.getExtraMonth());
		map.put("totalPrice", order.getTotalPrice());
		map.put("name", account.getName());
		map.put("status", order.getStatus());
		map.put("completeUrl", Env.getString("m.orderpay.complete") + "?id=" + id + "&sign=" + sign);
		map.put("validUrl", Env.getString("m.orderpay.validUrl"));

		// 获取微信重刷链接
		String agent = request.getHeader("User-Agent").toLowerCase();
		try {
			if (agent.indexOf("micromessenger") != -1) {
				// 微信
				String appid = Env.getString("weixin.appid.zuoye");
				String url = Env.getString("m.orderpay.url") + "?id=" + id + "__" + sign;
				String reloadUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid
						+ "&redirect_uri=" + url + "&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
				map.put("reloadUrl", reloadUrl);
			}
		} catch (Exception e) {
			logger.error("会员订单代付-路径跳转判断出错", e);
		}

		// 会员信息
		map.put("continue", false); // 是否续期
		if (complate != null && complate == true) {
			// 若查询本地订单仍为未支付状态，需要主动调用一次支付平台查询
			if (order.getStatus() == MemberPackageOrderStatus.NOT_PAY && order.getPaymentPlatformCode() != null) {
				// 查询第三方
				if (order.getPaymentPlatformCode() == 1) {
					// 微信订单
					String appid = Env.getString("weixin.appid.zuoye");
					zyMMemberController.queryWXOrder(appid, order.getId());
				} else if (order.getPaymentPlatformCode() == 2) {
					// 支付宝订单
					zyMMemberController.queryAlipayOrder(order.getId());
				}
			}

			UserMember userMember = userMemberService.findByUserId(order.getUserId());
			if (null != userMember) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = userMember.getStartAt();
				Date endDate = userMember.getEndAt();
				map.put("endAt", format.format(endDate));
				Calendar cal = Calendar.getInstance();
				cal.setTime(startDate);
				cal.add(Calendar.MONTH, memberPackage.getMonth() + memberPackage.getExtraMonth());
				if (cal.getTime().compareTo(endDate) == -1) {
					// 表示续期操作
					map.put("continue", true);
				}
			}
		}

		boolean outTime = false;
		if (System.currentTimeMillis() - order.getOrderAt().getTime() >= 120 * 60 * 1000) {
			// 超过2小时失效
			outTime = true;
		}
		map.put("outTime", outTime);

		return new Value(map);
	}

	/**
	 * 创建微信预支付订单.
	 * 
	 * @since 学生客户端 v1.4.2
	 * @param id
	 *            订单ID
	 * @param sign
	 *            验证标记
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "createWXOrderPay", method = { RequestMethod.POST })
	public Value createWXOrderPay(Long id, String sign, HttpServletRequest request, HttpServletResponse response) {
		String code = request.getParameter("code");
		if (id == null || StringUtils.isBlank(sign) || StringUtils.isBlank(code)) {
			return new Value(new MissingArgumentException());
		}

		try {
			String signRight = RSACoder.encryptBASE64(id.toString().getBytes()).replaceAll("\\s", "").trim();
			if (!signRight.equals(sign)) {
				return new Value(new NoPermissionException());
			}
		} catch (Exception e) {
			logger.error("获取代付订单数据出错", e);
		}
		MemberPackageOrder order = memberPackageOrderService.get(id);
		if (order == null) {
			return new Value(new EntityNotFoundException());
		}

		if (order.getStatus() != MemberPackageOrderStatus.NOT_PAY) {
			// 订单状态无法支付
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_NOTFOUND));
		}

		// 获取用户OPENID
		try {
			UserAccessToken accessToken = wxclient.getUserAccessToken(code, Product.YOOMATH);
			String openId = accessToken.getOpenid();

			// 生成微信预支付订单
			String appid = Env.getString("weixin.appid.zuoye");
			UnifiedPayResult result = memberPackagePaymentService.getWXQRCodeImage(appid, order.getMemberPackageId(),
					id, OrderPayBusinessSpace.MEMBER_PACKAGE, "JSAPI", openId, request, response);
			if (result != null && result.getReturnCode().equals("SUCCESS")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("appId", appid);
				map.put("timeStamp", new Date().getTime());
				map.put("nonceStr", RandomStringGenerator.getRandomStringByLength(32));
				map.put("package", "prepay_id=" + result.getPrepayId());
				map.put("signType", "MD5");
				sign = Signature.getSign(map, wxPaymentClient.getConfigure(appid)); // 签名
				map.put("paySign", sign);

				// 更新本地订单数据
				memberPackageOrderService.updatePayOrderInfos(id, 1, ThirdPaymentMethod.WX_JSAPI);
				return new Value(map);
			} else {
				logger.error(result != null ? result.getReturnMsg() : "创建微信预支付订单失败");
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_FAILURE));
			}

		} catch (org.apache.http.ParseException | IOException | DocumentException e) {
			logger.error("创建微信预支付订单失败", e);
			return new Value(new ServerException());
		}
	}

	/**
	 * 查询代付订单状态.
	 * 
	 * @since 学生客户端 v1.4.2
	 * @param orderId
	 *            本地订单ID
	 * @param sign
	 *            验证码
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "queryOrderPayStatus")
	public Value queryOrderPayStatus(final Long orderId, String sign) {
		if (null == orderId || StringUtils.isBlank(sign)) {
			return new Value(new MissingArgumentException());
		}
		try {
			String signRight = RSACoder.encryptBASE64(orderId.toString().getBytes()).replaceAll("\\s", "").trim();
			if (!signRight.equals(sign)) {
				return new Value(new NoPermissionException());
			}
		} catch (Exception e) {
			logger.error("查询代付订单数据出错", e);
		}

		return this.queryOrderStatus(orderId, true);
	}

	/**
	 * 创建支付宝订单.
	 * 
	 * @since 学生客户端 v1.4.2
	 * @param id
	 *            订单ID
	 * @param sign
	 *            验证标记
	 * @return
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "createAlipayOrderPay", method = { RequestMethod.GET })
	public void createAlipayOrderPay(Long id, String sign, HttpServletRequest request, HttpServletResponse response) {
		if (id == null || StringUtils.isBlank(sign)) {
			return;
		}

		try {
			String signRight = RSACoder.encryptBASE64(id.toString().getBytes()).replaceAll("\\s", "").trim();
			if (!signRight.equals(sign)) {
				return;
			}

			MemberPackageOrder order = memberPackageOrderService.get(id);
			if (order == null) {
				return;
			}

			// 更新本地订单数据
			memberPackageOrderService.updatePayOrderInfos(id, PaymentPlatform.ALIPAY.getValue(), null);

			memberPackagePaymentService.jumpToAlipayWap(order.getMemberPackageId(), id,
					OrderPayBusinessSpace.MEMBER_PACKAGE,
					Env.getString("m.orderpay.complete") + "?id=" + id + "&sign=" + sign, request, response);
		} catch (Exception e) {
			logger.error("创建支付宝代支付订单出错", e);
		}
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
				return zyMMemberController.queryWXOrder(appid, orderId);
			} else if (order.getPaymentPlatformCode() == 2) {
				// 支付宝订单
				return zyMMemberController.queryAlipayOrder(orderId);
			}
		}
		Map<String, Object> returnMap = Maps.newHashMap();
		returnMap.put("status", order.getStatus());
		boolean outTime = false;
		if (System.currentTimeMillis() - order.getOrderAt().getTime() >= 120 * 60 * 1000) {
			// 超过2小时失效
			outTime = true;
		}
		returnMap.put("outTime", outTime);
		return new Value(returnMap);
	}
}
