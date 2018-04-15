package com.lanking.uxb.security.api.impl;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.uxb.security.api.Authentication;
import com.lanking.uxb.security.api.AuthenticationProvider;
import com.lanking.uxb.service.session.api.impl.Security;

@Component
public class DefaultAuthenticationProviderImpl implements AuthenticationProvider {

	@Override
	public Authentication getCurrentAuthentication() {
		SimpleAuthentication authentication = new SimpleAuthentication();
		authentication.setAuthorities(Lists.newArrayList(Security.getUserType().toString().toLowerCase()));
		return authentication;
	}
}
