package com.lanking.uxb.channelSales.memberPackage.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.channelSales.memberPackage.value.VMemberPackage;

@Component
public class CsMemberPackageConvert extends Converter<VMemberPackage, MemberPackage, Long> {

	@Override
	protected Long getId(MemberPackage s) {
		return s.getId();
	}

	@Override
	protected VMemberPackage convert(MemberPackage s) {
		VMemberPackage vo = new VMemberPackage();
		vo.setId(s.getId());
		vo.setMemberPackageGroupId(s.getMemberPackageGroupId());
		vo.setMonth(s.getMonth());
		vo.setDiscount(s.getDiscount());
		vo.setOriginalPrice(s.getOriginalPrice());
		vo.setPresentPrice(s.getPresentPrice());
		vo.setTag(s.getTag());
		vo.setCustomTag(s.getCustomTag() == null ? "" : s.getCustomTag());
		vo.setMemberType(s.getMemberType());
		vo.setUserType(s.getUserType());
		vo.setSequence(s.getSequence());
		vo.setExtraMonth(s.getExtraMonth());
		return vo;
	}
}
