package com.lanking.uxb.security.api.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.lanking.uxb.security.api.Authentication;

public class SimpleAuthentication implements Authentication {
	private static final long serialVersionUID = -8245777493578668464L;

	private Collection<String> authorities;
	private Collection<String> relativeURIs;

	@Override
	public Collection<String> getAuthorities() {
		return authorities == null ? new ArrayList<String>(0) : authorities;
	}

	@Override
	public Collection<String> getRelativeURIs() {
		return relativeURIs == null ? new ArrayList<String>(0) : relativeURIs;
	}

	@Override
	public Object getPrincipal() {
		return "";
	}

	@Override
	public Object getCredentials() {
		return "";
	}

	public void setAuthorities(Collection<String> authorities) {
		this.authorities = authorities;
	}

	public void setRelativeURIs(Collection<String> relativeURIs) {
		this.relativeURIs = relativeURIs;
	}
}
