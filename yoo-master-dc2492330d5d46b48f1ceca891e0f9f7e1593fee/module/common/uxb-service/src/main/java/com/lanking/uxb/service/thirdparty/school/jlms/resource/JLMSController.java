package com.lanking.uxb.service.thirdparty.school.jlms.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.thirdparty.school.jlms.client.JLMSClient;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;

/**
 * 南京九龙中学
 * 
 * @author wlche
 * @since 2017-10-16
 */
@RestController
@RequestMapping("jlms")
@RolesAllowed(anyone = true)
public class JLMSController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CredentialService credentialService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private AccountService accountService;

	@Autowired
	private JLMSClient client;

	/**
	 * 悠数学登录.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "login")
	private ModelAndView login(HttpServletRequest request, HttpServletResponse response) {

		// 验证签名
		if (!client.signCheck(request)) {
			return null;
		}

		String userRole = request.getParameter("user_role"); // 1 老师，0 学生
		String userid = request.getParameter("userid");

		// 查找本地绑定关系
		Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH, CredentialType.JLMS, userid);
		if (credential == null) {
			// 查询南京电教馆获得用户信息
			UserType userType = UserType.NULL;
			if (StringUtils.isNotBlank(userRole) && "1".equals(userRole)) {
				userType = UserType.TEACHER;
			} else if (StringUtils.isNotBlank(userRole) && "0".equals(userRole)) {
				userType = UserType.STUDENT;
			}
			JLMSUser user = client.getUserInfo(userid, userType);
			if (user == null) {
				user = new JLMSUser();
				user.setUserid(userid);
			}

			// 缓存数据
			SessionPacket packet = new SessionPacket();
			packet.getAttrSession().setAttr("jlmsUser", user);
			packet.getAttrSession().setAttr("credentialType", CredentialType.JLMS.getValue());
			sessionService.refreshCurrentSession(packet, false);
			return new ModelAndView("redirect:/third-login.html");
		} else {
			// 缓存数据
			User u = accountService.getUserByAccountId(credential.getAccountId());
			u.setLoginSource(Product.YOOMATH);
			accountService.handleLogin(u, request, response);

			JLMSUser user = new JLMSUser();
			user.setUserid(credential.getUid());
			user.setRealName(u.getName());
			user.setUserType(u.getUserType());

			// 缓存数据
			SessionPacket packet = new SessionPacket();
			packet.getAttrSession().setAttr("jlmsUser", user);
			packet.getAttrSession().setAttr("credentialType", CredentialType.JLMS.getValue());
			sessionService.refreshCurrentSession(packet, false);

			WebUtils.addCookie(request, response, Cookies.SECURITY_LOGIN_STATUS, "1");
			return new ModelAndView("redirect:/index.html");
		}
	}
}
