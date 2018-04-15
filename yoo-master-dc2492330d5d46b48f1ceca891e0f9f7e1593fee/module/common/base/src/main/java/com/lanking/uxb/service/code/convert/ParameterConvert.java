package com.lanking.uxb.service.code.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VParameter;

@Component
public class ParameterConvert extends Converter<VParameter, Parameter, Long> {

	@Override
	protected Long getId(Parameter s) {
		return s.getId();
	}

	@Override
	protected VParameter convert(Parameter s) {
		VParameter vo = new VParameter();
		vo.setKey(s.getKey());
		vo.setValue(s.getValue());
		return vo;
	}

}
