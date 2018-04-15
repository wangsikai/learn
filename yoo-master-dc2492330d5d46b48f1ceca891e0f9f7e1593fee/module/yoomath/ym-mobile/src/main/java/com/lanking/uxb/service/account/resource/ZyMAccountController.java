package com.lanking.uxb.service.account.resource;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.cache.ZyMAccountCacheService;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.account.value.VMSession;
import com.lanking.uxb.service.account.value.VMStuSession;
import com.lanking.uxb.service.account.value.VMTeaSession;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.session.api.DeviceService;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.value.VUserProfile;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

/**
 * 悠数学移动端(登录登出接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/account")
public class ZyMAccountController {

	@Autowired
	private SessionService sessionService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private ZyMAccountCacheService accountCacheService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private ZyHomeworkClassService hkClazzService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;

	/**
	 * 移动端登录接口
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return {@link Value}
	 */
	@RestController(token = true)
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "login", method = { RequestMethod.POST, RequestMethod.GET })
	public Value login(@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password, HttpServletRequest request,
			HttpServletResponse response, @RequestHeader(value = "APP", required = false) YooApp app) {
		if (username == null || password == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
		}
		WebUtils.addCookie(request, response, Cookies.LOGIN_WRONG_TIME, String.valueOf(0));
		long passwordWrongTime = accountCacheService.getPasswordWrongTime(Security.getToken() + "_" + username);
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
		if (account == null || account.getStatus() == Status.DELETED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
		}
		try {
			User user = accountService.getUserByAccountId(account.getId());
			user.setLoginSource(Product.YOOMATH);
			if (app == null || app == YooApp.MATH_STUDENT) {
				if (user.getUserType() != UserType.STUDENT) {
					if (user.getUserType() == UserType.TEACHER) {
						throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT, "教师",
								"教师");
					} else if (user.getUserType() == UserType.PARENT) {
						throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT, "家长",
								"家长");
					} else {
						throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT, user
								.getUserType().getCnName(), user.getUserType().getCnName());
					}
				}
			} else {
				if (user.getUserType() != UserType.TEACHER) {
					if (user.getUserType() == UserType.STUDENT) {
						throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT, "学生",
								"学生");
					} else if (user.getUserType() == UserType.PARENT) {
						throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT, "家长",
								"家长");
					} else {
						throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT, user
								.getUserType().getCnName(), user.getUserType().getCnName());
					}
				}
			}

			if (user.getStatus() == Status.DISABLED || account.getStatus() == Status.DISABLED) {
				Parameter parameter = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
				throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_FORBIDDEN,
						parameter == null ? "" : parameter.getValue());
			}
			if (user.getStatus() == Status.DELETED) {
				throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG);
			}

			String superPassword = Env.getDynamicString("account.password.super");
			if (StringUtils.isNotBlank(superPassword) && superPassword.equals(password)) {
				// 超级密码直接过
			} else {
				if (!account.getPassword().equals(Codecs.md5Hex(password.getBytes()))) {
					passwordWrongTime = accountCacheService.incrPasswordWrongTime(Security.getToken() + "_" + username);
					WebUtils.addCookie(request, response, Cookies.LOGIN_WRONG_TIME, String.valueOf(passwordWrongTime));
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
				}
			}

			accountCacheService.invalidPasswordWrongTime(Security.getToken() + "_" + username);
			WebUtils.addCookie(request, response, Cookies.LOGIN_WRONG_TIME, "0");

			accountService.handleLogin(user, request, response);
			// 登录成功后返回当前会话信息
			VMSession session = null;

			VUserProfile up = userProfileConvert.to(user);
			// 凭证列表
			List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
			List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, up.getAccount().getId());
			for (Credential credential : credentials) {
				credentialTypes.add(credential.getType());
			}
			up.getAccount().setCredentialTypes(credentialTypes);

			if (up.getType() == UserType.STUDENT) {
				session = new VMStuSession(up);
				((VMStuSession) session).setNeedSetTextbook(up.getS().getTextbook() == null);
			} else if (up.getType() == UserType.TEACHER) {
				// 非导入用户可以修改一次用户名 sprint72
				if (user.isImport0()) {
					up.getAccount().setNameUpdateStatus(Status.DISABLED);
				}

				session = new VMTeaSession(up);
				// 2017.7.19 流程修改，老师不需要一定有班级,兼容之前版本，不删除属性
				((VMTeaSession) session).setNeedCreateClass(false);
				// 登录判断当前用户账户信息是否完善(阶段/真实姓名/版本/教材),不完善需要完善
				if (up.getT().getPhase() == null || up.getName() == null || up.getT().getTextbook() == null
						|| up.getT().getTextbookCategory() == null) {
					((VMTeaSession) session).setNeedPerfectData(true);
				}
				// 是否需要重置密码，同于学生端根据passwordStatus，判断
				if (up.getT().getPhase() != null && up.getT().getTextbookCategory() != null) {
					int phaseCode = up.getT().getPhase().getCode();
					int categoryCode = up.getT().getTextbookCategory().getCode();
					int subjectCode = phaseCode == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
							: SubjectService.PHASE_2_MATH;
					up.getT().getTextbookCategory()
							.setTextbooks(tbConvert.to(tbService.find(phaseCode, categoryCode, subjectCode)));
				}
			}

			// 登录动作
			userActionService.action(UserAction.LOGIN, user.getId(), null);

			return new Value(session);
		} catch (YoomathMobileException e) {
			return new Value(e);
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	/**
	 * 移动端登出接口
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return {@link Value}
	 */
	@RestController(clearToken = true)
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = { "logout" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value logout(HttpServletRequest request, HttpServletResponse response, String token) {
		if (Security.isLogin()) {
			sessionService.offline(request, response);
		}
		deviceService.unregister(token, Product.YOOMATH);
		WebUtils.removeCookie(request, response, Cookies.REMEMBER_USERTYPE);
		return new Value();
	}
}
