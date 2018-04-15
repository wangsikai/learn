package com.lanking.uxb.service.code.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.Title;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VTitle;

@Component
public class TitleConvert extends Converter<VTitle, Title, Integer> {

	@Override
	protected Integer getId(Title s) {
		return s.getCode();
	}

	@Override
	protected VTitle convert(Title s) {
		VTitle v = new VTitle();
		v.setCode(s.getCode());
		v.setSequence(s.getSequence());
		v.setName(s.getName());
		return v;
	}
}
