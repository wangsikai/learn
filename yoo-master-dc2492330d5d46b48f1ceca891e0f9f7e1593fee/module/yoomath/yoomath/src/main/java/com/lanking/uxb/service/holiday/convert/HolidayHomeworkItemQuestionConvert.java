package com.lanking.uxb.service.holiday.convert;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItemQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.holiday.value.VHolidayHomeworkItemQuestion;

@Component
public class HolidayHomeworkItemQuestionConvert extends
		Converter<VHolidayHomeworkItemQuestion, HolidayHomeworkItemQuestion, Long> {

	@Override
	protected Long getId(HolidayHomeworkItemQuestion s) {
		return s.getId();
	}

	@Override
	protected VHolidayHomeworkItemQuestion convert(HolidayHomeworkItemQuestion s) {
		VHolidayHomeworkItemQuestion v = new VHolidayHomeworkItemQuestion();
		v.setHomeworkItemId(s.getHolidayHomeworkItemId());
		v.setQuestionId(s.getQuestionId());
		v.setSequence(s.getSequence());
		v.setRightCount(s.getRightCount() == null ? 0 : s.getRightCount());
		v.setWrongCount(s.getWrongCount() == null ? 0 : s.getWrongCount());
		v.setRightRate(s.getRightRate() == null ? BigDecimal.valueOf(-1) : s.getRightRate());
		return v;
	}

}
