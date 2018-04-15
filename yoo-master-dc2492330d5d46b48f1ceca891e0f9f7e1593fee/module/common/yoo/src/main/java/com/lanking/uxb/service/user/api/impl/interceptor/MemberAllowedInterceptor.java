package com.lanking.uxb.service.user.api.impl.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.ex.core.MemberPrivilegesException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserMemberService;

@Component
public class MemberAllowedInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserMemberService userMemberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		MemberAllowed MemberAllowed = ((HandlerMethod) handler).getMethodAnnotation(MemberAllowed.class);
		if (null == MemberAllowed) {
			MemberAllowed = AnnotationUtils.getAnnotation(((HandlerMethod) handler).getMethod().getDeclaringClass(),
					MemberAllowed.class);
		}
		if (MemberAllowed != null && Security.isLogin()) {
			UserMember um = userMemberService.$findByUserId(Security.getUserId());
			SecurityContext.setMemberType(um.getMemberType());
			MemberType memberType = null;
			try {
				memberType = Enum.valueOf(MemberType.class, MemberAllowed.memberType());
			} catch (IllegalArgumentException ex) {
			}
			if (memberType != null && memberType != MemberType.NONE) {// 具体会员类型限制
				if (um == null || memberType.getValue() > um.getMemberType().getValue()) {
					// TODO 会员类型不符合 ,可以做一些返回处理
					this.noPermission(request, response);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 输出无对应权限结果.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void noPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Value value = new Value(new MemberPrivilegesException());
		response.getWriter().write(JSONObject.toJSON(value).toString());
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}
}
