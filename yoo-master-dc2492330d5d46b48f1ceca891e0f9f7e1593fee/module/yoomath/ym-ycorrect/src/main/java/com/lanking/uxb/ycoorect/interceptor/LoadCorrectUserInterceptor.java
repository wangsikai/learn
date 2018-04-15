package com.lanking.uxb.ycoorect.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lanking.intercomm.yoocorrect.dto.CorrectUserResponse;
import com.lanking.intercomm.yoocorrect.service.CorrectUserDatawayService;
import com.lanking.uxb.core.annotation.LoadCorrectUser;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.session.api.SessionPacket;
import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.session.api.impl.Security;

@Component
public class LoadCorrectUserInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private CorrectUserDatawayService correctUserDatawayService;
	@Autowired
	private SessionService sessionService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		LoadCorrectUser loadCorrectUser = ((HandlerMethod) handler).getMethodAnnotation(LoadCorrectUser.class);
		if (null == loadCorrectUser) {
			loadCorrectUser = AnnotationUtils.getAnnotation(((HandlerMethod) handler).getMethod().getDeclaringClass(),
					LoadCorrectUser.class);
		}

		CorrectUserResponse correctUser = null;

		if (loadCorrectUser != null && loadCorrectUser.value()) {
			correctUser = correctUserDatawayService.getCorrectUserByUxbUserId(Security.getUserId(), true);

			// 存储当前用户对应的快批用户
			SessionPacket sessionPacket = sessionService.getSessionPacket(Security.getToken());
			sessionPacket.getAttrSession().setAttr("correctUser", correctUser);
			sessionService.refreshCurrentSession(sessionPacket, false);
		}

		SecurityContext.setCorrectUserId(correctUser == null ? null : correctUser.getId());

		return true;
	}
}
