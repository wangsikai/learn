package com.lanking.uxb.service.code.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;

@Component
public class MetaKnowpointConvert extends Converter<VMetaKnowpoint, MetaKnowpoint, Integer> {

	@Override
	protected Integer getId(MetaKnowpoint s) {
		return s.getCode();
	}

	@Override
	protected VMetaKnowpoint convert(MetaKnowpoint s) {
		VMetaKnowpoint v = new VMetaKnowpoint();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setSubjectCode(s.getSubjectCode());
		return v;
	}
}
