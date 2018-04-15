package com.lanking.uxb.service.adminSecurity.config;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleRoleService;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.api.impl.interceptor.AdminSecurityMethodHandlerInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
@Configuration
@ConditionalOnExpression("${admin.security.enable}")
public class AdminSecurityConfig extends WebMvcConfigurerAdapter {
	private List<String> urls;
	@Autowired
	private ConsoleRoleService consoleRoleService;
	@Autowired
	private ConsoleUserService consoleUserService;

	@Value("${admin.security.filter}")
	private String filterUrl;

	@PostConstruct
	public void initUrls() {
		if (StringUtils.isNotBlank(filterUrl)) {
			String urlArr[] = filterUrl.split(",");
			urls = new ArrayList<String>(urlArr.length);

			for (String pattern : urlArr) {
				urls.add(pattern);
			}
		} else {
			urls = Collections.EMPTY_LIST;
		}
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(
				new AdminSecurityMethodHandlerInterceptor(consoleRoleService, consoleUserService, urls));
		super.addInterceptors(registry);
	}

	public String getFilterUrl() {
		return filterUrl;
	}

	public void setFilterUrl(String filterUrl) {
		this.filterUrl = filterUrl;
	}
}
