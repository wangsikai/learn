package com.lanking.uxb.service.user.value;

import java.io.Serializable;

import com.lanking.cloud.domain.yoo.account.CredentialType;

public class VCredential implements Serializable {

	private static final long serialVersionUID = -49217571607409043L;

	private CredentialType type;
	private String name;

	public CredentialType getType() {
		return type;
	}

	public void setType(CredentialType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
