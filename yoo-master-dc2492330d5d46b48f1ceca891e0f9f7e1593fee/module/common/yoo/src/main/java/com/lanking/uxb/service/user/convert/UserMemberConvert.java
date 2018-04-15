package com.lanking.uxb.service.user.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.user.value.VUserMember;

@Component
public class UserMemberConvert extends Converter<VUserMember, UserMember, Long> {

	@Override
	protected Long getId(UserMember s) {
		return s.getId();
	}

	@Override
	protected VUserMember convert(UserMember s) {
		VUserMember v = new VUserMember();
		v.setId(s.getId());
		v.setMemberType(s.getMemberType());
		v.setStartAt(s.getStartAt());
		v.setEndAt(s.getEndAt());
		return v;
	}

}
