package com.lanking.uxb.zycon.qs.convert;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VSchool;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Component
public class ZycSchoolConverter extends Converter<VSchool, School, Long> {
	@Override
	protected Long getId(School school) {
		return school.getId();
	}

	@Override
	protected VSchool convert(School school) {
		VSchool v = new VSchool();
		v.setName(school.getName());
		v.setId(school.getId());
		return v;
	}
}
