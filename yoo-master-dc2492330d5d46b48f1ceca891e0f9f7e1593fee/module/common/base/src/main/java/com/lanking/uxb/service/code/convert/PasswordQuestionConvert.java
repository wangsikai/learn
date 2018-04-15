package com.lanking.uxb.service.code.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.PasswordQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VPasswordQuestion;

@Component
public class PasswordQuestionConvert extends Converter<VPasswordQuestion, PasswordQuestion, Integer> {

	@Override
	protected Integer getId(PasswordQuestion s) {
		return s.getCode();
	}

	@Override
	protected VPasswordQuestion convert(PasswordQuestion s) {
		VPasswordQuestion v = new VPasswordQuestion();
		v.setCode(s.getCode());
		v.setName(s.getName());
		return v;
	}
}
