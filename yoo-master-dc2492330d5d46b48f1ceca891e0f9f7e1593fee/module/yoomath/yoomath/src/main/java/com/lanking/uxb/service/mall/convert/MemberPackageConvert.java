package com.lanking.uxb.service.mall.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.mall.value.VMemberPackage;

@Component
public class MemberPackageConvert extends Converter<VMemberPackage, MemberPackage, Long> {

	@Override
	protected Long getId(MemberPackage s) {
		return s.getId();
	}

	@Override
	protected VMemberPackage convert(MemberPackage s) {
		if (s == null) {
			return null;
		}
		VMemberPackage v = new VMemberPackage();
		v.setDiscount(s.getDiscount());
		v.setId(s.getId());
		v.setMemberType(s.getMemberType());
		v.setMonth(s.getMonth());
		v.setOriginalPrice(s.getOriginalPrice());
		v.setPresentPrice(s.getPresentPrice());
		v.setSequence(s.getSequence());
		v.setStatus(s.getStatus());
		v.setTag(s.getTag());
		if (s.getCustomTag() != null) {
			v.setTagName(s.getCustomTag());
		} else {
			v.setTagName(s.getTag() != null ? s.getTag().getTitle() : "");
		}

		v.setUserType(s.getUserType());
		v.setExtraMonth(s.getExtraMonth());
		return v;
	}
}
