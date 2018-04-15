package com.lanking.uxb.service.thirdparty.jsedu.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.thirdparty.jsedu.client.JSEduClient;
import com.lanking.uxb.service.thirdparty.jsedu.response.JSEduUser;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.form.RegisterForm;
import com.lanking.uxb.service.user.resource.Register2JSEduController;

/**
 * 电信教育云.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月26日
 */
@RestController
@RequestMapping("jsedu")
@RolesAllowed(anyone = true)
public class JSEduController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JSEduClient jsEduClient;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private Register2JSEduController register2JSEduController;

	/**
	 * 悠数学回调.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "yoomathCallback")
	private ModelAndView yoomathCallback(HttpServletRequest request, HttpServletResponse response) {
		String uid = request.getParameter("uid");
		String timestamp = request.getParameter("timestamp");
		String hashcode = request.getParameter("hashcode");
		Product product = Product.YOOMATH;

		if (StringUtils.isBlank(uid)) {
			return null; // 参数不正确
		}

		if (StringUtils.isNotBlank(hashcode) && StringUtils.isNotBlank(timestamp)) {
			// 验证合法性
			if (!jsEduClient.check(timestamp, hashcode)) {
				return null; // 参数不合法
			}
		}

		// 获取本地用户
		Credential credential = credentialService.getCredentialByPersonId(product, CredentialType.JSEDU, uid);
		if (credential == null) {
			// 本地没有账户，跳转获取TOKEN并取得用户信息
			jsEduClient.jumpToGetToken(request, response);
		} else {
			// 自动登录
			User u = accountService.getUserByAccountId(credential.getAccountId());
			u.setLoginSource(product);
			accountService.handleLogin(u, request, response);

			// 缓存数据
			SessionPacket packet = new SessionPacket();
			sessionService.refreshCurrentSession(packet, false);

			WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
			return new ModelAndView("redirect:/index.html");
		}

		return null;
	}

	/**
	 * 悠数学电信教育云用户回调.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "yoomathUserCallback")
	private ModelAndView yoomathUserCallback(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = request.getParameter("access_token");
		if (StringUtils.isNotBlank(accessToken)) {
			try {
				JSEduUser jsEduUser = jsEduClient.getUser(accessToken);
				if (jsEduUser == null) {
					logger.error("[JSEDU] jsEduUser is null !");
				} else {
					// 缓存数据
					SessionPacket packet = new SessionPacket();
					packet.getAttrSession().setAttr("jsuser", jsEduUser);
					packet.getAttrSession().setAttr("credentialType", CredentialType.JSEDU.getValue());
					sessionService.refreshCurrentSession(packet, false);
					if ("5".equals(jsEduUser.getIdentityType())) {
						// 学生、家长
						RegisterForm form = new RegisterForm();
						form.setCredentialType(CredentialType.JSEDU);
						form.setType(UserType.STUDENT);
						form.setName(jsEduUser.getUserName());
						form.setThirdName(jsEduUser.getNickName());
						form.setRealName(jsEduUser.getNickName());
						form.setToken(accessToken);
						form.setUid(jsEduUser.getIdentityId());
						if (jsEduUser.getJSEduUserAttrMap() != null) {
							form.setMobile(jsEduUser.getJSEduUserAttrMap().getMobile());
						}

						form.setSource(Product.YOOMATH);
						register2JSEduController.register(form, request, response); // 自动注册

						WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
						return new ModelAndView("redirect:/index.html");
					} else {
						RegisterForm form = new RegisterForm();
						form.setCredentialType(CredentialType.JSEDU);
						form.setType(UserType.TEACHER);

						// 检测教师班级信息
						return new ModelAndView("redirect:/third-improve.html");
						// form.setPhaseCode(section == 3 ? 2 : 3);
						// form.setSubjectCode(section == 3 ? 202 : 302);
						// form.setName(user.getAccount());
						// form.setThirdName(user.getOaccount());
						// form.setRealName(user.getOaccount());
						// form.setToken(user.getTokenInfo().getToken());
						// form.setUid(user.getPersonid());
						// form.setSource(Product.YOOMATH);
						// Value regValue =
						// register2SCEduController.register(form, request,
						// response); // 自动注册

						// 处理教材选择，初中，选择北师大版22，高中，人教A版13，13330201
						// int textbookCode = form.getPhaseCode() == 2 ?
						// 22220201 : 13330201;
						// Textbook tb = textbookService.get(textbookCode);
						// teacherService.updateCategory(Security.getUserId(),
						// tb.getCategoryCode(), textbookCode);
						//
						// WebUtils.addCookie(request, response,
						// Cookies.SECURITY_LOGIN_STATUS, "1");
						// return new ModelAndView("redirect:/index.html");
					}
				}
			} catch (Exception e) {
				logger.error("[JSEDU] token callback error", e);
			}
		}

		return null; // 参数不正确
	}
}
