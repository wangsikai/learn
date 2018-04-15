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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.message.util.VerifyCodes;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.school.jlms.resource.JLMSUser;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * 九龙登录的相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2017年10月27日
 */
@RestController
@RequestMapping("account/2/jlms")
public class Register2JLMSController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

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

	/**
	 * 发送相应的验证码
	 * 
	 * @since 2.1
	 * @param target
	 *            接受对象
	 * @param type
	 *            类型(name|email有效)
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "send_verify_code" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value sendVerifyCode(String target, GetType type, UserType userType, Product product) {
		if (StringUtils.isBlank(target) || (type == null || type == GetType.NAME || type == GetType.PASSWORD)
				|| (userType != UserType.TEACHER)) {
			return new Value(new IllegalArgException());
		}

		// 校验账户
		Value checkValue = null;
		if (type == GetType.EMAIL) {
			checkValue = this.checkContact("", target, userType, product);
		} else {
			checkValue = this.checkContact(target, "", userType, product);
		}
		Map<String, Object> map = (Map<String, Object>) checkValue.getRet();
		int flag = (Integer) map.get("flag");
		if (flag == 2) {
			return checkValue;
		}

		String verifyCode = accountCacheService.getVerifyCode(Security.getToken(), Security.getToken() + target);
		if (StringUtils.isBlank(verifyCode)) {// send again
			verifyCode = VerifyCodes.emailCode(6);
			if (type == GetType.EMAIL) {
				try {
					ValidateUtils.validateEmail(target);
				} catch (AccountException e) {
					return new Value(e);
				}
				if (product != null && product == Product.YOOMATH) {
					messageSender.send(new EmailPacket(target, 11000006,
							ValueMap.value("authCode", verifyCode).put("userType", userType.getCnName())));
				} else {
					messageSender.send(new EmailPacket(target, 11000005,
							ValueMap.value("authCode", verifyCode).put("userType", userType.getCnName())));
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
					messageSender.send(new SmsPacket(target, 10000008, ValueMap.value("authCode", verifyCode)));
				} else {
					messageSender.send(new SmsPacket(target, 10000002, ValueMap.value("authCode", verifyCode)));
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
		return new Value(map);
	}

	/**
	 * 检测验证码准确性
	 * 
	 * @since 2.1
	 * @param mobile
	 *            手机号码
	 * @param email
	 *            电子邮件
	 * @param verifyCode
	 *            验证码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "check_verify_code" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value checkVerifyCode(String mobile, String email, String verifyCode, UserType userType, Product source) {
		String code = "";
		if (StringUtils.isNotBlank(mobile)) {
			code = accountCacheService.getVerifyCode(Security.getToken(), mobile);
		} else if (StringUtils.isNotBlank(email)) {
			code = accountCacheService.getVerifyCode(Security.getToken(), email);
		}
		if (StringUtils.isBlank(verifyCode) || !verifyCode.equals(code)) {
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", 0);

		// 找到既有账户
		Account account = null;
		if (StringUtils.isNotBlank(mobile)) {
			account = accountService.getAccount(GetType.MOBILE, mobile);
		} else if (StringUtils.isNotBlank(email)) {
			account = accountService.getAccount(GetType.EMAIL, email);
		}
		if (account != null) {
			map.put("flag", 1);
		}
		map.put("account", account);

		Integer credentialValue = Security.getSession().getAttrSession().getIntAttr("credentialType"); // 需要绑定的凭证类型
		if (account != null && credentialValue != null) {
			CredentialType credentialType = CredentialType.findByValue(credentialValue);
			Credential credential = credentialService.getCredentialByAccountId(source, credentialType, account.getId());
			if (credentialType == CredentialType.JLMS && null != credential) {
				JLMSUser jlmsUser = Security.getSession().getAttrSession().getObject("jlmsUser", JLMSUser.class);
				if (!jlmsUser.getUserid().equals(credential.getUid())) {
					// 不允许多个九龙用户，绑定同一个本地账户
					map.put("flag", 2);
				}
			}
		}

		return new Value(map);
	}

	/**
	 * 注册接口
	 * 
	 * @since 2.1
	 * @param form
	 * @param request
	 *            {@link HttpServletRequest}
	 * @param response
	 *            {@link HttpServletResponse}
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "create" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value register(RegisterForm form, HttpServletRequest request, HttpServletResponse response) {
		int regflag = 1; // 第三方注册状态，1：系统无账户对应，2：系统有账户对应
		try {
			if (form.getType() == UserType.TEACHER) {
				if (StringUtils.isBlank(form.getMobile()) && StringUtils.isBlank(form.getEmail())) {
					throw new IllegalArgException();
				}

				if (form.getAccountId() == null && (form.getSubjectCode() == null || form.getPhaseCode() == null)) {
					throw new IllegalArgException();
				}
			}

			// 默认九龙中学
			form.setSchoolId(40027120);
			// 默认初中
			form.setPhaseCode(2);
			form.setSubjectCode(202);
			// 默认渠道
			form.setChannelCode(10003);

			String target = null;
			if (StringUtils.isNotBlank(form.getMobile())) {
				target = form.getMobile();
			} else if (StringUtils.isNotBlank(form.getEmail())) {
				target = form.getEmail();
			}

			Account account = null;
			if (form.getAccountId() != null) {
				regflag = 2;
				account = accountService.getAccount(form.getAccountId());
			}
			if (null == account) {
				account = accountService.createAccount2(form, true);
			}

			User user = null;

			// 保存第三方凭证.
			Credential credential = credentialService.getCredentialByPersonId(form.getSource(),
					form.getCredentialType(), form.getUid());
			Date date = new Date();
			if (credential == null) {
				credential = new Credential();
				credential.setAccountId(account.getId());
				credential.setCreateAt(date);
				credential.setType(form.getCredentialType());
				credential.setUid(form.getUid());
				credential.setName(form.getThirdName());
				user = accountService.getUserByAccountId(account.getId());
			} else {
				user = accountService.getUserByAccountId(credential.getAccountId());
			}
			credential.setEndAt(null);
			credential.setToken(form.getToken());
			credential.setUpdateAt(date);
			credential.setProduct(form.getSource());
			credential.setUserId(user.getId());

			// 调用新的凭证创建方法，金币成长值在方法内部处理
			credentialService.save(credential, false, user.getUserType());

			user.setLoginSource(form.getSource());
			accountService.handleLogin(user, request, response);
			// delete verify code cache
			accountCacheService.invalidVerifyCode(Security.getToken(), target);
			return new Value(regflag);
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	/**
	 * 检测联系方式.
	 * 
	 * @since 2.1
	 * @param mobile
	 *            手机号码
	 * @param email
	 *            电子邮件
	 * @param userType
	 *            用户类型
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "check_contact" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value checkContact(String mobile, String email, UserType userType, Product source) {
		if (StringUtils.isBlank(mobile) && StringUtils.isBlank(email)) {
			throw new MissingArgumentException();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", 0);

		// 找到既有账户
		Account account = null;
		if (StringUtils.isNotBlank(mobile)) {
			account = accountService.getAccount(GetType.MOBILE, mobile);
		} else if (StringUtils.isNotBlank(email)) {
			account = accountService.getAccount(GetType.EMAIL, email);
		}
		if (account != null) {
			map.put("flag", 1);
		}
		map.put("account", account);

		Integer credentialValue = Security.getSession().getAttrSession().getIntAttr("credentialType"); // 需要绑定的凭证类型
		if (account != null && credentialValue != null) {
			CredentialType credentialType = CredentialType.findByValue(credentialValue);
			Credential credential = credentialService.getCredentialByAccountId(source, credentialType, account.getId());
			if (credentialType == CredentialType.JLMS && null != credential) {
				JLMSUser jlmsUser = Security.getSession().getAttrSession().getObject("jlmsUser", JLMSUser.class);
				if (!jlmsUser.getUserid().equals(credential.getUid())) {
					// 不允许多个九龙用户，绑定同一个本地账户
					map.put("flag", 2);
				}
			}
		}

		return new Value(map);
	}
}
