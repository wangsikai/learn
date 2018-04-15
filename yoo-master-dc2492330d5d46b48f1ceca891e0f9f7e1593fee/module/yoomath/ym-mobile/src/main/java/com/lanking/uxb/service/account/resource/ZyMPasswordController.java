package com.lanking.uxb.service.account.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqSessionRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.common.baseData.PasswordQuestion;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.AccountPasswordQuestion;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.cache.ZyMFindPasswordCacheService;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.account.value.VMSession;
import com.lanking.uxb.service.account.value.VMStuSession;
import com.lanking.uxb.service.account.value.VMTeaSession;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.PasswordQuestionService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.PasswordQuestionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.message.util.VerifyCodes;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountPasswordQuestionService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.value.VUserProfile;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

/**
 * 移动端密码相关接口
 * 
 * @since 2.0.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年2月29日
 */
@ApiAllowed
@RestController(token = true)
@RequestMapping("zy/m/account")
public class ZyMPasswordController {

	private Logger logger = LoggerFactory.getLogger(ZyMPasswordController.class);

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private PasswordQuestionService pqService;
	@Autowired
	private PasswordQuestionConvert pqConvert;
	@Autowired
	private AccountPasswordQuestionService accountPasswordQuestionService;
	@Autowired
	private ZyMFindPasswordCacheService findPasswordCacheService;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;

