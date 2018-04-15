package com.lanking.uxb.zycon.user.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.user.value.VZycSchool;

@Component
public class ZycUserSchoolConvert extends Converter<VZycSchool, School, Long> {

	@Override
	protected Long getId(School s) {
		return s.getId();
	}

	@Override
	protected VZycSchool convert(School s) {
		VZycSchool v = new VZycSchool();
		v.setId(s.getId());
		v.setSchoolName(s.getName());
		return v;
	}
}
