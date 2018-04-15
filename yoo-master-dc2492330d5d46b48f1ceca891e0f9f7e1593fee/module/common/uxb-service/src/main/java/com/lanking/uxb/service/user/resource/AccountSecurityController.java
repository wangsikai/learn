package com.lanking.uxb.service.user.resource;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqSessionRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.message.util.VerifyCodes;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.convert.AccountConvert;
import com.lanking.uxb.service.user.value.VAccount;

/**
 * Account Security Controller, in ucenter module
 *
 * @author James
 * @since v2.1
 */
@RestController
@RequestMapping("ucenter/account/security")
public class AccountSecurityController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private AccountConvert accountConvert;
	@Autowired
	private MqSender mqSender;

	/**
	 *
	 * send Email
	 *
	 * @param newEmail
	 * @return
	 */
	@RequestMapping(value = "send_email_valid_code", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sendEmailValidCode(@RequestParam("newEmail") String newEmail, Product product) {

		if (StringUtils.isBlank(newEmail)) {
			return new Value(new IllegalArgException());
		}
		try {
			ValidateUtils.validateEmail(newEmail);
		} catch (AccountException e) {
			return new Value(e);
		}
		if (accountService.getAccount(GetType.EMAIL, newEmail) != null) {
			return new Value(new AccountException(AccountException.ACCOUNT_EMAIL_EXIST));
		}
		String cachesEmailCode = accountCacheService.getVerifyCode(Security.getToken(), Security.getToken() + newEmail);
		// 缓存当中没有已经发过验证码信息后，可以再次发送
		if (StringUtils.isBlank(cachesEmailCode)) {
			String validCode = VerifyCodes.emailCode(6);
			UserInfo userInfo = userService.getUser(Security.getUserId());

			ValueMap vm = ValueMap.value("authCode", validCode).put("name", userInfo.getName());
			if (accountService.getAccount(Security.getAccountId()).getEmailStatus() == Status.ENABLED) {
				if (product != null && product == Product.YOOMATH) {
					messageSender.send(new EmailPacket(newEmail, 11000011, vm));
				} else {
					messageSender.send(new EmailPacket(newEmail, 11000009, vm));
				}
			} else {
				if (product != null && product == Product.YOOMATH) {
					messageSender.send(new EmailPacket(newEmail, 11000012, vm));
				} else {
					messageSender.send(new EmailPacket(newEmail, 11000010, vm));
				}
			}

			accountCacheService.setVerifyCode(Security.getToken(), Security.getToken() + newEmail, validCode, 2,
					TimeUnit.MINUTES);
			accountCacheService.setVerifyCode(Security.getToken(), newEmail, validCode, 5, TimeUnit.MINUTES);
		} else {
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_NOT_EXPIRE));
		}

		return new Value();
	}

	/**
	 * valid the email
	 *
	 * @param validCode
	 *            valid code
	 * @param newEmail
	 *            need valid the email address
	 * @return
	 */
	@RequestMapping(value = "valid_email", method = { RequestMethod.POST, RequestMethod.GET })
	public Value validEmail(@RequestParam("validCode") String validCode, @RequestParam("newEmail") String newEmail,
			String password, Integer strength) {
		if (StringUtils.isBlank(validCode) || StringUtils.isBlank(newEmail)) {
			return new Value(new IllegalArgException());
		}
		try {
			ValidateUtils.validateEmail(newEmail);
		} catch (AccountException e) {
			return new Value(e);
		}
		if (accountService.getAccount(GetType.EMAIL, newEmail) != null) {
			return new Value(new AccountException(AccountException.ACCOUNT_EMAIL_EXIST));
		}
		String cacheValidCode = accountCacheService.getVerifyCode(Security.getToken(), newEmail);
		if (cacheValidCode == null) {
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}
		if (cacheValidCode.equalsIgnoreCase(validCode)) {
			Account account = accountService.updateAccountEmailStatus(newEmail, Status.ENABLED, Security.getUserId());

			// 更新密码
			if (StringUtils.isNotBlank(password)) {
				try {
					ValidateUtils.validatePassword(password);
				} catch (AccountException e) {
					return new Value(e);
				}
				accountService.updatePassword(account.getId(), password, strength);
				JSONObject jo = new JSONObject();
				jo.put("userId", Security.getUserId());
				jo.put("excludeToken", Security.getToken());
				mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
						MQ.builder().data(jo).build());
			}

			// 删除缓存
			accountCacheService.invalidVerifyCode(Security.getToken(), newEmail);
			VAccount vAccount = accountConvert.to(account);
			return new Value(vAccount);
		}

		return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
	}

	/**
	 * send mobile valid code to user
	 *
	 * @param newMobile
	 *            the mobile which need to be sended code
	 * @return
	 */
	@RequestMapping(value = "send_mobile_valid_code", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sendMobileValidCode(@RequestParam("newMobile") String newMobile, Product product) {
		if (StringUtils.isBlank(newMobile)) {
			return new Value(new IllegalArgException());
		}
		try {
			ValidateUtils.validateMobile(newMobile);
		} catch (AccountException e) {
			return new Value(e);
		}
		if (accountService.getAccount(GetType.MOBILE, newMobile) != null) {
			return new Value(new AccountException(AccountException.ACCOUNT_MOBILE_EXIST));
		}
		String cacheValidCode = accountCacheService.getVerifyCode(Security.getToken(), Security.getToken() + newMobile);
		if (StringUtils.isBlank(cacheValidCode)) {
			String validCode = VerifyCodes.smsCode(6);
			if (accountService.getAccount(Security.getAccountId()).getMobileStatus() == Status.ENABLED) {
				if (product != null && product == Product.YOOMATH) {
					messageSender.send(new SmsPacket(newMobile, 10000000, ValueMap.value("authCode", validCode)));
				} else {
					messageSender.send(new SmsPacket(newMobile, 10000004, ValueMap.value("authCode", validCode)));
				}
			} else {
				if (product != null && product == Product.YOOMATH) {
					messageSender.send(new SmsPacket(newMobile, 10000011, ValueMap.value("authCode", validCode)));
				} else {
					messageSender.send(new SmsPacket(newMobile, 10000005, ValueMap.value("authCode", validCode)));
				}
			}
			accountCacheService.setVerifyCode(Security.getToken(), Security.getToken() + newMobile, validCode, 1,
					TimeUnit.MINUTES);
			accountCacheService.setVerifyCode(Security.getToken(), newMobile, validCode, 5, TimeUnit.MINUTES);
		} else {
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_NOT_EXPIRE));
		}

		return new Value();
	}

	/**
	 * valid the code is right
	 *
	 * @param validCode
	 *            valid code
	 * @param newMobile
	 *            new user binding mobile
	 * @return
	 */
	@RequestMapping(value = "valid_mobile", method = { RequestMethod.POST, RequestMethod.GET })
	public Value validMobile(@RequestParam("validCode") String validCode, @RequestParam("newMobile") String newMobile,
			String password, Integer strength) {
		if (StringUtils.isBlank(validCode) || StringUtils.isBlank(newMobile)) {
			return new Value(new IllegalArgException());
		}
		try {
			ValidateUtils.validateMobile(newMobile);
		} catch (AccountException e) {
			return new Value(e);
		}
		if (accountService.getAccount(GetType.MOBILE, newMobile) != null) {
			return new Value(new AccountException(AccountException.ACCOUNT_MOBILE_EXIST));
		}
		String cacheValidCode = accountCacheService.getVerifyCode(Security.getToken(), newMobile);
		if (cacheValidCode == null) {
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}
		if (cacheValidCode.equalsIgnoreCase(validCode)) {
			Account account = accountService.updateAccountMobileStatus(newMobile, Status.ENABLED, Security.getUserId());
			// 绑定手机任务 by peng.zhao
			// 只有学生才有此任务
			if (UserType.STUDENT == Security.getUserType()) {
				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101000006);
				messageObj.put("userId", Security.getUserId());
				messageObj.put("isClient", false);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());
			}

			// 更新密码
			if (StringUtils.isNotBlank(password)) {
				try {
					ValidateUtils.validatePassword(password);
				} catch (AccountException e) {
					return new Value(e);
				}
				accountService.updatePassword(account.getId(), password, strength);
				JSONObject jo = new JSONObject();
				jo.put("userId", Security.getUserId());
				jo.put("excludeToken", Security.getToken());
				mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
						MQ.builder().data(jo).build());
			}

			// 删除缓存
			accountCacheService.invalidVerifyCode(Security.getToken(), newMobile);

			VAccount vAccount = accountConvert.to(account);
			return new Value(vAccount);
		}

		return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
	}
}
