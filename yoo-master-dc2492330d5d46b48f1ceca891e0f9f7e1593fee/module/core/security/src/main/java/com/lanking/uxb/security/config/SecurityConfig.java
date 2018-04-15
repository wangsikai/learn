package com.lanking.uxb.security.config;

import java.util.ArrayList;
import java.util.List;

import com.lanking.uxb.security.api.impl.interceptor.YoungySecurityMethodHandlerInterceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.security.api.impl.interceptor.ApiAllowedInterceptor;
import com.lanking.uxb.security.api.impl.interceptor.SecurityMethodHandlerInterceptor;

@Configuration
@ConfigurationProperties(prefix = "safe")
@ConditionalOnExpression("${safe.enable}")
public class SecurityConfig extends WebMvcConfigurerAdapter {

	private List<String> prefixs = new ArrayList<String>();
	private List<String> ignores = new ArrayList<String>();

	@Value("page.login")
	private String loginPage;

	@Value("page.404")
	private String notFoundPage;

	@Value("page.error")
	private String errorPage;

	@Bean(name = "apiAllowedInterceptor")
	ApiAllowedInterceptor apiAllowedInterceptor() {
		if (Env.getBoolean("api.allowed.enable")) {
			return new ApiAllowedInterceptor();
		} else {
			return null;
		}
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SecurityMethodHandlerInterceptor(this));
		registry.addInterceptor(new YoungySecurityMethodHandlerInterceptor());
		if (Env.getBoolean("api.allowed.enable")) {
			registry.addInterceptor(apiAllowedInterceptor());
		}
		super.addInterceptors(registry);
	}

	public List<String> getPrefixs() {
		return prefixs;
	}

	public void setPrefixs(List<String> prefixs) {
		this.prefixs = prefixs;
	}

	public List<String> getIgnores() {
		return ignores;
	}

	public void setIgnores(List<String> ignores) {
		this.ignores = ignores;
	}

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public String getNotFoundPage() {
		return notFoundPage;
	}

	public void setNotFoundPage(String notFoundPage) {
		this.notFoundPage = notFoundPage;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

}