	/**
	 * 重新设置密码
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param oldPwd
	 *            原始密码
	 * @param password
	 *            新密码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = { "resetPassword" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value resetPassword(String oldPwd, String password) {
		try {
			Account account = accountService.getAccountByUserId(Security.getUserId());
			if (account.getPasswordStatus() != PasswordStatus.DISABLED) {
				if (StringUtils.isBlank(oldPwd)) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_OLDPWD_WRONG);
				}
				if (!account.getPassword().equals(Codecs.md5Hex(oldPwd.getBytes()))) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_OLDPWD_WRONG);
				}
			}
			ValidateUtils.validatePassword(password);
			accountService.updatePassword(account.getId(), password, 2);
			JSONObject jo = new JSONObject();
			jo.put("userId", Security.getUserId());
			jo.put("excludeToken", Security.getToken());
			mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
					MQ.builder().data(jo).build());
		} catch (AccountException e) {
			return new Value(e);
		} catch (YoomathMobileException e) {
			return new Value(e);
		}
		return new Value();
	}

	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "ignoreResetPassword", method = { RequestMethod.GET, RequestMethod.POST })
	public Value ignoreResetPassword() {
		Account account = accountService.getAccountByUserId(Security.getUserId());
		if (account.getPasswordStatus() != PasswordStatus.SYSTEM) {
			return new Value(new NoPermissionException());
		}

		accountService.updatePasswordStatus(account.getId(), PasswordStatus.ENABLED);

		return new Value();
	}

	/**
	 * 获取系统预置的密码保护问题列表
	 * 
	 * @since 2.0.1
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = { "passwordQuestions" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value passwordQuestions() {
		return new Value(pqConvert.to(pqService.getAll()));
	}

	/**
	 * 设置密保
	 * 
	 * @since 2.0.1
	 * @param q1
	 *            第一个问题代码
	 * @param a1
	 *            第一个问题答案
	 * @param q2
	 *            第二个问题代码
	 * @param a2
	 *            第二个问题答案
	 * @param q3
	 *            第三个问题代码
	 * @param a3
	 *            第三个问题答案
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = { "setPasswordQuestions" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value setPasswordQuestions(int q1, String a1, int q2, String a2, int q3, String a3) {
		Account account = accountService.getAccountByUserId(Security.getUserId());
		if (account.getPqStatus() == Status.ENABLED) {// 已经设置过密保
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HAD_PASSWORD_QUESTION));
		}
		List<PasswordQuestion> all = pqService.getAll();
		boolean q1Exist = false;
		boolean q2Exist = false;
		boolean q3Exist = false;
		for (PasswordQuestion passwordQuestion : all) {
			if (!q1Exist && passwordQuestion.getCode().intValue() == q1) {
				q1Exist = true;
			}
			if (!q2Exist && passwordQuestion.getCode().intValue() == q2) {
				q2Exist = true;
			}
			if (!q3Exist && passwordQuestion.getCode().intValue() == q3) {
				q3Exist = true;
			}
		}
		if (!q1Exist || !q2Exist || !q3Exist || q1 == q2 || q1 == q3 || q2 == q3 || StringUtils.isBlank(a1)
				|| StringUtils.isBlank(a2) || StringUtils.isBlank(a3)) {
			return new Value(new IllegalArgException());
		}
		List<AccountPasswordQuestion> apqs = new ArrayList<AccountPasswordQuestion>(3);
		AccountPasswordQuestion apq1 = new AccountPasswordQuestion();
		apq1.setAccountId(account.getId());
		apq1.setAnswer(a1);
		apq1.setPasswordQuestionCode(q1);
		apqs.add(apq1);

		AccountPasswordQuestion apq2 = new AccountPasswordQuestion();
		apq2.setAccountId(account.getId());
		apq2.setAnswer(a2);
		apq2.setPasswordQuestionCode(q2);
		apqs.add(apq2);

		AccountPasswordQuestion apq3 = new AccountPasswordQuestion();
		apq3.setAccountId(account.getId());
		apq3.setAnswer(a3);
		apq3.setPasswordQuestionCode(q3);
		apqs.add(apq3);
		accountPasswordQuestionService.create(apqs);
		return new Value();
	}

	/**
	 * 找回密码方式
	 * 
	 * @since 2.0.1
	 * @param name
	 *            用户名(手机 | 密保 | 邮箱)
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = { "getFindPasswordType" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value getFindPasswordType(String name, @RequestHeader(value = "APP", required = false) YooApp app) {
		if (StringUtils.isBlank(name)) {
			findPasswordCacheService.setAccount(Security.getToken(), 0);
			return new Value(new IllegalArgException());
		}
		Account account = null;
		String type = null;
		if (name.contains("@")) {
			try {
				ValidateUtils.validateEmail(name);
			} catch (AccountException e) {
			}
			account = accountService.getAccount(GetType.EMAIL, name);
		} else {
			try {
				ValidateUtils.validateMobile(name);
				account = accountService.getAccount(GetType.MOBILE, name);
			} catch (AccountException ex) {
				try {
					ValidateUtils.validateName(name);
				} catch (AccountException e) {
				}
				account = accountService.getAccount(GetType.NAME, name);
			}
		}
		Map<String, Object> data = new HashMap<String, Object>(3);
		if (account == null) {
			findPasswordCacheService.setAccount(Security.getToken(), 0);
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_NOT_EXIST));
		}

		User user = accountService.getUserByAccountId(account.getId());
		if (app == null || app == YooApp.MATH_STUDENT) {
			if (user.getUserType() != UserType.STUDENT) {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_FINDPASSWORD_APP_WRONG, "教师", "教师"));
			}
		} else {
			if (user.getUserType() != UserType.TEACHER) {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_FINDPASSWORD_APP_WRONG, "学生", "学生"));
			}
		}

		if (user.getStatus() == Status.DISABLED) {
			Parameter parameter = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_FORBIDDEN,
					parameter == null ? "" : parameter.getValue()));
		}
		if (user.getStatus() == Status.DELETED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
		}

		if (StringUtils.isNotBlank(account.getMobile())) {
			type = "mobile";
			data.put("mobile", StringUtils.getMaskMobile(account.getMobile()));
		} else if (account.getPqStatus() == Status.ENABLED) {
			type = "password_question";
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(3);
			List<AccountPasswordQuestion> myList = accountPasswordQuestionService.findByAccountId(account.getId());
			for (AccountPasswordQuestion apq : myList) {
				Map<String, Object> map = new HashMap<String, Object>(2);
				map.put("code", apq.getPasswordQuestionCode());
				map.put("name", pqService.get(apq.getPasswordQuestionCode()).getName());
				list.add(map);
			}
			data.put("passwordQuestion", list);
		} else if (StringUtils.isNotBlank(account.getEmail())) {
			type = "email";
			data.put("email", StringUtils.getMaskEmail(account.getEmail()));
		} else {
			findPasswordCacheService.setAccount(Security.getToken(), 0);
			Parameter parameter = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_CANNOT_FINDPASSWORD,
							parameter == null ? "" : parameter.getValue()));
		}
		data.put("type", type);
		findPasswordCacheService.setAccount(Security.getToken(), account.getId());
		return new Value(data);
	}

	/**
	 * 发送验证码
	 * 
	 * @since 2.0.1
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = { "sendAuthCode" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value sendAuthCode() {
		long accountId = findPasswordCacheService.getAccount(Security.getToken());
		if (accountId == 0) {
			return new Value(new IllegalArgException());
		}
		Account account = accountService.getAccount(accountId);
		String type = null;
		String target = null;
		if (StringUtils.isNotBlank(account.getMobile())) {
			type = "mobile";
			target = account.getMobile();
		} else if (account.getPqStatus() == Status.ENABLED) {
			type = "password_question";
		} else if (StringUtils.isNotBlank(account.getEmail())) {
			type = "email";
			target = account.getEmail();
		}
		if ("mobile".equals(type) || "email".equals(type)) {
			String authCode = findPasswordCacheService.getAuthCode(Security.getToken(), target);
			if ("mobile".equals(type)) {// 手机
				if (StringUtils.isBlank(authCode)) {
					authCode = VerifyCodes.smsCode(6);
					messageSender.send(new SmsPacket(target, 10000009, ValueMap.value("authCode", authCode)));
					findPasswordCacheService.setAuthCode(Security.getToken(), target, authCode, 1, TimeUnit.MINUTES);
					findPasswordCacheService.setAuthCode(Security.getToken(), Security.getToken(), authCode, 5,
							TimeUnit.MINUTES);
					logger.info("sms code is:{}", authCode);
				} else {
					logger.info("last sms code is:{}", authCode);
				}
			} else if ("email".equals(type)) {
				if (StringUtils.isBlank(authCode)) {
					authCode = VerifyCodes.emailCode(6);
					messageSender.send(new EmailPacket(target, 11000015, ValueMap.value("authCode", authCode)));
					findPasswordCacheService.setAuthCode(Security.getToken(), target, authCode, 2, TimeUnit.MINUTES);
					findPasswordCacheService.setAuthCode(Security.getToken(), Security.getToken(), authCode, 5,
							TimeUnit.MINUTES);
					logger.info("email code is:{}", authCode);
				} else {
					logger.info("last email code is:{}", authCode);
				}
			}
			return new Value();
		} else {
			return new Value(new IllegalArgException());
		}
	}

	/**
	 * check验证码
	 * 
	 * @since 2.0.1
	 * @param authCode
	 *            验证码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = { "checkAuthCode" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value checkAuthCode(String authCode) {
		boolean check = true;
		String cacheAuthCode = findPasswordCacheService.getAuthCode(Security.getToken(), Security.getToken());
		if (StringUtils.isBlank(cacheAuthCode)) {
			check = false;
		} else {
			check = cacheAuthCode.equals(authCode);
		}
		if (check) {
			Map<String, Object> map = new HashMap<String, Object>(1);
			long accountId = findPasswordCacheService.getAccount(Security.getToken());
			map.put("username", accountService.getAccount(accountId).getName());
			return new Value(map);
		} else {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
	}

	/**
	 * 通过验证码重设密码
	 * 
	 * @since 2.0.1
	 * @param authCode
	 *            验证码
	 * @param password
	 *            密码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = { "resetPasswordByAuthCode" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value resetPasswordByAuthCode(String authCode, String password, HttpServletRequest request,
			HttpServletResponse response, @RequestHeader(value = "APP", required = false) YooApp app) {
		long accountId = findPasswordCacheService.getAccount(Security.getToken());
		if (accountId == 0) {
			return new Value(new IllegalArgException());
		}
		boolean check = true;
		String cacheAuthCode = findPasswordCacheService.getAuthCode(Security.getToken(), Security.getToken());
		if (StringUtils.isBlank(cacheAuthCode)) {
			check = false;
		} else {
			check = cacheAuthCode.equals(authCode);
		}
		if (check) {
			try {
				ValidateUtils.validatePassword(password);
			} catch (AccountException e) {
				return new Value(e);
			}
			accountService.updatePassword(accountId, password, 2);
			User user = accountService.getUserByAccountId(accountId);
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
						throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
								user.getUserType().getCnName(), user.getUserType().getCnName());
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
						throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
								user.getUserType().getCnName(), user.getUserType().getCnName());
					}
				}
			}
			if (user.getStatus() == Status.DISABLED) {
				Parameter parameter = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_FORBIDDEN),
						parameter == null ? "" : parameter.getValue());
			}
			if (user.getStatus() == Status.DELETED) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
			}
			accountService.handleLogin(user, request, response);

			accountCacheService.invalidLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_WRONG_TIME, "0");

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
				session = new VMTeaSession(up);
				// 找回密码---判断当前用户账户信息是否完善(阶段/真实姓名/版本/教材),不完善需要完善
				if (up.getT().getPhase() == null || up.getName() == null || up.getT().getTextbook() == null
						|| up.getT().getTextbookCategory() == null) {
					((VMTeaSession) session).setNeedPerfectData(true);
				}
				if (up.getT().getPhase() != null && up.getT().getTextbookCategory() != null) {
					int phaseCode = up.getT().getPhase().getCode();
					int categoryCode = up.getT().getTextbookCategory().getCode();
					int subjectCode = phaseCode == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
							: SubjectService.PHASE_2_MATH;
					up.getT().getTextbookCategory()
							.setTextbooks(tbConvert.to(tbService.find(phaseCode, categoryCode, subjectCode)));
				}
			}
			// 清理相关cache
			findPasswordCacheService.invalidAuthCode(Security.getToken(), Security.getToken());
			findPasswordCacheService.setAccount(Security.getToken(), 0);
			// 其他终端下线
			JSONObject jo = new JSONObject();
			jo.put("userId", Security.getUserId());
			jo.put("excludeToken", Security.getToken());
			mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
					MQ.builder().data(jo).build());
			return new Value(session);
		} else {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
	}

	/**
	 * 验证密保
	 * 
	 * @since 2.0.1
	 * @param q1
	 *            第一个问题代码
	 * @param a1
	 *            第一个问题答案
	 * @param q2
	 *            第二个问题代码
	 * @param a2
	 *            第二个问题答案
	 * @param q3
	 *            第三个问题代码
	 * @param a3
	 *            第三个问题答案
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = { "checkPasswordQuestion" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value checkPasswordQuestion(int q1, String a1, int q2, String a2, int q3, String a3) {
		long accountId = findPasswordCacheService.getAccount(Security.getToken());
		if (accountId == 0) {
			return new Value(new IllegalArgException());
		}
		Account account = accountService.getAccount(accountId);
		if (account.getPqStatus() == Status.ENABLED) {// 设置了密保
			List<AccountPasswordQuestion> myList = accountPasswordQuestionService.findByAccountId(account.getId());
			boolean check1 = false;
			boolean check2 = false;
			boolean check3 = false;
			if (StringUtils.isNotBlank(a1) && StringUtils.isNotBlank(a2) && StringUtils.isNotBlank(a3) && q1 != q2
					&& q1 != q3 && q3 != q2) {
				for (AccountPasswordQuestion accountPasswordQuestion : myList) {
					int code = accountPasswordQuestion.getPasswordQuestionCode();
					String answer = accountPasswordQuestion.getAnswer();
					if (code == q1 && answer.equals(a1)) {
						check1 = true;
					}
					if (code == q2 && answer.equals(a2)) {
						check2 = true;
					}
					if (code == q3 && answer.equals(a3)) {
						check3 = true;
					}
				}
			}
			boolean check = check1 && check2 && check3;
			if (check) {
				Map<String, Object> map = new HashMap<String, Object>(1);
				map.put("username", account.getName());
				return new Value(map);
			} else {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_PASSWORD_QUESTION_ANSWER));
			}
		} else {
			return new Value(new IllegalArgException());
		}
	}

	/**
	 * 通过密保重设密码
	 * 
	 * @since 2.0.1
	 * @param q1
	 *            第一个问题代码
	 * @param a1
	 *            第一个问题答案
	 * @param q2
	 *            第二个问题代码
	 * @param a2
	 *            第二个问题答案
	 * @param q3
	 *            第三个问题代码
	 * @param a3
	 *            第三个问题答案
	 * @param password
	 *            密码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = { "resetPasswordByPQA" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value resetPasswordByPQA(int q1, String a1, int q2, String a2, int q3, String a3, String password,
			HttpServletRequest request, HttpServletResponse response) {
		long accountId = findPasswordCacheService.getAccount(Security.getToken());
		if (accountId == 0) {
			return new Value(new IllegalArgException());
		}
		boolean check = false;
		// check answers
		Account account = accountService.getAccount(accountId);
		if (account.getPqStatus() == Status.ENABLED) {// 设置了密保
			List<AccountPasswordQuestion> myList = accountPasswordQuestionService.findByAccountId(account.getId());
			boolean check1 = false;
			boolean check2 = false;
			boolean check3 = false;
			if (StringUtils.isNotBlank(a1) && StringUtils.isNotBlank(a2) && StringUtils.isNotBlank(a3) && q1 != q2
					&& q1 != q3 && q3 != q2) {
				for (AccountPasswordQuestion accountPasswordQuestion : myList) {
					int code = accountPasswordQuestion.getPasswordQuestionCode();
					String answer = accountPasswordQuestion.getAnswer();
					if (code == q1 && answer.equals(a1)) {
						check1 = true;
					}
					if (code == q2 && answer.equals(a2)) {
						check2 = true;
					}
					if (code == q3 && answer.equals(a3)) {
						check3 = true;
					}
				}
			}
			check = check1 && check2 && check3;
		}
		if (check) {
			try {
				ValidateUtils.validatePassword(password);
			} catch (AccountException e) {
				return new Value(e);
			}
			accountService.updatePassword(accountId, password, 2);
			User user = accountService.getUserByAccountId(accountId);
			user.setLoginSource(Product.YOOMATH);
			if (user.getUserType() != UserType.STUDENT) {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_NOT_SUPPORT_USERTYPE));
			}
			if (user.getStatus() == Status.DISABLED) {
				Parameter parameter = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_FORBIDDEN),
						parameter == null ? "" : parameter.getValue());
			}
			if (user.getStatus() == Status.DELETED) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
			}
			accountService.handleLogin(user, request, response);

			accountCacheService.invalidLoginWrongTime(Security.getToken());
			WebUtils.addCookie(request, response, Cookies.LOGIN_WRONG_TIME, "0");

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
				session = new VMTeaSession(up);
				// 找回密码---判断当前用户账户信息是否完善(阶段/真实姓名/版本/教材),不完善需要完善
				if (up.getT().getPhase() == null || up.getName() == null || up.getT().getTextbook() == null
						|| up.getT().getTextbookCategory() == null) {
					((VMTeaSession) session).setNeedPerfectData(true);
				}
				if (up.getT().getPhase() != null && up.getT().getTextbookCategory() != null) {
					int phaseCode = up.getT().getPhase().getCode();
					int categoryCode = up.getT().getTextbookCategory().getCode();
					int subjectCode = phaseCode == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
							: SubjectService.PHASE_2_MATH;
					up.getT().getTextbookCategory()
							.setTextbooks(tbConvert.to(tbService.find(phaseCode, categoryCode, subjectCode)));
				}
			}
			// 清理相关cache
			findPasswordCacheService.invalidAuthCode(Security.getToken(), Security.getToken());
			findPasswordCacheService.setAccount(Security.getToken(), 0);
			// 其他终端下线
			JSONObject jo = new JSONObject();
			jo.put("userId", Security.getUserId());
			jo.put("excludeToken", Security.getToken());
			mqSender.send(MqSessionRegistryConstants.EX_SESSION, MqSessionRegistryConstants.RK_SESSION_OFFLINE,
					MQ.builder().data(jo).build());
			return new Value(session);
		} else {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		}
	}

}
