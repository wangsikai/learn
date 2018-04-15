package com.lanking.uxb.service.user.resource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.mq.common.constants.MqSessionRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.common.baseData.PasswordQuestion;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.AccountPasswordQuestion;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
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
import com.lanking.uxb.service.user.util.SecurityUtils;

/**
 * 密码相关接口
 * 
 * @since 2.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年2月5日
 */
@RestController
@RequestMapping("account/pwd")
public class PasswordController {

	private Logger logger = LoggerFactory.getLogger(PasswordController.class);

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private PasswordQuestionService pqService;
	@Autowired
	private PasswordQuestionConvert pqConvert;
	@Autowired
	private AccountPasswordQuestionService apqService;
	@Autowired
	private AccountPasswordQuestionConvert apqConvert;
	@Autowired
	private MqSender mqSender;

	@Deprecated
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "send_find_pwd_email" }, method = { RequestMethod.POST })
	public Value sendFindPasswordEmail(@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "verifyCode", required = false) String verifyCode) {
		try {
			ValidateUtils.validateEmail(email);
		} catch (AccountException e) {
			return new Value(e);
		}
		if (!Security.checkVerifyCode(verifyCode)) {
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}
		Account account = accountService.getAccount(GetType.EMAIL, email);
		if (account == null) {
			return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
		}
		if (account.getStatus() == Status.DISABLED) {
			return new Value(new AccountException(AccountException.ACCOUNT_NOT_ACTIVE));
		}

		long timestamp = System.currentTimeMillis();
		String security = SecurityUtils.emailFindPwdSecret(email, timestamp);
		String securityUrl = Env.getString("account.findpassword.email.url",
				new Object[] { security, email, timestamp });
		ValueMap vm = ValueMap.value();
		vm.put("name", account.getName());
		vm.put("email", account.getEmail());
		vm.put("url", securityUrl);
		messageSender.send(new EmailPacket(email, 11000013, vm));
		accountCacheService.setRestPwdSecurity(email, security);

		return new Value(AccountException.SUCCEED,
				Env.getString("account.findpassword.email.msg", new Object[] { email }));
	}

	/**
	 * 1.通过修改密码链接修改密码<br>
	 * 2.检测修改密码URL的有效性
	 * 
	 * @since 2.0
	 * @param security
	 *            秘钥[URL提取]
	 * @param timestamp
	 *            时间戳[URL提取]
	 * @param email
	 *            邮箱[URL提取]
	 * @param password
	 *            密码
	 * @param pwd
	 *            密码
	 * @param checkSecurity
	 *            1表示检测URL的有效性
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "reset_pwd_by_email" }, method = { RequestMethod.POST })
	public Value resetPwdByEmail(@RequestParam(value = "security", required = false) String security,
			@RequestParam(value = "timestamp", required = false) long timestamp,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "password", required = false, defaultValue = StringUtils.EMPTY) String password,
			@RequestParam(value = "pwd", required = false, defaultValue = StringUtils.EMPTY) String pwd,
			@RequestParam(value = "checkSecurity", required = false, defaultValue = StringUtils.EMPTY) String checkSecurity) {
		try {
			if ("1".equals(checkSecurity)) {// 检测链接有效
				String cacheSecurity = accountCacheService.getRestPwdSecurity(email);
				if (StringUtils.isBlank(cacheSecurity) || StringUtils.isBlank(security)
						|| !security.equals(cacheSecurity)) {
					throw new AccountException(AccountException.ACCOUNT_URL_INVALID);
				} else {
					Account account = accountService.getAccount(GetType.EMAIL, email);
					return new Value(account.getName());
				}
			} else {
				ValidateUtils.validateEmail(email);

				String cacheSecurity = accountCacheService.getRestPwdSecurity(email);
				if (StringUtils.isBlank(cacheSecurity) || StringUtils.isBlank(security)
						|| !security.equals(cacheSecurity)) {
					throw new AccountException(AccountException.ACCOUNT_URL_INVALID);
				}
				if (!SecurityUtils.emailFindPwdSecret(email, timestamp).equals(security)) {
					return new Value(new AccountException(AccountException.ACCOUNT_URL_INVALID));
				}
				ValidateUtils.validatePassword(password);
				if (!password.equals(pwd)) {
					throw new AccountException(AccountException.ACCOUNT_2PWD_NOT_CONSISTENT);
				}
				Account account = accountService.updatePassword(email, password);
				JSONObject jo = new JSONObject();
				jo.put("userId", accountService.getUserByAccountId(account.getId()).getId());
				jo.put("excludeToken", Security.getToken());
				mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
						MQ.builder().data(jo).build());
				accountCacheService.invalidRestPwdSecurity(email);
				return new Value();
			}
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	/**
	 * 修改密码各个页面的权限检测
	 * 
	 * @since 2.0
	 * @param route
	 *            路由名称
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "check", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkForFindPassword(
			@RequestParam(value = "route", required = false, defaultValue = StringUtils.EMPTY) String route) {
		if ("forget".equals(route) || "findteacher".equals(route) || "findstudent".equals(route)) {
			String accountId = accountCacheService.getPasswordForget(Security.getToken());
			if (StringUtils.isBlank(accountId)) {
				return new Value("");
			}
			Account account = accountService.getAccount(Long.parseLong(accountId));
			if ("findteacher".equals(route)) {
				return new Value(account.getEmail());
			}
			return new Value(account.getName());
		} else if ("resetpassword".equals(route)) {
			String accountId = accountCacheService.getPasswordForget(Security.getToken());
			if (StringUtils.isBlank(accountId)) {
				return new Value("");
			}
			String _accountId = accountCacheService.getAnswerRight(Security.getToken());
			String __accountId = accountCacheService.getResetPasswordCodeRight(Security.getToken());
			if (accountId.equals(_accountId) || accountId.equals(__accountId)) {
				Account account = accountService.getAccount(Long.parseLong(accountId));
				return new Value(account.getName());
			} else {
				return new Value("");
			}
		} else if ("findPhone".equals(route)) {
			String accountId = accountCacheService.getPasswordForget(Security.getToken());
			if (StringUtils.isBlank(accountId)) {
				return new Value("");
			}
			accountCacheService.invalidAnswerRight(Security.getToken());
			Account account = accountService.getAccount(Long.parseLong(accountId));
			return new Value(
					account.getPasswordMobile().substring(0, 3) + "****" + account.getPasswordMobile().substring(7));
		}
		return new Value("");
	}

	/**
	 * 找回密码-检测用户名
	 * 
	 * @since 2.0
	 * @param username
	 *            用户名
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "check_name", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkUser(
			@RequestParam(value = "username", required = false, defaultValue = StringUtils.EMPTY) String username) {
		try {
			if (StringUtils.isBlank(username)) {
				throw new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST);
			}
			Account account = accountService.getAccount(GetType.NAME, username);
			if (account == null) {
				throw new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST);
			}
			User user = accountService.getUserByAccountId(account.getId());
			accountCacheService.setPasswordForget(Security.getToken(), account.getId().toString());
			return new Value(user.getUserType().toString());
		} catch (AccountException e) {
			accountCacheService.invalidPasswordForget(Security.getToken());
			return new Value(e);
		}

	}

	/**
	 * 通过邮箱发送重置密码邮件
	 * 
	 * @since 2.0
	 * @param email
	 *            邮箱
	 * @param verifyCode
	 *            验证码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "send_reset_email" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value sendResetEmail(@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "verifyCode", required = false) String verifyCode) {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		try {
			ValidateUtils.validateEmail(email);
		} catch (AccountException e) {
			return new Value(e);
		}
		if (!Security.checkVerifyCode(verifyCode)) {
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}
		Account account = accountService.getAccount(GetType.EMAIL, email);
		if (account == null) {
			return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
		}

		long timestamp = System.currentTimeMillis();
		String security = SecurityUtils.emailFindPwdSecret(email, timestamp);
		String securityUrl = Env.getString("account.findpassword.email.url",
				new Object[] { security, email, timestamp });
		ValueMap vm = ValueMap.value();
		vm.put("name", account.getName());
		vm.put("email", account.getEmail());
		vm.put("url", securityUrl);
		messageSender.send(new EmailPacket(email, 11000013, vm));
		accountCacheService.setRestPwdSecurity(email, security);
		accountCacheService.invalidPasswordForget(Security.getToken());
		return new Value(AccountException.SUCCEED,
				Env.getString("account.findpassword.email.msg", new Object[] { email }));
	}

	/**
	 * 忘记密保问题
	 * 
	 * @since 2.0
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "forget_answer", method = { RequestMethod.POST, RequestMethod.GET })
	public Value forgetAnswer() {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		Account account = accountService.getAccount(Long.parseLong(accountId));
		if (StringUtils.isBlank(account.getPasswordMobile())) {
			return new Value("");
		} else {
			return new Value(account.getPasswordMobile());
		}
	}

	/**
	 * 密保找回密码-获取已设置的密码问题
	 * 
	 * @since 2.0
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questions() {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		String _accountId = accountCacheService.getAnswerRight(Security.getToken());
		List<AccountPasswordQuestion> apqs = apqService.findByAccountId(Long.parseLong(accountId));
		if (accountId.equals(_accountId)) {
			return new Value(apqConvert.to(apqs, true));
		} else {
			return new Value(apqConvert.to(apqs));
		}
	}

	/**
	 * 密保找回密码-检测密码问题
	 * 
	 * @param answer
	 *            密保答案
	 * @return {@link Value}
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "check_answer", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkAnswer(@RequestParam(value = "answer") String answer) {
		List<Long> wrongList = Lists.newArrayList();
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		JSONArray array = JSON.parseArray(answer);
		Map<Long, String> userAnswers = Maps.newHashMap();
		for (Object object : array) {
			Map map = (Map) object;
			userAnswers.put(Long.parseLong(map.get("id").toString()), map.get("value").toString());
		}
		List<AccountPasswordQuestion> apqs = apqService.findByAccountId(Long.parseLong(accountId));
		for (AccountPasswordQuestion p : apqs) {
			if (!p.getAnswer().equals(userAnswers.get(p.getId()))) {
				wrongList.add(p.getId());
			}
		}
		if (wrongList.size() == 0) {
			accountCacheService.setAnswerRight(Security.getToken(), accountId);
		} else {
			accountCacheService.invalidAnswerRight(Security.getToken());
		}
		return new Value(wrongList);
	}

	/**
	 * 密保找回密码-重置密码
	 * 
	 * @since 2.0
	 * @param password
	 *            密码
	 * @param pwd
	 *            密码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "reset_password", method = { RequestMethod.POST, RequestMethod.GET })
	public Value resetPassoword(@RequestParam(value = "password") String password,
			@RequestParam(value = "pwd") String pwd) {
		try {
			String accountId = accountCacheService.getPasswordForget(Security.getToken());
			String _accountId = accountCacheService.getAnswerRight(Security.getToken());
			String __accountId = accountCacheService.getResetPasswordCodeRight(Security.getToken());
			if (StringUtils.isBlank(accountId) || !(accountId.equals(_accountId) || accountId.equals(__accountId))) {
				throw new NoPermissionException();
			}
			ValidateUtils.validatePassword(password);
			if (!password.equals(pwd)) {
				throw new AccountException(AccountException.ACCOUNT_2PWD_NOT_CONSISTENT);
			}
			Account account = accountService.updatePassword(Long.parseLong(accountId), password);
			JSONObject jo = new JSONObject();
			jo.put("userId", accountService.getUserByAccountId(account.getId()).getId());
			jo.put("excludeToken", Security.getToken());
			mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
					MQ.builder().data(jo).build());
			accountCacheService.invalidAnswerRight(Security.getToken());
			accountCacheService.invalidResetPasswordCodeRight(Security.getToken());
			accountCacheService.invalidPasswordForget(Security.getToken());
			return new Value(accountId);
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	/**
	 * 密保手机或则手机找回密码-重新发送验证码
	 * 
	 * @since 2.0
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "send_reset_smscode", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sendResetSmsCode(
			@RequestParam(value = "isPasswordMobile", required = false, defaultValue = "true") boolean isPasswordMobile) {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		String smsCode = accountCacheService.getVerifyCode(Security.getToken(), Security.getToken());
		if (StringUtils.isBlank(smsCode)) {
			smsCode = VerifyCodes.smsCode(6);
			accountCacheService.setVerifyCode(Security.getToken(), null, smsCode, 5, TimeUnit.MINUTES);
			accountCacheService.setVerifyCode(Security.getToken(), Security.getToken(), smsCode, 1, TimeUnit.MINUTES);
			if (isPasswordMobile) {
				messageSender
						.send(new SmsPacket(accountService.getAccount(Long.parseLong(accountId)).getPasswordMobile(),
								10000003, ValueMap.value("authCode", smsCode)));
			} else {
				messageSender.send(new SmsPacket(accountService.getAccount(Long.parseLong(accountId)).getMobile(),
						10000003, ValueMap.value("authCode", smsCode)));
			}
			logger.info("send_reset_smscode:sms code is:{}", smsCode);
		} else {
			logger.info("send_reset_smscode:last sms code is:{}", smsCode);
		}
		return new Value();
	}

	/**
	 * 密保手机找回密码-验证验证码
	 * 
	 * @since 2.0
	 * @param smsCode
	 *            验证码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "check_reset_smscode", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkResetSmsCode(@RequestParam(value = "smsCode") String smsCode) {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(accountId)) {
			return new Value(new NoPermissionException());
		}
		String rightSmsCode = accountCacheService.getVerifyCode(Security.getToken(), null);
		if (smsCode.equals(rightSmsCode)) {
			accountCacheService.invalidVerifyCode(Security.getToken(), null);
			accountCacheService.setResetPasswordCodeRight(Security.getToken(), accountId);
			return new Value();
		} else {
			accountCacheService.invalidResetPasswordCodeRight(Security.getToken());
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}
	}

	/**
	 * 通过原密码修改密码
	 * 
	 * @since 2.0
	 * @param oldPwd
	 *            原密码
	 * @param password
	 *            密码
	 * @param pwd
	 *            密码
	 * @param request
	 * @param response
	 * @return {@link Value}
	 */
	@RequestMapping(value = { "reset_pwd" }, method = { RequestMethod.POST })
	public Value resetPwdByPwd(@RequestParam(value = "oldPwd", required = false) String oldPwd,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "pwd", required = false) String pwd,
			@RequestParam(value = "strength", required = false, defaultValue = "2") Integer strength,
			@RequestParam(defaultValue = "true") boolean logout, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Account account = accountService.getAccountByUserId(Security.getUserId());
			PasswordStatus passwordStatus = account.getPasswordStatus();
			// passwordStatus 为disable时，表示没有密码
			if (PasswordStatus.DISABLED != passwordStatus) {
				if (StringUtils.isBlank(oldPwd)) {
					throw new AccountException(AccountException.ACCOUNT_OLDPWD_WRONG);
				}
			}
			ValidateUtils.validatePassword(password);
			if (!password.equals(pwd)) {
				throw new AccountException(AccountException.ACCOUNT_2PWD_NOT_CONSISTENT);
			}
			if (PasswordStatus.DISABLED != passwordStatus) {
				if (!account.getPassword().equals(Codecs.md5Hex(oldPwd.getBytes()))) {
					throw new AccountException(AccountException.ACCOUNT_OLDPWD_WRONG);
				}
			}
			accountService.updatePassword(account.getId(), password, strength);
			JSONObject jo = new JSONObject();
			jo.put("userId", Security.getUserId());
			jo.put("excludeToken", Security.getToken());
			mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
					MQ.builder().data(jo).build());
			if (logout) {
				response.sendRedirect("/account/logout");
			}
		} catch (AccountException e) {
			return new Value(e);
		} catch (Exception e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 设置密保问题接口
	 * 
	 * @since 2.0
	 * @return {@link Value}
	 */

	@RequestMapping(value = { "list_pwd_questions" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value listPasswordQuestion() {
		return new Value(pqConvert.to(pqService.getAll()));
	}

	/**
	 * 设置密保问题接口
	 * 
	 * @since 2.0
	 * @return {@link Value}
	 */

	@RequestMapping(value = { "set_account_pwd_question" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value setAccountPasswordQuestion(@RequestParam(value = "questionFirst") int questionFirst,
			@RequestParam(value = "answerFirst") String answerFirst,
			@RequestParam(value = "questionSecond") int questionSecond,
			@RequestParam(value = "answerSecond") String answerSecond,
			@RequestParam(value = "questionThird") int questionThird,
			@RequestParam(value = "answerThird") String answerThird) {
		if (questionFirst == questionSecond || questionSecond == questionThird || questionThird == questionFirst) {
			return new Value(new IllegalArgException());
		}
		if (StringUtils.isBlank(answerFirst) || StringUtils.isBlank(answerSecond) || StringUtils.isBlank(answerThird)) {
			return new Value(new IllegalArgException());
		}
		int answerFirstLen = StringUtils.getJsUnicodeLength(answerFirst);
		if (answerFirstLen < 4 || answerFirstLen > 38) {
			return new Value(new IllegalArgException());
		} else {
			int answerSecondLen = StringUtils.getJsUnicodeLength(answerSecond);
			if (answerSecondLen < 4 || answerSecondLen > 38) {
				return new Value(new IllegalArgException());
			} else {
				int answerThirdLen = StringUtils.getJsUnicodeLength(answerThird);
				if (answerThirdLen < 4 || answerThirdLen > 38) {
					return new Value(new IllegalArgException());
				}
			}
		}
		long accountId = accountService.getUserByUserId(Security.getUserId()).getAccountId();
		List<AccountPasswordQuestion> ps = Lists.newArrayList();
		AccountPasswordQuestion p1 = new AccountPasswordQuestion();
		p1.setAccountId(accountId);
		p1.setPasswordQuestionCode(questionFirst);
		p1.setAnswer(answerFirst);
		ps.add(p1);
		AccountPasswordQuestion p2 = new AccountPasswordQuestion();
		p2.setAccountId(accountId);
		p2.setPasswordQuestionCode(questionSecond);
		p2.setAnswer(answerSecond);
		ps.add(p2);
		AccountPasswordQuestion p3 = new AccountPasswordQuestion();
		p3.setAccountId(accountId);
		p3.setPasswordQuestionCode(questionThird);
		p3.setAnswer(answerThird);
		ps.add(p3);
		accountCacheService.setFirstAccountPasswordQuestion(Security.getToken(), ps);
		return new Value();
	}

	/**
	 * 设置密保问题接口
	 * 
	 * @since 2.0
	 * @return {@link Value}
	 */

	@RequestMapping(value = { "list_set_account_password_question" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public Value listSetAccountPasswordQuestion() {
		List<AccountPasswordQuestion> ps = accountCacheService.getFirstAccountPasswordQuestion(Security.getToken());
		if (ps == null) {
			return new Value(Lists.newArrayList());
		} else {
			for (AccountPasswordQuestion p : ps) {
				p.setId(0L);
			}
			return new Value(apqConvert.to(ps, true));
		}

	}

	/**
	 * 设置密保问题接口
	 * 
	 * @since 2.0
	 * @return {@link Value}
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
		ret.add(list.get(1));
		ret.add(list.get(0));
		ret.add(list.get(2));
		return new Value(pqConvert.to(ret));
	}

	/**
	 * 设置密保问题接口
	 * 
	 * @since 2.0
	 * @return {@link Value}
	 */

	@RequestMapping(value = { "check_account_pwd_question" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value checkAccountPasswordQuestion(@RequestParam(value = "questionFirst") int questionFirst,
			@RequestParam(value = "answerFirst") String answerFirst,
			@RequestParam(value = "questionSecond") int questionSecond,
			@RequestParam(value = "answerSecond") String answerSecond,
			@RequestParam(value = "questionThird") int questionThird,
			@RequestParam(value = "answerThird") String answerThird) {
		try {
			List<AccountPasswordQuestion> ps = accountCacheService.getFirstAccountPasswordQuestion(Security.getToken());
			if (ps == null) {
				throw new NoPermissionException();
			}
			boolean right = true;
			List<Integer> wrong = Lists.newArrayList();
			for (AccountPasswordQuestion p : ps) {
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
			if (right) {
				apqService.create(ps);
				// 删除缓存数据
				accountCacheService.invalidFirstAccountPasswordQuestion(Security.getToken());
			}
			return new Value(wrong);
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	/**
	 * 设置密码手机
	 * 
	 * @param mobile
	 *            手机
	 * @return {@link Value}
	 */

	@RequestMapping(value = { "set_password_mobile" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value setPasswordMobile(@RequestParam(value = "mobile") String mobile) {
		try {
			ValidateUtils.validateMobile(mobile);
			accountService.updatePasswordMobile(accountService.getUserByUserId(Security.getUserId()).getAccountId(),
					mobile);
			return new Value();
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	/**
	 * 通过手机号码发送验证码
	 * 
	 * @since client 1.0
	 * @param mobile
	 *            手机号码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "sendFindPwdSmsCodeByMobile" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value sendFindPwdSmsCodeByMobile(@RequestParam(value = "mobile", required = false) String mobile) {
		String accountId = accountCacheService.getPasswordForget(Security.getToken());
		if (StringUtils.isBlank(mobile)) {// 重新发送
			if (StringUtils.isBlank(accountId)) {
				return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
			} else {
				return sendResetSmsCode(false);
			}
		} else {
			Account account = accountService.getAccount(GetType.MOBILE, mobile);
			if (account == null) {
				return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
			} else {
				accountCacheService.setPasswordForget(Security.getToken(), account.getId().toString());
				return sendResetSmsCode(false);
			}
		}
	}
}
