package com.lanking.uxb.service.thirdparty.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lanking.uxb.service.session.api.SessionService;
import com.lanking.uxb.service.thirdparty.weixin.WXMethodHandlerInterceptor;
import com.lanking.uxb.service.thirdparty.weixin.client.WXClient;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;

@Configuration
public class ThirdpartyConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private WXClient wxclient;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private SessionService sessionService;

	@Bean("WXMethodHandlerInterceptor")
	@Qualifier(value = "WXMethodHandlerInterceptor")
	WXMethodHandlerInterceptor handlerInterceptorAdapter() {
		WXMethodHandlerInterceptor handlerInterceptorAdapter = new WXMethodHandlerInterceptor();
		handlerInterceptorAdapter.setAccountService(accountService);
		handlerInterceptorAdapter.setCredentialService(credentialService);
		handlerInterceptorAdapter.setSessionService(sessionService);
		handlerInterceptorAdapter.setWxclient(wxclient);
		return handlerInterceptorAdapter;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(handlerInterceptorAdapter());
		super.addInterceptors(registry);
	}
}
