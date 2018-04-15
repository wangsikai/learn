package com.lanking.uxb.service.user.resource;

import java.util.ArrayList;
import java.util.Date;
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
import com.lanking.cloud.domain.yoomath.clazz.ClazzFrom;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
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
import com.lanking.uxb.service.thirdparty.scedu.resource.SCEduClass;
import com.lanking.uxb.service.thirdparty.scedu.resource.SCEduStudent;
import com.lanking.uxb.service.thirdparty.scedu.resource.SCEduTeacher;
import com.lanking.uxb.service.thirdparty.scedu.resource.SCEduUser;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 四川教育平台登录的相关接口
 * 
 * @author wlche
 * @version 2016年2月29日
 */
@RestController
@RequestMapping("account/2/scedu")
public class Register2SCEduController {
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
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkStudentClazzService homeworkStudentClazzService;

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
					messageSender.send(new EmailPacket(target, 11000006, ValueMap.value("authCode", verifyCode).put(
							"userType", userType.getCnName())));
				} else {
					messageSender.send(new EmailPacket(target, 11000005, ValueMap.value("authCode", verifyCode).put(
							"userType", userType.getCnName())));
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

		// 找到该账户已绑定的凭证
		Integer credentialValue = Security.getSession().getAttrSession().getIntAttr("credentialType");
		if (account != null && credentialValue != null) {
			CredentialType credentialType = CredentialType.findByValue(credentialValue);
			Credential credential = credentialService.getCredentialByAccountId(source, credentialType, account.getId());
			if (credentialType == CredentialType.SCEDU && null != credential) {
				SCEduUser scUser = Security.getSession().getAttrSession().getObject("scuser", SCEduUser.class);
				if (!scUser.getPersonid().equals(credential.getUid())) {
					// 不允许多个四川教育平台用户，绑定同一个本地账户
					map.put("flag", 2);
				}
			} else if (credentialType == CredentialType.SCEDU && null == credential) {
				// 四川教育平台教师用户不允许绑定到学生账户上
				User user = accountService.getUserByAccountId(account.getId());
				if (userType == UserType.TEACHER && user.getUserType() == UserType.STUDENT) {
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
	 * @since 2.0.1 教师不再使用电话邮箱绑定，改为用户名直接注册 2016-10-10
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
				// if (StringUtils.isBlank(form.getMobile()) &&
				// StringUtils.isBlank(form.getEmail())) {
				// return new Value(new
				// AccountException(AccountException.ILLEGAL_ARG));
				// }

				if (form.getAccountId() == null
						&& (form.getSubjectCode() == null || form.getPhaseCode() == null || StringUtils.isBlank(form
								.getRealName()))) {
					return new Value(new IllegalArgException());
				}
			}

			String target = null;
			// if (StringUtils.isNotBlank(form.getMobile())) {
			// target = form.getMobile();
			// } else if (StringUtils.isNotBlank(form.getEmail())) {
			// target = form.getEmail();
			// }

			Account account = null;
			if (form.getAccountId() != null) {
				regflag = 2;
				account = accountService.getAccount(form.getAccountId());
			}
			if (null == account) {
				if (StringUtils.isBlank(form.getPassword())) {
					account = accountService.createAccount2(form, false);
				} else {
					account = accountService.createAccount2(form, true);
				}
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
			if (form.getEndTime() != null) {
				credential.setEndAt(new Date(form.getEndTime()));
			}
			credential.setToken(form.getToken());
			credential.setUpdateAt(date);
			credential.setProduct(form.getSource());
			credential.setUserId(user.getId());
			credentialService.save(credential);

			user.setLoginSource(form.getSource());
			accountService.handleLogin(user, request, response);

			// 处理班级信息
			this.processClazz(user);

			return new Value(regflag);
		} catch (AccountException e) {
			return new Value(e);
		}
	}

	// 处理四川用户班级信息
	private void processClazz(User user) {
		SCEduUser scUser = Security.getSession().getAttrSession().getObject("scuser", SCEduUser.class);
		if (scUser == null) {
			return;
		}

		List<SCEduClass> scclazzes = null;
		List<String> codes = new ArrayList<String>();
		if (scUser.getType() == 26) {
			SCEduStudent student = scUser.getStudent();
			scclazzes = student.getClazzes();
		} else {
			SCEduTeacher teacher = scUser.getTeacher();
			scclazzes = teacher.getClazzes();
		}

		if (scclazzes.size() == 0) {
			return;
		}

		for (SCEduClass clazz : scclazzes) {
			codes.add(clazz.getClassId());
		}

		// 带第三方及教师组合key的班级列表
		Map<String, List<HomeworkClazz>> homeworkClazzMap = homeworkClassService.findTeaUsedByFromCode(ClazzFrom.SCEDU,
				codes);

		if (scclazzes.get(0) == null) {
			return;
		}
		if (scUser.getType() == 26) {
			// 学生
			for (SCEduClass clazz : scclazzes) {
				List<HomeworkClazz> homeworkClazzs = homeworkClazzMap.get(clazz.getClassId()); // 相同第三方班级码的班级集合
				if (null != homeworkClazzs) {
					for (HomeworkClazz hc : homeworkClazzs) {
						homeworkStudentClazzService.join(hc.getId(), user.getId()); // 加入班级
					}
				} else {
					// 没有该第三方编码的班级，创建无教师的新班级
					HomeworkClazz homeworkClazz = homeworkClassService.createByThird(clazz.getClassname(), null,
							ClazzFrom.SCEDU, clazz.getClassId());
					homeworkStudentClazzService.join(homeworkClazz.getId(), user.getId()); // 加入班级
				}
			}
		} else {
			Integer section = scclazzes.get(0).getSection();
			if (null == section) {
				return;
			}

			// 老师
			for (SCEduClass clazz : scclazzes) {
				if (clazz.getSection().intValue() != section) {
					continue;
				}
				List<HomeworkClazz> homeworkClazzs = homeworkClazzMap.get(clazz.getClassId()); // 相同第三方班级码的班级集合
				HomeworkClazz homeworkClazz = null;
				if (null != homeworkClazzs) {
					for (HomeworkClazz hc : homeworkClazzs) {
						if (hc.getTeacherId() != null && hc.getTeacherId() == user.getId().longValue()) {
							homeworkClazz = hc;
						} else if (hc.getTeacherId() == null) {
							// 有无教师的班级，更新教师
							homeworkClassService.updateTeacherByFromCode(ClazzFrom.SCEDU, clazz.getClassId(),
									user.getId());
							homeworkClazz = hc;
						}
					}
				}

				if (homeworkClazz == null) {
					// 没有该老师的班级，创建班级
					homeworkClazz = homeworkClassService.createByThird(clazz.getClassname(), user.getId(),
							ClazzFrom.SCEDU, clazz.getClassId());
					if (null != homeworkClazzs && homeworkClazzs.size() > 0) {
						// 将其他不是该老师班级下的学生，加入到新班级中
						List<Long> studentIds = homeworkStudentClazzService.listClassStudents(homeworkClazzs.get(0)
								.getId());
						for (Long studentId : studentIds) {
							homeworkStudentClazzService.join(homeworkClazz.getId(), studentId);
						}
					}
				}
			}
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

		// 找到该账户已绑定的凭证
		Integer credentialValue = Security.getSession().getAttrSession().getIntAttr("credentialType");
		if (account != null && credentialValue != null) {
			CredentialType credentialType = CredentialType.findByValue(credentialValue);
			Credential credential = credentialService.getCredentialByAccountId(source, credentialType, account.getId());
			if (credentialType == CredentialType.SCEDU && null != credential) {
				SCEduUser scUser = Security.getSession().getAttrSession().getObject("scuser", SCEduUser.class);
				if (!scUser.getPersonid().equals(credential.getUid())) {
					// 不允许多个四川教育平台用户，绑定同一个本地账户
					map.put("flag", 2);
				}
			} else if (credentialType == CredentialType.SCEDU && null == credential) {
				// 四川教育平台教师用户不允许绑定到学生账户上
				User user = accountService.getUserByAccountId(account.getId());
				if (userType == UserType.TEACHER && user.getUserType() == UserType.STUDENT) {
					map.put("flag", 2);
				}
			}
		}

		return new Value(map);
	}
}
