package com.lanking.uxb.zycon.user.convert;

import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.user.value.VZycUser;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@Component
public class ZycUserConvert extends Converter<VZycUser, User, Long> {
	@Override
	protected Long getId(User user) {
		return user.getId();
	}

	@Override
	protected VZycUser convert(User user) {
		VZycUser v = new VZycUser();
		v.setUserId(user.getId());
		v.setName(user.getName());
		v.setAccountId(user.getAccountId());
		return v;
	}
}
