package com.lanking.uxb.service.account.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.form.ThirdPartyForm;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.account.value.VMSession;
import com.lanking.uxb.service.account.value.VMStuSession;
import com.lanking.uxb.service.account.value.VMTeaSession;
import com.lanking.uxb.service.base.api.ThirdPartyRegisterService;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.convert.CredentialConvert;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.user.value.VUserProfile;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.http.PostParameter;
import com.qq.connect.utils.json.JSONException;

/**
 * 第三方登录
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月23日
 */
@RestController
@ApiAllowed
@RequestMapping("zy/m/account/thirdparty")
public class ZyMAccountThirdPartyController {

	private Logger logger = LoggerFactory.getLogger(ZyMAccountThirdPartyController.class);
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private CredentialConvert credentialConvert;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private ThirdPartyRegisterService thirdPartyRegisterService;
	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private ZyHomeworkClassService hkClazzService;
	@Autowired
	private HttpClient httpClient;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;

	@RestController(token = true)
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "login", method = { RequestMethod.POST, RequestMethod.GET })
	public Value login(ThirdPartyForm form, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(value = "APP", required = false, defaultValue = "MATH_STUDENT") YooApp app) {
		if (form == null || form.getType() == null || form.getType() == CredentialType.DEFAULT) {
			return new Value(new IllegalArgException());
		}
		if (form.getType() == CredentialType.QQ) {
			return qqLogin(form, request, response, app);
		} else if (form.getType() == CredentialType.WEIXIN) {
			return weixinLogin(form, request, response, app);
		}

		// 登录动作
		userActionService.action(UserAction.LOGIN, Security.getUserId(), null);
		return new Value();
	}

	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "bindCredential", method = { RequestMethod.POST, RequestMethod.GET })
	public Value bindCredential(ThirdPartyForm form, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(value = "APP", required = false, defaultValue = "MATH_STUDENT") YooApp app) {
		if (form == null || form.getType() == null || form.getType() == CredentialType.DEFAULT) {
			return new Value(new IllegalArgException());
		}
		if (form.getType() == CredentialType.QQ) {
			return qqBind(form, request, response, app);
		} else if (form.getType() == CredentialType.WEIXIN) {
			return weixinBind(form, request, response, app);
		}
		return new Value();
	}

	@RestController(token = true)
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "bindAccount", method = { RequestMethod.POST, RequestMethod.GET })
	public Value bindAccount(ThirdPartyForm form, String username, String password, HttpServletRequest request,
			HttpServletResponse response,
			@RequestHeader(value = "APP", required = false, defaultValue = "MATH_STUDENT") YooApp app) {
		if (form == null || form.getType() == null || form.getType() == CredentialType.DEFAULT) {
			return new Value(new IllegalArgException());
		}
		if (form.getType() == CredentialType.QQ) {
			try {
				return qqBindAccount(form, username, password, request, response, app);
			} catch (YoomathMobileException e) {
				return new Value(e);
			}
		} else if (form.getType() == CredentialType.WEIXIN) {
			try {
				return weixinBindAccount(form, username, password, request, response, app);
			} catch (YoomathMobileException e) {
				return new Value(e);
			}
		}
		return new Value();
	}

	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "unbindCredential", method = { RequestMethod.POST, RequestMethod.GET })
	public Value unbindCredential(ThirdPartyForm form) {
		if (form == null || form.getType() == null || form.getType() == CredentialType.DEFAULT) {
			return new Value(new IllegalArgException());
		}
		Account account = accountService.getAccount(Security.getAccountId());
		if (account.getPasswordStatus() == PasswordStatus.DISABLED) {
			return new Value(new YoomathMobileException(
					YoomathMobileException.YOOMATH_MOBILE_NOPWD_CANNOT_DELETECREDENTIAL));
		}
		credentialService.deleteCredential(Product.YOOMATH, form.getType(), account.getId());
		return new Value();
	}

	@RestController(token = true)
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "checkBind", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkBind(ThirdPartyForm form, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(value = "APP", required = false, defaultValue = "MATH_STUDENT") YooApp app) {
		if (form == null || form.getType() == null || form.getType() == CredentialType.DEFAULT) {
			return new Value(new IllegalArgException());
		}
		Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, form.getType(),
				form.getOpenId());
		if (credential == null) {
			ValueMap vm = ValueMap.value("bind", false);
			if (form.getType() == CredentialType.QQ) {
				try {
					OpenID openIDObj = new OpenID(form.getAccessToken());
					String openId = openIDObj.getUserOpenID();
					if (!form.getOpenId().equals(openId)) {
						return new Value(new IllegalArgException());
					}
					QQUserInfo qqUserInfo = new QQUserInfo(Env.getString("zyapp." + app.name().toLowerCase()
							+ ".qq.appid"), form.getAccessToken(), form.getOpenId());
					vm.put("nickname", qqUserInfo.getUserInfo().getNickname());
					vm.put("avatar", qqUserInfo.getQqURL100());
				} catch (QQConnectException e) {
					logger.error("qq connect error:", e);
				}
			} else if (form.getType() == CredentialType.WEIXIN) {
				WeixinUserInfo info = getWeixinUserInfo(form.getAccessToken(), form.getOpenId());
				vm.put("nickname", info.getNickname());
				vm.put("avatar", info.getHeadimgurl());
			} else {
				return new Value(new IllegalArgException());
			}
			return new Value(vm);
		} else {
			try {
				Account account = accountService.getAccount(credential.getAccountId());
				if (account == null || account.getStatus() != Status.ENABLED) {
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
				}
				User user = accountService.getUserByAccountId(credential.getAccountId());
				user.setLoginSource(Product.YOOMATH);
				if (app == null || app == YooApp.MATH_STUDENT) {
					if (user.getUserType() != UserType.STUDENT) {
						if (user.getUserType() == UserType.TEACHER) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"教师", "教师");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
						}
					}
				} else {
					if (user.getUserType() != UserType.TEACHER) {
						if (user.getUserType() == UserType.STUDENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"学生", "学生");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
						}
					}
				}
				if (user.getStatus() == Status.DISABLED) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_FORBIDDEN);
				}
				if (user.getStatus() == Status.DELETED) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG);
				}
				accountService.handleLogin(user, request, response);
				// 登录成功后返回当前会话信息
				VMSession session = null;

				VUserProfile profile = userProfileConvert.to(user);
				// 凭证列表
				List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
				List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, profile.getAccount()
						.getId());
				for (Credential cred : credentials) {
					credentialTypes.add(cred.getType());
				}
				profile.getAccount().setCredentialTypes(credentialTypes);

				if (profile.getType() == UserType.STUDENT) {
					session = new VMStuSession(profile);
					((VMStuSession) session).setNeedSetTextbook(profile.getS().getTextbook() == null);
				} else if (profile.getType() == UserType.TEACHER) {
					session = new VMTeaSession(profile);
					((VMTeaSession) session).setNeedCreateClass(hkClassService.currentCount(Security.getUserId()) <= 0);
					// 登录判断当前用户账户信息是否完善(阶段/真实姓名/版本/教材),不完善需要完善
					if (profile.getT().getPhase() == null || profile.getName() == null
							|| profile.getT().getTextbook() == null || profile.getT().getTextbookCategory() == null) {
						((VMTeaSession) session).setNeedPerfectData(true);
					}
					dealTextBooks(profile);
				}
				return new Value(ValueMap.value("bind", true).put("session", session));
			} catch (YoomathMobileException e) {
				return new Value(e);
			}
		}
	}

	private Value qqLogin(ThirdPartyForm form, HttpServletRequest request, HttpServletResponse response, YooApp app) {
		if (StringUtils.isBlank(form.getOpenId()) || StringUtils.isBlank(form.getAccessToken())) {
			return new Value(new IllegalArgException());
		}
		try {
			OpenID openIDObj = new OpenID(form.getAccessToken());
			String openId = openIDObj.getUserOpenID();
			if (!form.getOpenId().equals(openId)) {
				return new Value(new IllegalArgException());
			}
			// 找到绑定的凭证
			Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, form.getType(), openId);
			if (credential == null) {
				QQUserInfo qqUserInfo = new QQUserInfo(
						Env.getString("zyapp." + app.name().toLowerCase() + ".qq.appid"), form.getAccessToken(),
						form.getOpenId());
				RegisterForm regForm = new RegisterForm();
				regForm.setSex(Sex.findByName(qqUserInfo.getUserInfo().getGender()));
				regForm.setCredentialType(form.getType());
				regForm.setName(qqUserInfo.getUserInfo().getNickname());
				regForm.setNickname(qqUserInfo.getUserInfo().getNickname());
				regForm.setRealName(qqUserInfo.getUserInfo().getNickname());
				regForm.setQq("腾讯QQ");
				regForm.setSource(Product.YOOMATH);
				regForm.setStrength(null);
				regForm.setType(app == YooApp.MATH_TEACHER ? UserType.TEACHER : UserType.STUDENT);
				regForm.setThirdName(qqUserInfo.getUserInfo().getNickname());
				regForm.setUid(openId);
				regForm.setToken(form.getAccessToken());
				regForm.setEndTime(form.getExpireIn());
				regForm.setPassword(RandomStringUtils.random(10, true, true));
				regForm.setPwd(regForm.getPassword());
				Account account = thirdPartyRegisterService.register(regForm);
				User user = account.getUser() == null ? accountService.getUserByAccountId(account.getId()) : account
						.getUser();
				user.setLoginSource(Product.YOOMATH);
				accountService.handleLogin(user, request, response);
				// 登录成功后返回当前会话信息
				VMSession session = null;
				VUserProfile profile = userProfileConvert.to(user);
				if (StringUtils.isNotBlank(qqUserInfo.getQqURL100())) {
					profile.setAvatarUrl(qqUserInfo.getQqURL100());
				}
				// 凭证列表
				List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
				List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, profile.getAccount()
						.getId());
				for (Credential cred : credentials) {
					credentialTypes.add(cred.getType());
				}
				profile.getAccount().setCredentialTypes(credentialTypes);

				if (profile.getType() == UserType.STUDENT) {
					session = new VMStuSession(profile);
					((VMStuSession) session).setNeedSetTextbook(true);
					((VMStuSession) session).setThirdPartyRegister(true);
				} else if (profile.getType() == UserType.TEACHER) {
					session = new VMTeaSession(profile);
					((VMTeaSession) session).setThirdPartyRegister(true);
					if (profile.getT().getPhase() == null || profile.getName() == null
							|| profile.getT().getTextbook() == null || profile.getT().getTextbookCategory() == null) {
						((VMTeaSession) session).setNeedPerfectData(true);
					}
					dealTextBooks(profile);
				}

				// 登录动作
				userActionService.action(UserAction.LOGIN, Security.getUserId(), null);

				return new Value(session);
			} else {
				Account account = accountService.getAccount(credential.getAccountId());
				if (account == null) {
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
				}
				if (account.getStatus() != Status.ENABLED) {
					Parameter parameter = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_FORBIDDEN),
							parameter == null ? "" : parameter.getValue());
				}
				User user = accountService.getUserByAccountId(credential.getAccountId());
				user.setLoginSource(Product.YOOMATH);
				if (app == null || app == YooApp.MATH_STUDENT) {
					if (user.getUserType() != UserType.STUDENT) {
						if (user.getUserType() == UserType.TEACHER) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"教师", "教师");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
						}
					}
				} else {
					if (user.getUserType() != UserType.TEACHER) {
						if (user.getUserType() == UserType.STUDENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"学生", "学生");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
						}
					}
				}
				if (user.getStatus() == Status.DISABLED) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_FORBIDDEN);
				}
				if (user.getStatus() == Status.DELETED) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG);
				}
				accountService.handleLogin(user, request, response);
				// 登录成功后返回当前会话信息
				VMSession session = null;

				VUserProfile profile = userProfileConvert.to(user);
				// 凭证列表
				List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
				List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, profile.getAccount()
						.getId());
				for (Credential cred : credentials) {
					credentialTypes.add(cred.getType());
				}
				profile.getAccount().setCredentialTypes(credentialTypes);

				if (profile.getType() == UserType.STUDENT) {
					session = new VMStuSession(profile);
					((VMStuSession) session).setNeedSetTextbook(profile.getS().getTextbook() == null);
				} else if (profile.getType() == UserType.TEACHER) {
					session = new VMTeaSession(profile);
					if (profile.getT().getPhase() == null || profile.getName() == null
							|| profile.getT().getTextbook() == null || profile.getT().getTextbookCategory() == null) {
						((VMTeaSession) session).setNeedPerfectData(true);
					}
					dealTextBooks(profile);
				}

				// 登录动作
				userActionService.action(UserAction.LOGIN, Security.getUserId(), null);

				return new Value(session);
			}
		} catch (YoomathMobileException e) {
			logger.info("register fail:", e);
			return new Value(e);
		} catch (Exception e) {
			logger.info("connect qq fail:", e);
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CONNECTQQ_FAIL));
		}
	}

	private Value qqBind(ThirdPartyForm form, HttpServletRequest request, HttpServletResponse response, YooApp app) {
		if (StringUtils.isBlank(form.getOpenId()) || StringUtils.isBlank(form.getAccessToken())) {
			return new Value(new IllegalArgException());
		}
		try {
			Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, form.getType(),
					form.getOpenId());
			if (credential == null) {
				OpenID openIDObj = new OpenID(form.getAccessToken());
				String openId = openIDObj.getUserOpenID();
				if (!form.getOpenId().equals(openId)) {
					return new Value(new IllegalArgException());
				}
				Account account = accountService.getAccountByUserId(Security.getUserId());
				credential = new Credential();
				credential.setAccountId(account.getId());
				credential.setType(form.getType());
				credential.setUid(form.getOpenId());
				credential.setToken(form.getAccessToken());
				credential.setEndAt(new Date(form.getExpireIn()));
				credential.setCreateAt(new Date());
				credential.setUpdateAt(credential.getCreateAt());
				credential.setProduct(Product.YOOMATH);
				QQUserInfo qqUserInfo = new QQUserInfo(
						Env.getString("zyapp." + app.name().toLowerCase() + ".qq.appid"), form.getAccessToken(),
						form.getOpenId());
				credential.setName(qqUserInfo.getUserInfo().getNickname());
				credential.setUserId(Security.getUserId());

				// 调用新的凭证创建方法，金币成长值在方法内部处理
				credentialService.save(credential, true, Security.getUserType());

				// QQ绑定动作
				userActionService.asyncAction(UserAction.BIND_QQ, Security.getUserId(), null);
				return new Value(ValueMap.value("credential", credentialConvert.to(credential)));
			} else {
				if (Security.getAccountId() != credential.getAccountId()) {
					return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_QQ_BINDED));
				}
				return new Value();
			}
		} catch (YoomathMobileException e) {
			logger.info("qq bind fail:", e);
			return new Value(e);
		} catch (Exception e) {
			logger.info("connect qq fail:", e);
		}
		return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CONNECTQQ_FAIL));
	}

	private Value qqBindAccount(ThirdPartyForm form, String username, String password, HttpServletRequest request,
			HttpServletResponse response, YooApp app) {
		Credential cred = credentialService.getCredentialByPersonId(Product.YOOMATH, form.getType(), form.getOpenId());
		if (cred == null) {
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
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"教师", "教师");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
						}
					}
				} else {
					if (user.getUserType() != UserType.TEACHER) {
						if (user.getUserType() == UserType.STUDENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"学生", "学生");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
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

				if (!account.getPassword().equals(Codecs.md5Hex(password.getBytes()))) {
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
				}
				// 检测此账号是否已经绑定过qq账号
				List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, account.getId());
				for (Credential credential : credentials) {
					if (credential.getType() == CredentialType.QQ) {
						return new Value(new YoomathMobileException(
								YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_HAS_BIND_QQ));
					}
				}
				// 绑定qq
				OpenID openIDObj = new OpenID(form.getAccessToken());
				String openId = openIDObj.getUserOpenID();
				if (!form.getOpenId().equals(openId)) {
					return new Value(new IllegalArgException());
				}
				cred = new Credential();
				cred.setAccountId(account.getId());
				cred.setType(form.getType());
				cred.setUid(form.getOpenId());
				cred.setToken(form.getAccessToken());
				cred.setEndAt(new Date(form.getExpireIn()));
				cred.setCreateAt(new Date());
				cred.setUpdateAt(cred.getCreateAt());
				cred.setProduct(Product.YOOMATH);
				QQUserInfo qqUserInfo = new QQUserInfo(
						Env.getString("zyapp." + app.name().toLowerCase() + ".qq.appid"), form.getAccessToken(),
						form.getOpenId());
				cred.setName(qqUserInfo.getUserInfo().getNickname());
				cred.setUserId(user.getId());

				// 调用新的凭证创建方法，金币成长值在方法内部处理
				credentialService.save(cred, true, user.getUserType());

				// 登录
				accountService.handleLogin(user, request, response);
				// 登录成功后返回当前会话信息
				VMSession session = null;

				VUserProfile up = userProfileConvert.to(user);
				// 凭证列表
				List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
				for (Credential credential : credentials) {
					credentialTypes.add(credential.getType());
				}
				up.getAccount().setCredentialTypes(credentialTypes);

				if (up.getType() == UserType.STUDENT) {
					session = new VMStuSession(up);
					((VMStuSession) session).setNeedSetTextbook(up.getS().getTextbook() == null);
				} else if (up.getType() == UserType.TEACHER) {
					session = new VMTeaSession(up);
					if (up.getT().getPhase() == null || up.getName() == null || up.getT().getTextbook() == null
							|| up.getT().getTextbookCategory() == null) {
						((VMTeaSession) session).setNeedPerfectData(true);
					}
					dealTextBooks(up);
				}
				return new Value(session);
			} catch (YoomathMobileException e) {
				return new Value(e);
			} catch (AccountException e) {
				return new Value(e);
			} catch (QQConnectException e) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CONNECTQQ_FAIL));
			}

		} else {
			// 未check bind的情况下会走到此逻辑
			return new Value(new NoPermissionException());
		}
	}

	private Value weixinLogin(ThirdPartyForm form, HttpServletRequest request, HttpServletResponse response, YooApp app) {
		if (StringUtils.isBlank(form.getOpenId()) || StringUtils.isBlank(form.getAccessToken())) {
			return new Value(new IllegalArgException());
		}
		try {
			// 找到绑定的凭证
			Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, form.getType(),
					form.getOpenId());
			if (credential == null) {
				if (checkWeixinAuth(form.getAccessToken(), form.getOpenId())) {
					return new Value(new IllegalArgException());
				}
				WeixinUserInfo weixinUserInfo = getWeixinUserInfo(form.getAccessToken(), form.getOpenId());
				RegisterForm regForm = new RegisterForm();
				regForm.setSex(Sex.findByValue(weixinUserInfo.getSex()));
				regForm.setCredentialType(form.getType());
				regForm.setName(weixinUserInfo.getNickname());
				regForm.setNickname(weixinUserInfo.getNickname());
				regForm.setRealName(weixinUserInfo.getNickname());
				regForm.setSource(Product.YOOMATH);
				regForm.setStrength(null);
				regForm.setType(app == YooApp.MATH_TEACHER ? UserType.TEACHER : UserType.STUDENT);
				regForm.setThirdName(weixinUserInfo.getNickname());
				regForm.setUid(form.getOpenId());
				regForm.setToken(form.getAccessToken());
				regForm.setEndTime(form.getExpireIn());
				regForm.setPassword(RandomStringUtils.random(10, true, true));
				regForm.setPwd(regForm.getPassword());
				Account account = thirdPartyRegisterService.register(regForm);
				User user = account.getUser() == null ? accountService.getUserByAccountId(account.getId()) : account
						.getUser();
				user.setLoginSource(Product.YOOMATH);
				accountService.handleLogin(user, request, response);
				// 登录成功后返回当前会话信息
				VMSession session = null;
				VUserProfile profile = userProfileConvert.to(user);
				if (StringUtils.isNotBlank(weixinUserInfo.getHeadimgurl())) {
					profile.setAvatarUrl(weixinUserInfo.getHeadimgurl());
				}
				// 凭证列表
				List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
				List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, profile.getAccount()
						.getId());
				for (Credential cred : credentials) {
					credentialTypes.add(cred.getType());
				}
				profile.getAccount().setCredentialTypes(credentialTypes);

				if (profile.getType() == UserType.STUDENT) {
					session = new VMStuSession(profile);
					((VMStuSession) session).setNeedSetTextbook(true);
					((VMStuSession) session).setThirdPartyRegister(true);
				} else if (profile.getType() == UserType.TEACHER) {
					session = new VMTeaSession(profile);
					if (profile.getT().getPhase() == null || profile.getName() == null
							|| profile.getT().getTextbook() == null || profile.getT().getTextbookCategory() == null) {
						((VMTeaSession) session).setNeedPerfectData(true);
					}
					dealTextBooks(profile);
					((VMTeaSession) session).setThirdPartyRegister(true);
				}

				// 登录动作
				userActionService.action(UserAction.LOGIN, Security.getUserId(), null);

				return new Value(session);
			} else {
				Account account = accountService.getAccount(credential.getAccountId());
				if (account == null || account.getStatus() != Status.ENABLED) {
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
				}
				User user = accountService.getUserByAccountId(credential.getAccountId());
				user.setLoginSource(Product.YOOMATH);
				if (app == null || app == YooApp.MATH_STUDENT) {
					if (user.getUserType() != UserType.STUDENT) {
						if (user.getUserType() == UserType.TEACHER) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"教师", "教师");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
						}
					}
				} else {
					if (user.getUserType() != UserType.TEACHER) {
						if (user.getUserType() == UserType.STUDENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"学生", "学生");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
						}
					}
				}
				if (user.getStatus() == Status.DISABLED) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_FORBIDDEN);
				}
				if (user.getStatus() == Status.DELETED) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG);
				}
				accountService.handleLogin(user, request, response);
				// 登录成功后返回当前会话信息
				VMSession session = null;

				VUserProfile profile = userProfileConvert.to(user);
				// 凭证列表
				List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
				List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, profile.getAccount()
						.getId());
				for (Credential cred : credentials) {
					credentialTypes.add(cred.getType());
				}
				profile.getAccount().setCredentialTypes(credentialTypes);

				if (profile.getType() == UserType.STUDENT) {
					session = new VMStuSession(profile);
					((VMStuSession) session).setNeedSetTextbook(profile.getS().getTextbook() == null);
				} else if (profile.getType() == UserType.TEACHER) {
					session = new VMTeaSession(profile);
					if (profile.getT().getPhase() == null || profile.getName() == null
							|| profile.getT().getTextbook() == null || profile.getT().getTextbookCategory() == null) {
						((VMTeaSession) session).setNeedPerfectData(true);
					}
					dealTextBooks(profile);
				}

				// 登录动作
				userActionService.action(UserAction.LOGIN, Security.getUserId(), null);

				return new Value(session);
			}
		} catch (YoomathMobileException e) {
			logger.info("register fail:", e);
			return new Value(e);
		} catch (Exception e) {
			logger.info("connect weixin fail:", e);
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CONNECTWEIXIN_FAIL));
		}
	}

	public void dealTextBooks(VUserProfile profile) {
		if (profile.getT().getPhase() != null && profile.getT().getTextbookCategory() != null) {
			int phaseCode = profile.getT().getPhase().getCode();
			int categoryCode = profile.getT().getTextbookCategory().getCode();
			int subjectCode = phaseCode == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
					: SubjectService.PHASE_2_MATH;
			profile.getT().getTextbookCategory()
					.setTextbooks(tbConvert.to(tbService.find(phaseCode, categoryCode, subjectCode)));
		}
	}

	private Value weixinBind(ThirdPartyForm form, HttpServletRequest request, HttpServletResponse response, YooApp app) {
		if (StringUtils.isBlank(form.getOpenId()) || StringUtils.isBlank(form.getAccessToken())) {
			return new Value(new IllegalArgException());
		}
		try {
			Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, form.getType(),
					form.getOpenId());
			if (credential == null) {
				if (checkWeixinAuth(form.getAccessToken(), form.getOpenId())) {
					return new Value(new IllegalArgException());
				}
				WeixinUserInfo weixinUserInfo = getWeixinUserInfo(form.getAccessToken(), form.getOpenId());
				Account account = accountService.getAccountByUserId(Security.getUserId());
				credential = new Credential();
				credential.setAccountId(account.getId());
				credential.setType(form.getType());
				credential.setUid(form.getOpenId());
				credential.setToken(form.getAccessToken());
				credential.setEndAt(new Date(form.getExpireIn()));
				credential.setCreateAt(new Date());
				credential.setUpdateAt(credential.getCreateAt());
				credential.setProduct(Product.YOOMATH);
				credential.setName(weixinUserInfo.getNickname());
				credential.setUserId(Security.getUserId());

				// 调用新的凭证创建方法，金币成长值在方法内部处理
				credentialService.save(credential, true, Security.getUserType());

				// 微信绑定动作
				userActionService.asyncAction(UserAction.BIND_WEIXIN, Security.getUserId(), null);
				return new Value(ValueMap.value("credential", credentialConvert.to(credential)));
			} else {
				if (Security.getAccountId() != credential.getAccountId()) {
					return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_WEIXIN_BINDED));
				}
				return new Value();
			}
		} catch (YoomathMobileException e) {
			logger.info("weixin bind fail:", e);
			return new Value(e);
		} catch (Exception e) {
			logger.info("connect weixin fail:", e);
		}
		return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CONNECTWEIXIN_FAIL));
	}

	private Value weixinBindAccount(ThirdPartyForm form, String username, String password, HttpServletRequest request,
			HttpServletResponse response, YooApp app) {
		Credential cred = credentialService.getCredentialByPersonId(Product.YOOMATH, form.getType(), form.getOpenId());
		if (cred == null) {
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
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"教师", "教师");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
						}
					}
				} else {
					if (user.getUserType() != UserType.TEACHER) {
						if (user.getUserType() == UserType.STUDENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"学生", "学生");
						} else if (user.getUserType() == UserType.PARENT) {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									"家长", "家长");
						} else {
							throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_LOGIN_NOT_SUPPORT,
									user.getUserType().getCnName(), user.getUserType().getCnName());
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

				if (!account.getPassword().equals(Codecs.md5Hex(password.getBytes()))) {
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_PWD_WRONG));
				}
				// 检测此账号是否已经绑定过微信账号
				List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, account.getId());
				for (Credential credential : credentials) {
					if (credential.getType() == CredentialType.WEIXIN) {
						return new Value(new YoomathMobileException(
								YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_HAS_BIND_WEIXIN));
					}
				}
				// 绑定微信
				if (checkWeixinAuth(form.getAccessToken(), form.getOpenId())) {
					return new Value(new IllegalArgException());
				}
				WeixinUserInfo weixinUserInfo = getWeixinUserInfo(form.getAccessToken(), form.getOpenId());
				cred = new Credential();
				cred.setAccountId(account.getId());
				cred.setType(form.getType());
				cred.setUid(form.getOpenId());
				cred.setToken(form.getAccessToken());
				cred.setEndAt(new Date(form.getExpireIn()));
				cred.setCreateAt(new Date());
				cred.setUpdateAt(cred.getCreateAt());
				cred.setProduct(Product.YOOMATH);
				cred.setName(weixinUserInfo.getNickname());
				cred.setUserId(user.getId());

				// 调用新的凭证创建方法，金币成长值在方法内部处理
				credentialService.save(cred, true, user.getUserType());

				// 登录
				accountService.handleLogin(user, request, response);
				// 登录成功后返回当前会话信息
				VMSession session = null;

				VUserProfile up = userProfileConvert.to(user);
				// 凭证列表
				List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
				for (Credential credential : credentials) {
					credentialTypes.add(credential.getType());
				}
				up.getAccount().setCredentialTypes(credentialTypes);

				if (up.getType() == UserType.STUDENT) {
					session = new VMStuSession(up);
					((VMStuSession) session).setNeedSetTextbook(up.getS().getTextbook() == null);
				} else if (up.getType() == UserType.TEACHER) {
					session = new VMTeaSession(up);
					if (up.getT().getPhase() == null || up.getName() == null || up.getT().getTextbook() == null
							|| up.getT().getTextbookCategory() == null) {
						((VMTeaSession) session).setNeedPerfectData(true);
					}
					dealTextBooks(up);
				}
				return new Value(session);
			} catch (YoomathMobileException e) {
				return new Value(e);
			} catch (AccountException e) {
				return new Value(e);
			} catch (Exception e) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CONNECTWEIXIN_FAIL));
			}

		} else {
			// 未check bind的情况下会走到此逻辑
			return new Value(new NoPermissionException());
		}
	}

	class QQUserInfo extends UserInfo {

		private static final long serialVersionUID = 8644815316261347920L;

		private String appid;
		private String qqURL40;
		private String qqURL100;

		public QQUserInfo(String appid, String token, String openID) {
			super(token, openID);
			this.appid = appid;
		}

		public UserInfoBean getUserInfo() throws QQConnectException {
			com.qq.connect.utils.json.JSONObject json = this.client.get(
					QQConnectConfig.getValue("getUserInfoURL"),
					new PostParameter[] { new PostParameter("openid", this.client.getOpenID()),
							new PostParameter("oauth_consumer_key", this.appid),
							new PostParameter("access_token", this.client.getToken()),
							new PostParameter("format", "json") }).asJSONObject();
			try {
				this.qqURL40 = json.getString("figureurl_qq_1");
				this.qqURL100 = json.getString("figureurl_qq_2");
			} catch (JSONException e) {
				logger.info("get qq avatar fail:", e);
			}
			return new UserInfoBean(json);
		}

		public String getQqURL40() {
			return qqURL40;
		}

		public String getQqURL100() {
			return qqURL100;
		}
	}

	private WeixinUserInfo getWeixinUserInfo(String accessToken, String openId) {
		String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
		WeixinUserInfo info = new WeixinUserInfo();
		try {
			HttpGet httpGet = new HttpGet(userInfoUrl);
			httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			JSONObject jsonObject = JSONObject.parseObject(response);
			info.setOpenId(jsonObject.getString("openid"));
			info.setNickname(jsonObject.getString("nickname"));
			info.setSex(jsonObject.getIntValue("sex") == 2 ? 0 : 1);
			info.setHeadimgurl(jsonObject.getString("headimgurl"));
		} catch (Exception e) {
			logger.error("get weixin user info error:", e);
		}
		return info;
	}

	private boolean checkWeixinAuth(String accessToken, String openId) {
		String checkWeixinAuthUrl = "https://api.weixin.qq.com/sns/auth?access_token=" + accessToken + "&openid="
				+ openId;
		try {
			HttpGet httpGet = new HttpGet(checkWeixinAuthUrl);
			httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			JSONObject jsonObject = JSONObject.parseObject(response);
			int errcode = jsonObject.getInteger("errcode");
			return errcode != 0;
		} catch (Exception e) {
			logger.error("check weixin auth error:", e);
		}
		return false;
	}

	class WeixinUserInfo {
		private String openId;
		private String nickname;
		private int sex;
		private String headimgurl;

		public String getOpenId() {
			return openId;
		}

		public void setOpenId(String openId) {
			this.openId = openId;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public int getSex() {
			return sex;
		}

		public void setSex(int sex) {
			this.sex = sex;
		}

		public String getHeadimgurl() {
			return headimgurl;
		}

		public void setHeadimgurl(String headimgurl) {
			this.headimgurl = headimgurl;
		}

	}

}
