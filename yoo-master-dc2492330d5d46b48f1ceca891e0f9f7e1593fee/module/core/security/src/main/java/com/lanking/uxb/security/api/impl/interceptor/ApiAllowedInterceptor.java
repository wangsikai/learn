package com.lanking.uxb.security.api.impl.interceptor;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqDataRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.ex.core.APIAccessRateLimitException;
import com.lanking.cloud.ex.core.ProfileNotSupportException;
import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.security.cache.ApiAllowedCacheService;
import com.lanking.uxb.service.session.api.impl.Security;

public class ApiAllowedInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ApiAllowedCacheService apiAllowedCacheService;
	@Autowired
	private MqSender mqSender;

	private static String URI_ENCODING = Charsets.UTF8;
	private static boolean API_LOG = false;
	private static int DEF_ACCESS_RATE = 5;

	public static final String MSG_NOT_SUPPORT_PROFILE = JSONObject.toJSON(new Value(new ProfileNotSupportException()))
			.toString();
	public static final String MSG_ACCESS_RATE_LIMIT = JSONObject.toJSON(new Value(new APIAccessRateLimitException()))
			.toString();

	public ApiAllowedInterceptor() {
		super();
		URI_ENCODING = Env.getString("server.tomcat.uri-encoding").intern();
		API_LOG = Env.getBoolean("api.log.enable");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ApiAllowed apiAllowed = ((HandlerMethod) handler).getMethodAnnotation(ApiAllowed.class);
		if (null == apiAllowed) {
			apiAllowed = AnnotationUtils.getAnnotation(((HandlerMethod) handler).getMethod().getDeclaringClass(),
					ApiAllowed.class);
		}
		SecurityContext.setStart(System.currentTimeMillis());
		SecurityContext.setToken(Security.getSafeToken());
		if (apiAllowed == null) {
			return true;
		} else {
			SecurityContext.setApiAllowed(apiAllowed);
			// check accessRate
			String uri = request.getRequestURI().intern();
			if (apiAllowed.accessRate() > -1) {
				if (apiAllowedCacheService.get(uri) != null) {
					accessRateLimit(request, response);
					return false;
				}
				int accessRate = apiAllowed.accessRate() == 0 ? DEF_ACCESS_RATE : apiAllowed.accessRate();
				apiAllowedCacheService.set(uri, accessRate);
			}
		}
		return true;
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
		String uri = request.getRequestURI().intern();

		if (SecurityContext.getApiAllowed() != null && SecurityContext.getApiAllowed().accessRate() > -1) {
			apiAllowedCacheService.expire(uri, 1);
		}

		long costTime = System.currentTimeMillis() - SecurityContext.getStart();
		if (API_LOG) {
			JSONObject object = new JSONObject();
			object.put("userId", Security.getSafeUserId());
			object.put("api", uri);
			object.put("costTime", costTime);
			object.put("createAt", new Date());
			object.put("hostName", request.getServerName());
			object.put("token", SecurityContext.getToken());
			if (ex != null) {
				String exContent = ex.toString() + "\n" + ex.getMessage() + "\n";
				for (StackTraceElement e : ex.getStackTrace()) {
					exContent += e.getFileName() + "--" + e.getClassName() + "--" + e.getMethodName() + "("
							+ e.getLineNumber() + ")\n";
				}
				object.put("ex", exContent);
			}
			object.put("params", JSONObject.toJSONString(request.getParameterMap()));
			mqSender.asynSend(MqDataRegistryConstants.EX_DATA, MqDataRegistryConstants.RK_DATA_APILOG,
					MQ.builder().data(object).build());
		}
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	private void accessRateLimit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding(URI_ENCODING);
		response.setContentType("application/json");
		response.getWriter().write(MSG_ACCESS_RATE_LIMIT);
	}

}
