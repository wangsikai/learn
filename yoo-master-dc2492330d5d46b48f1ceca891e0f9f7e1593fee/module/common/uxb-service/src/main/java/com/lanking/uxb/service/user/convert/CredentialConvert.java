package com.lanking.uxb.service.user.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.user.value.VCredential;

@Component
public class CredentialConvert extends Converter<VCredential, Credential, Long> {

	@Override
	protected Long getId(Credential s) {
		return s.getId();
	}

	@Override
	protected VCredential convert(Credential s) {
		VCredential v = new VCredential();
		v.setType(s.getType());
		v.setName(s.getName());
		return v;
	}

}
