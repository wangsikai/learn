package com.lanking.uxb.service.base.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.uxb.service.base.api.ThirdPartyRegisterService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.form.RegisterForm;

@Service
public class ThirdPartyRegisterServiceImpl implements ThirdPartyRegisterService {

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private CredentialService credentialService;

	@Override
	public Account register(RegisterForm form) {
		String name = accountService.generateName(form.getType(), null);
		if (name == null) {
			throw new ServerException();
		}
		form.setName(name);

		Account account = createAccount(form);
		createCredential(form, account);
		return account;
	}

	@Transactional
	void createCredential(RegisterForm form, Account account) {
		Credential credential = new Credential();
		credential.setAccountId(account.getId());
		credential.setCreateAt(new Date());
		credential.setEndAt(new Date(form.getEndTime()));
		credential.setName(form.getNickname());
		credential.setToken(form.getToken());
		credential.setType(form.getCredentialType());
		credential.setUid(form.getUid());
		credential.setUpdateAt(credential.getCreateAt());
		credential.setProduct(form.getSource());
		if (account.getUser() != null) {
			credential.setUserId(account.getUser().getId());
			// 调用新的凭证创建方法，金币成长值在方法内部处理
			credentialService.save(credential, true, account.getUser().getUserType());
		}
	}

	@Transactional
	Account createAccount(RegisterForm form) {
		return accountService.createAccount2(form, false);
	}
}
