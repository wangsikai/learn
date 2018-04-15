package com.lanking.uxb.service.user.resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.mq.common.constants.MqSessionRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.common.baseData.PasswordQuestion;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.AccountPasswordQuestion;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.code.api.PasswordQuestionService;
import com.lanking.uxb.service.code.convert.PasswordQuestionConvert;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.message.util.VerifyCodes;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountPasswordQuestionService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.convert.AccountPasswordQuestionConvert;
import com.lanking.uxb.service.user.value.VPassWordScene;

/**
 * 忘记密码相关接口
 * 
 * @since 2.1
 * @author wangsenhao
 * @version 2015年6月15日
 */
@RestController
@RequestMapping("account/pwd/2")
public class Password2Controller {
	private Logger logger = LoggerFactory.getLogger(Password2Controller.class);
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private AccountPasswordQuestionService apqService;
	@Autowired
	private AccountPasswordQuestionConvert apqConvert;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private PasswordQuestionService pqService;
	@Autowired
	private PasswordQuestionConvert pqConvert;
	@Autowired
	private MqSender mqSender;

	/**
	 * 检测用户名/手机号/邮箱信息是否正确
	 * 
	 * @since 2.1
	 * @param value
	 *            用户名/手机号/邮箱
	 * @param verifyCode
	 *            验证码
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "check_name", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkUser(@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "verifyCode", required = false) String verifyCode) {
		try {
			if (StringUtils.isBlank(value)) {
				throw new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST);
			}
			Account account = null;
			if (value.indexOf("@") != -1) {
				account = accountService.getAccount(GetType.EMAIL, value);
			} else {
				try {
					ValidateUtils.validateName(value);
					account = accountService.getAccount(GetType.NAME, value);
				} catch (AccountException a) {
					account = accountService.getAccount(GetType.MOBILE, value);
				}
			}
			if (account == null) {
				throw new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST);
			} else {
				if (account.getStatus() == Status.DISABLED) {
					throw new AccountException(AccountException.ACCOUNT_FORBIDDEN);
				}
			}
			// 验证码是否正确
			if (!Security.checkVerifyCode(verifyCode)) {
				return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
			}
			accountCacheService.setPasswordForget(Security.getToken(), account.getId().toString());
			return new Value();
		} catch (AccountException e) {
			accountCacheService.invalidPasswordForget(Security.getToken());
			return new Value(e);
		}

	}

	/**
	 * 获取密保信息
	 * 
	 * @since 2.1
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getPwdInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getPwdInfo() {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		Account account = accountService.getAccount(Long.parseLong(accountId));
		VPassWordScene vPwdScene = new VPassWordScene();
		if (account.getMobile() != null && account.getMobile().trim().length() > 0) {
			vPwdScene.setIsMobile(true);
			vPwdScene.setMobileShow(account.getMobile().substring(0, 2) + "*******" + account.getMobile().substring(9));
		}
		if (account.getEmail() != null && account.getEmail().trim().length() > 0) {
			vPwdScene.setIsEmail(true);
			vPwdScene.setEmailShow(account.getEmail().substring(0, 2) + "***"
					+ account.getEmail().substring(account.getEmail().indexOf("@") - 1));
		}
		// getUserByAccountId
		User user = accountService.getUserByAccountId(Long.parseLong(accountId));
		// 老师不做密保问题
		if (user.getUserType() == UserType.STUDENT) {
			List<AccountPasswordQuestion> apqs = apqService.findByAccountId(account.getId());
			if (!apqs.isEmpty()) {
				vPwdScene.setIsPwdQuestion(true);
				vPwdScene.setPwdQuestionList(apqConvert.to(apqs));
			} else {
				vPwdScene.setIsPwdQuestion(false);
			}
		}
		return new Value(vPwdScene);
	}

	/**
	 * 获取用户名称,并验证
	 * 
	 * @since2.1
	 * 
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "checkAccountName", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkAccountName() {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		Account account = accountService.getAccount(Long.parseLong(accountId));
		return new Value(account.getName());
	}

	/**
	 * 给邮箱发送验证码<br>
	 * (1)邮箱120S内不可以重发<br>
	 * (2)验证码有效期5min
	 * 
	 * @since 2.1
	 * @param email
	 *            邮箱号
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "setEmailVerifyCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setEmailVerifyCode(@RequestParam(value = "email", required = false) String email, Product product) {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		if (email == null) {
			Account account = accountService.getAccount(Long.parseLong(accountId));
			email = account.getEmail();
		} else {
			try {
				ValidateUtils.validateEmail(email);
			} catch (AccountException e) {
				return new Value(e);
			}
			Account account = accountService.getAccount(GetType.EMAIL, email);
			if (account == null) {
				return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
			}

		}

		String verifyCode = accountCacheService.getVerifyCode(Security.getToken(), Security.getToken() + email);
		if (StringUtils.isBlank(verifyCode)) {
			verifyCode = VerifyCodes.emailCode(6);
			if (product != null && product == Product.YOOMATH) {
				messageSender.send(new EmailPacket(email, 11000015, ValueMap.value("authCode", verifyCode)));
			} else {
				messageSender.send(new EmailPacket(email, 11000014, ValueMap.value("authCode", verifyCode)));
			}
			accountCacheService.setVerifyCode(Security.getToken(), Security.getToken() + email, verifyCode, 2,
					TimeUnit.MINUTES);
			accountCacheService.setVerifyCode(Security.getToken(), email, verifyCode, 5, TimeUnit.MINUTES);
			logger.info("setEmailVerifyCode:email code is:{}", verifyCode);
		} else {
			logger.info("setEmailVerifyCode:last email code is:{}", verifyCode);
		}
		return new Value();
	}

	/**
	 * 给手机发送验证码<br>
	 * (1)短信60S内不可以重发<br>
	 * (2)验证码有效期5min
	 * 
	 * @param mobile
	 *            手机号
	 * @since 2.1
	 * 
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "setSmsVerifyCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setSmsVerifyCode(@RequestParam(value = "mobile", required = false) String mobile, Product product) {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		if (mobile == null) {
			Account account = accountService.getAccount(Long.parseLong(accountId));
			mobile = account.getMobile();
		}
		String smsCode = accountCacheService.getVerifyCode(Security.getToken(), Security.getToken() + mobile);
		if (StringUtils.isBlank(smsCode)) {
			smsCode = VerifyCodes.smsCode(6);
			if (product != null && product == Product.YOOMATH) {
				messageSender.send(new SmsPacket(mobile, 10000009, ValueMap.value("authCode", smsCode)));
			} else {
				messageSender.send(new SmsPacket(mobile, 10000003, ValueMap.value("authCode", smsCode)));
			}
			accountCacheService.setVerifyCode(Security.getToken(), Security.getToken() + mobile, smsCode, 1,
					TimeUnit.MINUTES);
			accountCacheService.setVerifyCode(Security.getToken(), mobile, smsCode, 5, TimeUnit.MINUTES);
			logger.info("setSmsVerifyCode:sms code is:{}", smsCode);
		} else {
			logger.info("setSmsVerifyCode:last sms code is:{}", smsCode);
		}
		return new Value();
	}

	/**
	 * 验证验证码
	 * 
	 * @since 2.1
	 * @param smsCode
	 *            验证码
	 * @param value
	 *            手机或邮箱的值
	 * @param type
	 *            需要验证的类型
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "checkSmscode", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkSmscode(@RequestParam(value = "smsCode") String smsCode,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "type", required = false) GetType type) {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		if (type != null && value == null) {
			Account account = accountService.getAccount(Long.parseLong(accountId));
			if (type == GetType.EMAIL) {
				value = account.getEmail();
			} else if (type == GetType.MOBILE) {
				value = account.getMobile();
			}
		}
		String rightSmsCode = accountCacheService.getVerifyCode(Security.getToken(), value);
		if (smsCode.equals(rightSmsCode)) {
			accountCacheService.invalidVerifyCode(Security.getToken(), value);
			accountCacheService.setResetPasswordCodeRight(Security.getToken(), accountId);
			return new Value();
		} else {
			// accountCacheService.invalidResetPasswordCodeRight(Security.getToken());
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}
	}

	/**
	 * 验证学生的密保问题
	 * 
	 * @since 2.1
	 * @param questionFirst
	 * @param answerFirst
	 * @param questionSecond
	 * @param answerSecond
	 * @param questionThird
	 * @param answerThird
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "checkPwdQues", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkPwdQues(@RequestParam(value = "questionFirst") int questionFirst,
			@RequestParam(value = "answerFirst") String answerFirst,
			@RequestParam(value = "questionSecond") int questionSecond,
			@RequestParam(value = "answerSecond") String answerSecond,
			@RequestParam(value = "questionThird") int questionThird,
			@RequestParam(value = "answerThird") String answerThird) {
		if (StringUtils.isBlank(answerFirst) || StringUtils.isBlank(answerSecond) || StringUtils.isBlank(answerThird)) {
			return new Value(new IllegalArgException());
		}
		int answerFirstLen = StringUtils.getJsUnicodeLength(answerFirst);
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		List<AccountPasswordQuestion> apqs = apqService.findByAccountId(Long.parseLong(accountId));
		boolean right = true;
		List<Integer> wrong = Lists.newArrayList();
		for (AccountPasswordQuestion p : apqs) {
			if (p.getPasswordQuestionCode() == questionFirst) {
				if (!p.getAnswer().equals(answerFirst)) {
					right = false;
					wrong.add(questionFirst);
				}
			} else if (p.getPasswordQuestionCode() == questionSecond) {
				if (!p.getAnswer().equals(answerSecond)) {
					right = false;
					wrong.add(questionSecond);
				}
			} else if (p.getPasswordQuestionCode() == questionThird) {
				if (!p.getAnswer().equals(answerThird)) {
					right = false;
					wrong.add(questionThird);
				}
			} else {
				right = false;
				break;
			}
		}
		return new Value(wrong);

	}

	/**
	 * 修改密码
	 * 
	 * @since 2.1
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "resetPassWord", method = { RequestMethod.POST, RequestMethod.GET })
	public Value resetPassWord(@RequestParam(value = "password1", required = false) String password1,
			@RequestParam(value = "password2", required = false) String password2,
			@RequestParam(value = "strength", required = false) Integer strength) {
		if (!password1.equals(password2)) {
			return new Value(new AccountException(AccountException.ACCOUNT_2PWD_NOT_CONSISTENT));
		}
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		Account account = accountService.updatePassword(Long.parseLong(accountId), password1, strength);
		JSONObject jo = new JSONObject();
		jo.put("userId", accountService.getUserByAccountId(account.getId()).getId());
		jo.put("excludeToken", Security.getToken());
		mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
				MQ.builder().data(jo).build());
		return new Value();
	}

	/**
	 * 设置密保问题接口
	 * 
	 * @since 2.1
	 * @return
	 */

	@RequestMapping(value = { "list_account_password_question" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value listAccountPasswordQuestion() {
		List<AccountPasswordQuestion> ps = accountCacheService.getFirstAccountPasswordQuestion(Security.getToken());
		if (ps == null) {
			return new Value(new NoPermissionException());
		}
		List<Integer> codes = Lists.newArrayList();
		for (AccountPasswordQuestion p : ps) {
			codes.add(p.getPasswordQuestionCode());
		}
		List<PasswordQuestion> list = pqService.mgetList(codes);
		List<PasswordQuestion> ret = Lists.newArrayList();
		ret.add(list.get(0));
		ret.add(list.get(1));
		ret.add(list.get(2));
		return new Value(pqConvert.to(ret));
	}
}
