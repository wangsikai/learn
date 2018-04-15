package com.lanking.uxb.service.thirdparty.weixin;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.uxb.core.annotation.WXBindCheck;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.weixin.client.WXClient;
import com.lanking.uxb.service.thirdparty.weixin.response.UserAccessToken;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;

/**
 * 微信绑定拦截器.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年12月29日
 */
public class WXMethodHandlerInterceptor extends HandlerInterceptorAdapter {

	private WXClient wxclient;
	private CredentialService credentialService;
	private AccountService accountService;
	private SessionService sessionService;

	public void setWxclient(WXClient wxclient) {
		this.wxclient = wxclient;
	}

	public void setCredentialService(CredentialService credentialService) {
		this.credentialService = credentialService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		WXBindCheck wxBindCheck = ((HandlerMethod) handler).getMethodAnnotation(WXBindCheck.class);
		if (null == wxBindCheck) {
			wxBindCheck = AnnotationUtils.getAnnotation(((HandlerMethod) handler).getMethod().getDeclaringClass(),
					WXBindCheck.class);
		}

		if (null != wxBindCheck) {
			String requestURI = request.getRequestURI().intern();
			Product product = Product.valueOf(wxBindCheck.product());
			UserType bindType = UserType.valueOf(wxBindCheck.userType());
			String code = request.getParameter("code");

			if (StringUtils.isBlank(code)) {
				if (Security.isLogin()) {
					return true;
				} else {
					Cookie cookie = WebUtils.getCookie(request, "WX_CODE");
					if (cookie == null) {
						return true;
					}
					code = cookie.getValue();
				}
			} else {
				WebUtils.addCookie(request, response, "WX_CODE", code, 24 * 3600);
			}

			// 获取用户OPENID
			UserAccessToken accessToken = wxclient.getUserAccessToken(code, product);
			String openId = accessToken.getOpenid();

			// 校验当前用户的微信绑定
			Credential credential = credentialService.getCredentialByPersonId(product, CredentialType.WEIXIN_MP,
					openId);

			if (null == credential) {
				// 无凭证，需要绑定
				this.storeData(accessToken);
				WebUtils.addCookie(request, response, "BIND_TYPE", bindType.name());
				WebUtils.removeCookie(request, response, "USER_TYPE");
				response.sendRedirect("/login.html?path=" + requestURI);
				return false;
			} else {
				User user = accountService.getUserByAccountId(credential.getAccountId());
				if (user.getUserType() != bindType) {
					// 角色不同，需要重新绑定
					this.storeData(accessToken);
					WebUtils.addCookie(request, response, "BIND_TYPE", bindType.name());
					WebUtils.addCookie(request, response, "USER_TYPE", user.getUserType().name());
					response.sendRedirect("/login.html?path=" + requestURI);
					return false;
				} else if (!Security.isLogin()) {
					// 登录
					accountService.handleLogin(user, request, response);
				}
			}
		}

		return true;
	}

	// 缓存数据
	private void storeData(UserAccessToken accessToken) {
		SessionPacket packet = new SessionPacket();
		packet.getAttrSession().setAttr("wxuser", wxclient.getUser(accessToken));
		packet.getAttrSession().setAttr("credentialType", CredentialType.WEIXIN_MP.getValue());
		sessionService.refreshCurrentSession(packet, false);
	}
}
