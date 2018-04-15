package com.lanking.uxb.service.user.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.user.value.VAccount;

@Component
public class AccountConvert extends Converter<VAccount, Account, Long> {

	@Override
	protected Long getId(Account s) {
		return s.getId();
	}

	@Override
	protected VAccount convert(Account s) {
		VAccount v = new VAccount();
		v.setId(s.getId());
		v.setName(s.getName());
		v.setMobile(validBlank(s.getMobile()));
		v.setEmail(validBlank(s.getEmail()));
		v.setEmailStatus(s.getEmailStatus());
		v.setMobileStatus(s.getMobileStatus());
		v.setPasswordStatus(s.getPasswordStatus());
		v.setStrength(s.getStrength());
		v.setPqStatus(s.getPqStatus());
		v.setHasPassword(s.getPasswordStatus() != PasswordStatus.DISABLED);
		v.setNameUpdateStatus(s.getNameUpdateStatus() == null ? Status.DISABLED : s.getNameUpdateStatus());
		return v;
	}
}
