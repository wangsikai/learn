package com.lanking.uxb.service.code.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.Duty;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VDuty;

@Component
public class DutyConvert extends Converter<VDuty, Duty, Integer> {

	@Override
	protected Integer getId(Duty s) {
		return s.getCode();
	}

	@Override
	protected VDuty convert(Duty s) {
		VDuty v = new VDuty();
		v.setCode(s.getCode());
		v.setSequence(s.getSequence());
		v.setName(s.getName());
		return v;
	}
}
