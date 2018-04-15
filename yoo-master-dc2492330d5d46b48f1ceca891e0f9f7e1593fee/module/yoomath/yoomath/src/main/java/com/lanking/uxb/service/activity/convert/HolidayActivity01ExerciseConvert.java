package com.lanking.uxb.service.activity.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Exercise;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.activity.value.VHolidayActivity01Exercise;

@Component
public class HolidayActivity01ExerciseConvert
		extends Converter<VHolidayActivity01Exercise, HolidayActivity01Exercise, Long> {

	@Override
	protected Long getId(HolidayActivity01Exercise s) {
		return s.getId();
	}

	@Override
	protected VHolidayActivity01Exercise convert(HolidayActivity01Exercise s) {
		VHolidayActivity01Exercise vo = new VHolidayActivity01Exercise();
		vo.setId(s.getId());
		vo.setName(s.getName());
		vo.setQuestionCount(s.getQuestionCount());
		vo.setSequence(s.getSequence());
		return vo;
	}

}
