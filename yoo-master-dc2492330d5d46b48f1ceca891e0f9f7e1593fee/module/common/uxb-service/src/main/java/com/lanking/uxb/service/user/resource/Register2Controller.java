package com.lanking.uxb.service.user.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.message.util.VerifyCodes;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * 2.1的注册接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年6月15日
 */
@RestController
@RequestMapping("account/2")
public class Register2Controller {

	private Logger logger = LoggerFactory.getLogger(Register2Controller.class);

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private IndexService indexService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private UserService userService;

	/**
	 * 注册接口
	 * 
	 * @param form
	 *            注意form中的classCode实际上传的是课程码
	 * @param request
	 *            {@link HttpServletRequest}
	 * @param response
	 *            {@link HttpServletResponse}
	 * 
	 * @since 2.1
	 * @since 2.1 2015-08-24 学生注册不再要求班级码、课程码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "create" }, method = { RequestMethod.GET, RequestMethod.POST })
	@MasterSlaveDataSource(type="M")
	public Value register(RegisterForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (form.getType() == UserType.TEACHER) {
				if (StringUtils.isBlank(form.getMobile()) && StringUtils.isBlank(form.getEmail())) {
					throw new IllegalArgException();
				}
				if ((!Security.isClient() && (form.getSubjectCode() == null || form.getPhaseCode() == null))
						|| StringUtils.isBlank(form.getRealName())) {
					throw new IllegalArgException();
				}
				String verifyCode = null;
				String target = null;
				if (StringUtils.isNotBlank(form.getMobile())) {
					target = form.getMobile();
					verifyCode = accountCacheService.getVerifyCode(Security.getToken(), form.getMobile());
				} else if (StringUtils.isNotBlank(form.getEmail())) {
					target = form.getEmail();
					verifyCode = accountCacheService.getVerifyCode(Security.getToken(), form.getEmail());
				}
				if (StringUtils.isBlank(verifyCode) || !verifyCode.equals(form.getVerifyCode())) {
					throw new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID);
				}
				Account account = accountService.createAccount2(form, true);
				User user = accountService.getUserByAccountId(account.getId());
				user.setLoginSource(form.getSource());
				accountService.handleLogin(user, request, response);
				// delete verify code cache
				accountCacheService.invalidVerifyCode(Security.getToken(), target);
			} else if (form.getType() == UserType.STUDENT) {
				// 验证手机验证码
				if (StringUtils.isNotBlank(form.getMobile())) {
					String verifyCode = accountCacheService.getVerifyCode(Security.getToken(), form.getMobile());
					if (StringUtils.isBlank(verifyCode) || !verifyCode.equals(form.getVerifyCode())) {
						throw new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID);
					}
				}
				Account account = accountService.createAccount2(form, true);
				User user = accountService.getUserByAccountId(account.getId());
				user.setLoginSource(form.getSource());
				accountService.handleLogin(user, request, response);

				// 删除手机验证码
				if (StringUtils.isNotBlank(form.getMobile())) {
					accountCacheService.invalidVerifyCode(Security.getToken(), form.getMobile());
				}
			} else if (form.getType() == UserType.PARENT) {
				// TODO
			}
			return new Value(Security.getToken());
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	/**
	 * 发送相应的验证码
	 * 
	 * @since 2.1
	 * @param target
	 *            接受对象
	 * @param type
	 *            类型(name|email有效)
	 * @param userType
	 * @param product
	 *            产品
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "send_verify_code" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value sendVerifyCode(String target, GetType type, UserType userType, Product product, String location,
			@RequestHeader(value = "APP", required = false) YooApp app) {

		if (type == GetType.MOBILE) {
			// 图片验证码校验
			if (app == null
					|| (app == YooApp.MATH_STUDENT
							&& Integer.valueOf(Security.getVersion().replaceAll("\\.", "")) >= 144)
					|| (app == YooApp.MATH_TEACHER
							&& Integer.valueOf(Security.getVersion().replaceAll("\\.", "")) > 119)) {
				// v1.4.3及以前的旧版本不做校验
				Parameter parameter = parameterService.get(Product.YOOMATH,
						"account.register.pictureVerification.enable");
				boolean code = parameter == null ? false : Boolean.valueOf(parameter.getValue());
				if (code) {
					// 客户端暂时不做校验
					boolean result = Security.checkPointVerifyCode(location);
					if (!result) {
						return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
					}
				}
			}
		}

		if (StringUtils.isBlank(target) || (type == null || type == GetType.NAME || type == GetType.PASSWORD)
				|| (userType != UserType.TEACHER)) {
			return new Value(new IllegalArgException());
		}
		Account account = accountService.getAccount(type, target);
		if (account != null) {
			if (type == GetType.EMAIL) {
				return new Value(new AccountException(AccountException.ACCOUNT_EMAIL_EXIST));
			} else if (type == GetType.MOBILE) {
				return new Value(new AccountException(AccountException.ACCOUNT_MOBILE_EXIST));
			}
		}
		String verifyCode = accountCacheService.getVerifyCode(Security.getToken(), Security.getToken() + target);
		if (StringUtils.isBlank(verifyCode)) {// send again
			if (type == GetType.EMAIL) {
				try {
					ValidateUtils.validateEmail(target);
				} catch (AccountException e) {
					return new Value(e);
				}
				verifyCode = VerifyCodes.emailCode(6);
				ValueMap vm = ValueMap.value("authCode", verifyCode).put("userType", userType.getCnName());
				if (product != null && product == Product.YOOMATH) {
					messageSender.send(new EmailPacket(target, 11000001, vm));
				} else {
					messageSender.send(new EmailPacket(target, 11000000, vm));
				}
				// cache verify code
				accountCacheService.setVerifyCode(Security.getToken(), Security.getToken() + target, verifyCode, 2,
						TimeUnit.MINUTES);// 邮件两分钟可以重发
				accountCacheService.setVerifyCode(Security.getToken(), target, verifyCode, 5, TimeUnit.MINUTES);
			} else if (type == GetType.MOBILE) {
				try {
					ValidateUtils.validateMobile(target);
				} catch (AccountException e) {
					return new Value(e);
				}
				verifyCode = VerifyCodes.smsCode(6);

				if (product != null && product == Product.YOOMATH) {
					messageSender.send(new SmsPacket(target, 10000007, ValueMap.value("authCode", verifyCode)));
				} else {
					messageSender.send(new SmsPacket(target, 10000001, ValueMap.value("authCode", verifyCode)));
				}
				// cache verify code
				accountCacheService.setVerifyCode(Security.getToken(), Security.getToken() + target, verifyCode, 1,
						TimeUnit.MINUTES);// 短信一分钟可以重发
				accountCacheService.setVerifyCode(Security.getToken(), target, verifyCode, 5, TimeUnit.MINUTES);
			}
			logger.info("send_verify_code:sms or email code is {}", verifyCode);
		} else {
			logger.info("send_verify_code:last sms or email code is {}", verifyCode);
		}
		return new Value();
	}

	/**
	 * 绑定已有账户.
	 * 
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "bindAccount" })
	public Value bindAccount(String username, String password, CredentialType type, String uid, String thirdName,
			String token, Long endTime, UserType userType, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || type == null || StringUtils.isBlank(uid)
				|| StringUtils.isBlank(thirdName)) {
			return new Value(new MissingArgumentException());
		}
		Account account = null;
		if (username.contains("@")) {
			try {
				ValidateUtils.validateEmail(username);
			} catch (AccountException e) {
				return new Value(e);
			}
			account = accountService.getAccount(GetType.EMAIL, username);
		} else {
			try {
				ValidateUtils.validateMobile(username);
				account = accountService.getAccount(GetType.MOBILE, username);
			} catch (AccountException ex) {
				try {
					ValidateUtils.validateName(username);
				} catch (AccountException e) {
					return new Value(e);
				}
				account = accountService.getAccount(GetType.NAME, username);
			}
		}
		if (account == null) {
			return new Value(new AccountException(AccountException.ACCOUNT_NAME_EMAIL_NOT_EXIST));
		}
		if (account.getStatus() == Status.DISABLED) {
			return new Value(new AccountException(AccountException.ACCOUNT_NOT_ACTIVE), account.getEmail());
		}
		if (account.getStatus() == Status.DELETED) {
			return new Value(new AccountException(AccountException.ACCOUNT_DELETED));
		}
		if (!account.getPassword().equals(Codecs.md5Hex(password.getBytes()))) {
			return new Value(new AccountException(AccountException.ACCOUNT_PASSWORD_WRONG));
		}

		Map<String, Object> map = new HashMap<String, Object>(1);

		// 已有的凭证
		if (type == CredentialType.JLMS) {
			Credential credentialOld = credentialService.getCredentialByAccountId(Product.YOOMATH, type,
					account.getId());
			if (null != credentialOld && !credentialOld.getUid().toLowerCase().equals(uid.toLowerCase())) {
				map.put("bindFlag", 1);
				return new Value(map);
			}
		}

		// 第三方凭证.
		Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, type, uid);

		try {
			User user = null;
			Date date = new Date();
			if (credential == null) {
				credential = new Credential();
				credential.setAccountId(account.getId());
				credential.setCreateAt(date);
				credential.setType(type);
				credential.setUid(uid);
				credential.setName(thirdName);
				user = accountService.getUserByAccountId(account.getId());
			} else {
				user = accountService.getUserByAccountId(credential.getAccountId());
			}

			if (type == CredentialType.JLMS && userType != null
					&& user.getUserType().getValue() != userType.getValue()) {
				map.put("userType", user.getUserType());
				return new Value(map);
			}

			if (endTime != null) {
				credential.setEndAt(new Date(endTime));
			}
			credential.setToken(token);
			credential.setUpdateAt(date);
			credential.setProduct(Product.YOOMATH);
			credential.setUserId(user.getId());

			// 调用新的凭证创建方法，金币成长值在方法内部处理
			credentialService.save(credential, false, user.getUserType());

			if (type == CredentialType.JLMS) {
				// 九龙中学绑定账户，需要更新渠道和学校
				userService.updateSchool(userType, user.getId(), 40027120);
				userService.updateUserChannel(user.getId(), 10003);
			}

			user.setLoginSource(Product.YOOMATH);
			accountService.handleLogin(user, request, response);

			return new Value(map);
		} catch (AccountException e) {
			return new Value(e);
		}
	}
}
