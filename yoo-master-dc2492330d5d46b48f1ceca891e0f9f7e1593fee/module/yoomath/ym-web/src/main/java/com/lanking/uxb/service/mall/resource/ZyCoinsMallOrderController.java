package com.lanking.uxb.service.mall.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.mall.api.CoinsGoodsOrderService;
import com.lanking.uxb.service.mall.api.CoinsGoodsService;
import com.lanking.uxb.service.mall.cache.GoodsValidCodeCacheService;
import com.lanking.uxb.service.mall.convert.CoinsGoodsConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsOrderConvert;
import com.lanking.uxb.service.mall.convert.CoinsGoodsOrderConvertOption;
import com.lanking.uxb.service.mall.form.CoinsGoodsOrderForm;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.message.util.VerifyCodes;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 金币商城订单相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
@RestController
@RequestMapping("zy/mall/order")
public class ZyCoinsMallOrderController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CoinsGoodsService coinsGoodsService;
	@Autowired
	private CoinsGoodsConvert coinsGoodsConvert;
	@Autowired
	private CoinsGoodsOrderService coinsGoodsOrderService;
	@Autowired
	private GoodsValidCodeCacheService codeCacheService;
	@Autowired
	private UserService userService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CoinsGoodsOrderConvert coinsGoodsOrderConvert;
	@Autowired
	private MessageSender messageSender;

	/**
	 * 用户提交订单信息
	 *
	 * @param form
	 *            {@link CoinsGoodsOrderForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "createOrder", method = { RequestMethod.GET, RequestMethod.POST })
	public Value createOrder(CoinsGoodsOrderForm form) {
		String validCode = null;
		if (StringUtils.isNotBlank(form.getBindMobile())) {
			validCode = codeCacheService.getMobileCode(Security.getToken(), form.getBindMobile());
		} else if (StringUtils.isNotBlank(form.getBindEmail())) {
			validCode = codeCacheService.getEmailCode(Security.getToken(), form.getBindEmail());
		} else {
			return new Value(new IllegalArgException());
		}

		if (validCode == null) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_VALID_CODE_ERROR));
		}

		if (!validCode.equalsIgnoreCase(form.getValidCode())) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_VALID_CODE_ERROR));
		}
		Account account = accountService.getAccountByUserId(Security.getUserId());

		if (StringUtils.isBlank(account.getEmail()) && StringUtils.isBlank(account.getMobile())) {
			Account queryAccount = null;
			if (StringUtils.isNotBlank(form.getBindMobile())) {
				queryAccount = accountService.getAccount(GetType.MOBILE, form.getBindMobile());
			} else {
				queryAccount = accountService.getAccount(GetType.EMAIL, form.getBindEmail());
			}

			if (queryAccount != null) {
				return new Value(new AccountException(AccountException.ACCOUNT_MOBILE_EXIST));
			}

		} else {
			form.setBindEmail(null);
			form.setBindMobile(null);
		}

		if (form.getAmount() == 0
		        || (StringUtils.isBlank(form.getQq()) && StringUtils.isBlank(form.getMobile())
		                && StringUtils.isBlank(form.getMemo()))
		        || form.getCoinsGoodsId() == 0 || form.getCoinsGoodsSnapshotId() == 0) {
			return new Value(new IllegalArgException());
		}

		form.setUserId(Security.getUserId());
		form.setUserType(Security.getUserType());

		CoinsGoodsOrder order = null;
		try {
			order = coinsGoodsOrderService.createOrder(form);
			if (StringUtils.isNotBlank(form.getBindEmail())) {
				codeCacheService.invalidEmailCode(Security.getToken(), Security.getToken());
				codeCacheService.invalidEmailCode(Security.getToken(), form.getBindEmail());
			} else {
				codeCacheService.invalidMobileCode(Security.getToken(), Security.getToken());
				codeCacheService.invalidMobileCode(Security.getToken(), form.getBindMobile());
			}
		} catch (ZuoyeException | NoPermissionException e) {
			return new Value(e);
		}

		return new Value(coinsGoodsOrderConvert.to(order));
	}

	/**
	 * 得到订单信息
	 *
	 * @param id
	 *            coins goods order id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getOrderInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getOrderInfo(long id) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);

		retMap.put("order",
				coinsGoodsOrderConvert.to(coinsGoodsOrderService.get(id), new CoinsGoodsOrderConvertOption(true)));
		retMap.put("redirectUrl", Env.getString("mall.redirect.yoomath", new Object[] { Security.getUserId() }));

		return new Value(retMap);
	}

	/**
	 * 发送邮箱验证
	 *
	 * @param email
	 *            邮箱
	 * @return {@link Value}
	 */
	@RequestMapping(value = "sendEmailValidCode", method = { RequestMethod.GET, RequestMethod.POST })
	public Value sendEmailValidCode(String email) {
		if (StringUtils.isBlank(email)) {
			return new Value(new IllegalArgException());
		}
		try {
			ValidateUtils.validateEmail(email);
		} catch (ZuoyeException e) {
			return new Value(e);
		}

		Account queryAccount = accountService.getAccount(GetType.EMAIL, email);
		if (queryAccount != null && (!queryAccount.getId().equals(Security.getAccountId()))) {
			return new Value(new AccountException(AccountException.ACCOUNT_EMAIL_EXIST));
		}

		String code = codeCacheService.getEmailCode(Security.getToken(), Security.getToken());
		// 缓存中不存在验证码
		if (StringUtils.isBlank(code)) {
			String validCode = VerifyCodes.emailCode(6);

			UserInfo userInfo = userService.getUser(Security.getUserId());
			logger.info("[sendEmailValidCode] code = {}", validCode);
			messageSender.send(new EmailPacket(email, 11000004, ValueMap.value("authCode", validCode).put("name",
					userInfo.getName())));

			codeCacheService.setEmailCode(Security.getToken(), Security.getToken(), validCode, 2, TimeUnit.MINUTES);
			codeCacheService.setEmailCode(Security.getToken(), email, validCode, 5, TimeUnit.MINUTES);
		} else {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_VALID_CODE_ERROR));
		}

		return new Value();
	}

	/**
	 * 发送手机验证码
	 *
	 * @param mobile
	 *            手机号
	 * @return {@link Value}
	 */
	@RequestMapping(value = "sendMobileValidCode", method = { RequestMethod.GET, RequestMethod.POST })
	public Value sendMobileValidCode(String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return new Value(new IllegalArgException());
		}

		try {
			ValidateUtils.validateMobile(mobile);
		} catch (ZuoyeException e) {
			return new Value(e);
		}

		Account queryAccount = accountService.getAccount(GetType.MOBILE, mobile);
		if (queryAccount != null && (!queryAccount.getId().equals(Security.getAccountId()))) {
			return new Value(new AccountException(AccountException.ACCOUNT_MOBILE_EXIST));
		}

		String code = codeCacheService.getMobileCode(Security.getToken(), Security.getToken());
		if (StringUtils.isBlank(code)) {
			String authCode = VerifyCodes.smsCode(6);
			messageSender.send(new SmsPacket(mobile, 10000021, ValueMap.value("authCode", authCode)));
			codeCacheService.setMobileCode(Security.getToken(), Security.getToken(), authCode, 1, TimeUnit.MINUTES);
			codeCacheService.setMobileCode(Security.getToken(), mobile, authCode, 5, TimeUnit.MINUTES);
		} else {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_VALID_CODE_ERROR));
		}

		return new Value();
	}
}
