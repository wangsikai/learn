package com.lanking.uxb.zycon.operation.convert;

import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.operation.value.VZycAccount;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@Component
public class ZycAccountConvert extends Converter<VZycAccount, Account, Long> {
	@Override
	protected Long getId(Account account) {
		return account.getId();
	}

	@Override
	protected VZycAccount convert(Account account) {
		VZycAccount v = new VZycAccount();
		v.setName(account.getName());
		v.setEmail(account.getEmail());
		v.setMobile(account.getMobile());
		v.setId(account.getId());
		return v;
	}
}
