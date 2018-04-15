package com.lanking.uxb.service.account.resource;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.account.cache.ZyMAccountSecurityCacheService;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.message.util.VerifyCodes;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.api.UserService;

/**
 * 账户安全相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月28日
 */
@RestController
@RequestMapping("zy/m/account/sec")
public class ZyMAccountSecurityController {

	private Logger logger = LoggerFactory.getLogger(ZyMAccountSecurityController.class);

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private UserService userService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private ZyMAccountSecurityCacheService accountSecurityCacheService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private MqSender mqSender;

	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "sendOldBindSms", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sendOldBindSms() {
		Account account = accountService.getAccount(Security.getAccountId());
		if (StringUtils.isNotBlank(account.getMobile())) {
			String authCode = accountSecurityCacheService.getMobileCode(Security.getToken(),
					Security.getToken() + ".old");
			if (StringUtils.isBlank(authCode)) {
				authCode = VerifyCodes.smsCode(6);
				messageSender.send(new SmsPacket(account.getMobile(), 10000000, ValueMap.value("authCode", authCode)));
				accountSecurityCacheService.setMobileCode(Security.getToken(), Security.getToken() + ".old", authCode,
						1, TimeUnit.MINUTES);
				accountSecurityCacheService.setMobileCode(Security.getToken(), account.getMobile(),
						authCode + "_" + System.currentTimeMillis(), 30, TimeUnit.MINUTES);
				logger.info("sms code is:{}", authCode);
			} else {
				logger.info("last sms code is:{}", authCode);
			}
			return new Value();
		} else {
			return new Value(new ServerException());
		}
	}

	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "checkOldBindSms", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkOldBindSms(String authCode) {
		if (StringUtils.isBlank(authCode)) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
		Account account = accountService.getAccount(Security.getAccountId());
		if (StringUtils.isNotBlank(account.getMobile())) {
			String cacheAuthCode = accountSecurityCacheService.getMobileCode(Security.getToken(), account.getMobile());
			if (StringUtils.isNotBlank(cacheAuthCode)) {
				long sec = (System.currentTimeMillis() - Long.parseLong(cacheAuthCode.split("_")[1])) / 1000;
				if (sec > 300) {
					cacheAuthCode = null;
				}
			}
			if (StringUtils.isNotBlank(cacheAuthCode) && authCode.equals(cacheAuthCode.split("_")[0])) {
				return new Value();
			} else {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
			}
		} else {
			return new Value(new ServerException());
		}
	}

	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "sendBindSms", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sendBindSms(String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return new Value(new IllegalArgException());
		}
		try {
			ValidateUtils.validateMobile(mobile);
		} catch (AccountException e) {
			return new Value(e);
		}
		Account account = accountService.getAccount(Security.getAccountId());
		if (account != null && mobile.equals(account.getMobile())) {// 新手机号不能和老的手机号码一样
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_MOBILE_EXIST));
		}
		Account existAccount = accountService.getAccount(GetType.MOBILE, mobile);
		if (existAccount != null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_MOBILE_EXIST));
		}

		String authCode = accountSecurityCacheService.getMobileCode(Security.getToken(), Security.getToken());
		if (StringUtils.isBlank(authCode)) {
			authCode = VerifyCodes.smsCode(6);
			messageSender.send(new SmsPacket(mobile, 10000011, ValueMap.value("authCode", authCode)));
			accountSecurityCacheService.setMobileCode(Security.getToken(), Security.getToken(), authCode, 1,
					TimeUnit.MINUTES);
			accountSecurityCacheService.setMobileCode(Security.getToken(), mobile, authCode, 5, TimeUnit.MINUTES);
			logger.info("sms code is:{}", authCode);
		} else {
			logger.info("last sms code is:{}", authCode);
		}
		return new Value();
	}

	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "bindMobile", method = { RequestMethod.POST, RequestMethod.GET })
	public Value bindMobile(String oldSmsAuthCode, String mobile, String authCode) {
		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(authCode)) {
			return new Value(new IllegalArgException());
		}
		Account account = accountService.getAccount(Security.getAccountId());
		if (account != null && StringUtils.isNotBlank(account.getMobile()) && StringUtils.isBlank(oldSmsAuthCode)) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
		if (StringUtils.isNotBlank(account.getMobile())) {
			String cacheOldSmsAuthCode = accountSecurityCacheService.getMobileCode(Security.getToken(),
					account.getMobile());
			if (StringUtils.isNotBlank(cacheOldSmsAuthCode)) {
				long sec = (System.currentTimeMillis() - Long.parseLong(cacheOldSmsAuthCode.split("_")[1])) / 1000;
				if (sec > 300) {
					cacheOldSmsAuthCode = null;
				}
			}
			if (!oldSmsAuthCode.equals(cacheOldSmsAuthCode.split("_")[0])) {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
			}
		}
		String cacheAuthCode = accountSecurityCacheService.getMobileCode(Security.getToken(), mobile);
		if (authCode.equals(cacheAuthCode)) {
			// 清理缓存
			if (StringUtils.isNotBlank(account.getMobile())) {
				accountSecurityCacheService.invalidMobileCode(Security.getToken(), Security.getToken() + ".old");
				accountSecurityCacheService.invalidMobileCode(Security.getToken(), account.getMobile());
			}
			accountSecurityCacheService.invalidMobileCode(Security.getToken(), Security.getToken());
			accountSecurityCacheService.invalidMobileCode(Security.getToken(), mobile);
			// 更新绑定的手机
			accountService.updateAccountMobileStatus(mobile, Status.ENABLED, Security.getUserId());

			// 手机绑定动作
			userActionService.asyncAction(UserAction.BIND_MOBILE, Security.getUserId(), null);
			// 绑定手机任务 by peng.zhao
			// 只有学生才有此任务
			if (UserType.STUDENT == Security.getUserType()) {
				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101000006);
				messageObj.put("userId", Security.getUserId());
				messageObj.put("isClient", true);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());
			}

			return new Value();
		} else {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
	}

	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "sendOldBindEmail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sendOldBindEmail() {
		Account account = accountService.getAccount(Security.getAccountId());
		if (StringUtils.isNotBlank(account.getEmail())) {
			String authCode = accountSecurityCacheService.getEmailCode(Security.getToken(),
					Security.getToken() + ".old");
			if (StringUtils.isBlank(authCode)) {
				authCode = VerifyCodes.smsCode(6);
				ValueMap vm = ValueMap.value("authCode", authCode).put("name",
						userService.getUser(Security.getUserId()).getName());
				messageSender.send(new EmailPacket(account.getEmail(), 11000011, vm));
				accountSecurityCacheService.setEmailCode(Security.getToken(), Security.getToken() + ".old", authCode, 2,
						TimeUnit.MINUTES);
				accountSecurityCacheService.setEmailCode(Security.getToken(), account.getEmail(), authCode, 5,
						TimeUnit.MINUTES);
				logger.info("email code is:{}", authCode);
			} else {
				logger.info("last email code is:{}", authCode);
			}
			return new Value();
		} else {
			return new Value(new ServerException());
		}
	}

	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "checkOldBindEmail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkOldBindEmail(String authCode) {
		if (StringUtils.isBlank(authCode)) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
		Account account = accountService.getAccount(Security.getAccountId());
		if (StringUtils.isNotBlank(account.getEmail())) {
			String cacheAuthCode = accountSecurityCacheService.getEmailCode(Security.getToken(), account.getEmail());
			if (authCode.equals(cacheAuthCode)) {
				return new Value();
			} else {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
			}
		} else {
			return new Value(new ServerException());
		}
	}

	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "sendBindEmail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sendBindEmail(String email) {
		if (StringUtils.isBlank(email)) {
			return new Value(new IllegalArgException());
		}
		try {
			ValidateUtils.validateEmail(email);
		} catch (AccountException e) {
			return new Value(e);
		}
		Account account = accountService.getAccount(Security.getAccountId());
		if (account != null && email.equals(account.getEmail())) {// 新邮箱不能和老的邮箱一样
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_EMAIL_EXIST));
		}
		Account existAccount = accountService.getAccount(GetType.EMAIL, email);
		if (existAccount != null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_EMAIL_EXIST));
		}

		String authCode = accountSecurityCacheService.getEmailCode(Security.getToken(), Security.getToken());
		if (StringUtils.isBlank(authCode)) {
			authCode = VerifyCodes.smsCode(6);
			ValueMap vm = ValueMap.value("authCode", authCode).put("name",
					userService.getUser(Security.getUserId()).getName());
			messageSender.send(new EmailPacket(email, 11000012, vm));

			accountSecurityCacheService.setEmailCode(Security.getToken(), Security.getToken(), authCode, 2,
					TimeUnit.MINUTES);
			accountSecurityCacheService.setEmailCode(Security.getToken(), email, authCode, 5, TimeUnit.MINUTES);
			logger.info("email code is:{}", authCode);
		} else {
			logger.info("last email code is:{}", authCode);
		}
		return new Value();

	}

	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "bindEmail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value bindEmail(String oldEmailAuthCode, String email, String authCode) {
		if (StringUtils.isBlank(email) || StringUtils.isBlank(authCode)) {
			return new Value(new IllegalArgException());
		}
		Account account = accountService.getAccount(Security.getAccountId());
		if (account != null && StringUtils.isNotBlank(account.getEmail()) && StringUtils.isBlank(oldEmailAuthCode)) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
		if (StringUtils.isNotBlank(account.getEmail())) {
			String cacheOldEmailAuthCode = accountSecurityCacheService.getEmailCode(Security.getToken(),
					account.getEmail());
			if (!oldEmailAuthCode.equals(cacheOldEmailAuthCode)) {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
			}
		}
		String cacheAuthCode = accountSecurityCacheService.getEmailCode(Security.getToken(), email);
		if (authCode.equals(cacheAuthCode)) {
			// 清理缓存
			if (StringUtils.isNotBlank(account.getEmail())) {
				accountSecurityCacheService.invalidEmailCode(Security.getToken(), Security.getToken() + ".old");
				accountSecurityCacheService.invalidEmailCode(Security.getToken(), account.getEmail());
			}
			accountSecurityCacheService.invalidEmailCode(Security.getToken(), Security.getToken());
			accountSecurityCacheService.invalidEmailCode(Security.getToken(), email);
			// 更新绑定的邮箱
			accountService.updateAccountEmailStatus(email, Status.ENABLED, Security.getUserId());
			return new Value();
		} else {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
	}

}
