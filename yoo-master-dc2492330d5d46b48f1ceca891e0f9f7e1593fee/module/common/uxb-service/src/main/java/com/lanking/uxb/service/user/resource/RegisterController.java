package com.lanking.uxb.service.user.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.user.util.SecurityUtils;

/**
 * 注册相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
@RestController
@RequestMapping("account")
public class RegisterController {

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private IndexService indexService;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "check" }, method = RequestMethod.POST)
	public Value check(@RequestParam("type") GetType type, @RequestParam("value") String value) {
		try {
			if (type == GetType.NAME) {
				ValidateUtils.validateName(value);
			} else if (type == GetType.EMAIL) {
				ValidateUtils.validateEmail(value);
			} else if (type == GetType.MOBILE) {
				ValidateUtils.validateMobile(value);
			} else if (type == GetType.PASSWORD) {
				ValidateUtils.validatePassword(value);
			}
		} catch (AccountException e) {
			return new Value(e);
		}
		if (type != GetType.PASSWORD && accountService.getAccount(type, value) != null) {
			if (type == GetType.NAME) {
				return new Value(new AccountException(AccountException.ACCOUNT_NAME_EXIST));
			} else if (type == GetType.EMAIL) {
				return new Value(new AccountException(AccountException.ACCOUNT_EMAIL_EXIST));
			} else if (type == GetType.MOBILE) {
				return new Value(new AccountException(AccountException.ACCOUNT_MOBILE_EXIST));
			}
		}
		return new Value();
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "create" }, method = RequestMethod.POST)
	public Value register(RegisterForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			Account account = accountService.createAccount(form);

			if (form.getType() == UserType.TEACHER) {
				// send active email
				long timestamp = System.currentTimeMillis();
				String security = SecurityUtils.accountActiveSecret(form.getEmail(), timestamp);
				accountCacheService.setActiveSecurity(form.getEmail(), security);

				String securityUrl = Env.getString("account.active.url", new Object[] { security, form.getEmail(),
						timestamp });
				ValueMap vm = ValueMap.value();
				vm.put("name", account.getName());
				vm.put("userType", form.getType().getCnName());
				vm.put("email", account.getEmail());
				vm.put("url", securityUrl);
				messageSender.send(new EmailPacket(form.getEmail(), 11000007, vm));

				return new Value(Env.getString("account.create.success.msg", new Object[] { form.getEmail() }));
			} else if (form.getType() == UserType.STUDENT) {
				accountService.handleLogin(accountService.getUserByAccountId(account.getId()), request, response);
				return new Value();
			}
			return new Value();
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "send_active_email" }, method = RequestMethod.POST)
	public Value sendActiveMail(@RequestParam(value = "email", required = false) String email,
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
			User user = accountService.getUserByAccountId(account.getId());
			if (user.getUserType() == UserType.TEACHER) {
				long timestamp = System.currentTimeMillis();
				String security = SecurityUtils.accountActiveSecret(account.getEmail(), timestamp);
				accountCacheService.setActiveSecurity(email, security);

				String securityUrl = Env.getString("account.active.url", new Object[] { security, account.getEmail(),
						timestamp });
				ValueMap vm = ValueMap.value();
				vm.put("name", account.getName());
				vm.put("userType", user.getUserType().getCnName());
				vm.put("email", account.getEmail());
				vm.put("url", securityUrl);
				messageSender.send(new EmailPacket(account.getEmail(), 11000008, vm));

				return new Value(Env.getString("account.active.email.send.success.msg",
						new Object[] { account.getEmail() }));
			}
		}
		return new Value();
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "active" }, method = RequestMethod.POST)
	public Value active(@RequestParam("security") String security,
			@RequestParam(value = "timestamp", required = false) long timestamp, @RequestParam("email") String email) {
		try {
			String cacheSecurity = accountCacheService.getActiveSecurity(email);
			if (StringUtils.isBlank(cacheSecurity) || StringUtils.isBlank(security) || !security.equals(cacheSecurity)) {
				throw new AccountException(AccountException.ACCOUNT_ACTIVE_URL_INVALID);
			}
			if (SecurityUtils.accountActiveSecret(email, timestamp).equals(security)) {
				Account account = accountService.getAccount(GetType.EMAIL, email);
				if (account.getStatus() == Status.ENABLED) {
					throw new AccountException(AccountException.ACCOUNT_ACTIVE_URL_INVALID);
				}
				accountService.updateAccountStatus(email, Status.ENABLED);
				accountCacheService.invalidActiveSecurity(email);
			} else {
				throw new AccountException(AccountException.ACCOUNT_ACTIVE_URL_INVALID);
			}
		} catch (AccountException e) {
			return new Value(e);
		} catch (Exception e) {
			return new Value(new AccountException(AccountException.ACCOUNT_ACTIVE_URL_INVALID));
		}
		return new Value();
	}

}
