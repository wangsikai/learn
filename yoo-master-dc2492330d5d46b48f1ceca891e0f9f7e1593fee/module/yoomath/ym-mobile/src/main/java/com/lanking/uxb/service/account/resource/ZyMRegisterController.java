package com.lanking.uxb.service.account.resource;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.account.value.VMSession;
import com.lanking.uxb.service.account.value.VMStuSession;
import com.lanking.uxb.service.account.value.VMTeaSession;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.user.resource.Register2Controller;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 悠数学移动端(注册相关接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@RestController
@RequestMapping("zy/m/account")
public class ZyMRegisterController {

	@Autowired
	private Register2Controller register2Controller;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private ParameterService parameterService;

	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = { "create/check" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value check(String name, String mobile, @RequestHeader(value = "APP", required = false) YooApp app) {
		// check格式
		try {
			ValidateUtils.validateName(name);
			ValidateUtils.validateMobile(mobile);
		} catch (AccountException e) {
			return new Value(e);
		}
		// check是否存在
		Account account = accountService.getAccount(GetType.NAME, name);
		if (account == null) {
			account = accountService.getAccount(GetType.MOBILE, mobile);
		}
		if (account == null) {
			// 发送验证码
			register2Controller.sendVerifyCode(mobile, GetType.MOBILE, UserType.TEACHER, Product.YOOMATH, null, app);
			return new Value();
		} else {
			if (name.equalsIgnoreCase(account.getName())) {// 用户名存在
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_NAME_EXIST));
			} else if (mobile.equals(account.getMobile())) {// 手机号码 存在
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_MOBILE_EXIST));
			}
			return new Value(new ServerException());
		}
	}

	/**
	 * 检查手机号是否可用
	 *
	 * @param mobile
	 *            手机号码
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = { "create/checkMobile" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value checkMobile(String mobile) {
		try {
			ValidateUtils.validateMobile(mobile);
		} catch (AbstractException e) {
			return new Value(e);
		}

		Account account = accountService.getAccount(GetType.MOBILE, mobile);
		if (account == null) {
			Map<String, Object> retMap = new HashMap<String, Object>(1);
			Parameter parameter = parameterService.get(Product.YOOMATH, "account.register.pictureVerification.enable");
			boolean code = parameter == null ? false : Boolean.valueOf(parameter.getValue());
			retMap.put("needVerifyCode", code);
			return new Value(retMap);
		}
		return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_MOBILE_EXIST));
	}

	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = { "create/sendAuthCode" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value sendAuthCode(String mobile, @RequestParam(value = "location", required = false) String location,
			@RequestHeader(value = "APP", required = false) YooApp app) {
		try {
			ValidateUtils.validateMobile(mobile);
		} catch (AccountException e) {
			return new Value(e);
		}
		// 发送验证码
		Value value = register2Controller.sendVerifyCode(mobile, GetType.MOBILE, UserType.TEACHER, Product.YOOMATH,
				location, app);
		if (value.getRet_code() == AccountException.ACCOUNT_MOBILE_EXIST) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_MOBILE_EXIST));
		} else if (value.getRet_code() == AccountException.ACCOUNT_VERIFYCODE_INVALID) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_VERIFYCODE_INVALID));
		}
		return new Value();
	}

	/**
	 * 注册接口,学生注册
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param form
	 *            注册的相关参数
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletRequest
	 * @return {@link Value}
	 */
	@RestController(token = true)
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = { "create" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value register(RegisterForm form, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(value = "APP", required = false) YooApp app) {
		if ((app == null || app == YooApp.MATH_STUDENT) && form.getType() != UserType.STUDENT) {
			return new Value(new ServerException());
		} else if (app == YooApp.MATH_TEACHER && form.getType() != UserType.TEACHER) {
			return new Value(new ServerException());
		}
		form.setSource(Product.YOOMATH);
		form.setPwd(form.getPassword());
		if (form.getType() == UserType.TEACHER) {
			form.setRealName(form.getName());
			if (form.getPhaseCode() != null) {
				if (form.getPhaseCode().intValue() == PhaseService.PHASE_MIDDLE) {
					form.setSubjectCode(SubjectService.PHASE_2_MATH);
				} else if (form.getPhaseCode().intValue() == PhaseService.PHASE_HIGH) {
					form.setSubjectCode(SubjectService.PHASE_3_MATH);
				}
			}
			if (StringUtils.isBlank(form.getName()) && StringUtils.isNotBlank(form.getMobile())
					&& StringUtils.isNotBlank(form.getVerifyCode())) {
				form.setName(accountService.generateName(UserType.TEACHER, GetType.MOBILE));
				form.setRealName(form.getName());
			}
		} else if (form.getType() == UserType.STUDENT) {
			// 如果学生通过手机号码注册并且用户名为空的情况下，则需要生成用户名
			if (StringUtils.isBlank(form.getName()) && StringUtils.isNotBlank(form.getMobile())
					&& StringUtils.isNotBlank(form.getVerifyCode())) {
				form.setName(accountService.generateName(UserType.STUDENT, GetType.MOBILE));
				form.setRealName(form.getName());
			}
		}
		Value value = register2Controller.register(form, request, response);
		if (value.getRet_code() == 0) {
			VMSession session = null;
			VUser up = userProfileConvert.get(Security.getUserId());
			if (form.getType() == UserType.STUDENT) {
				session = new VMStuSession(up);
				((VMStuSession) session).setNeedSetTextbook(true);
			} else {
				session = new VMTeaSession(up);
				// 2017.7.19 注册流程修改，老师不需要创建班级，兼容之前版本，不删除属性
				((VMTeaSession) session).setNeedCreateClass(false);
				// 是否需要完善个人资料，注册接口是新用户，所有信息都要填
				((VMTeaSession) session).setNeedPerfectData(true);
			}

			// 登录动作
			userActionService.asyncAction(UserAction.LOGIN, Security.getUserId(), null);

			return new Value(session);
		} else if (value.getRet_code() == AccountException.ACCOUNT_VERIFYCODE_INVALID) {// 验证码错误
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_WRONG_AUTHCODE));
		} else if (value.getRet_code() == AccountException.ACCOUNT_NAME_EXIST) {// 用户名已经存在
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ACCOUNT_NAME_EXIST));
		}
		return value;
	}

}
