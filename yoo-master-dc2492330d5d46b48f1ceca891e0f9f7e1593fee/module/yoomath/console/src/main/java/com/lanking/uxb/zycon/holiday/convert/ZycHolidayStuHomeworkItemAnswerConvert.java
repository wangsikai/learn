package com.lanking.uxb.zycon.holiday.convert;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.homework.value.VZycStudentHomeworkAnswer;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
@Component
public class ZycHolidayStuHomeworkItemAnswerConvert extends
		Converter<VZycStudentHomeworkAnswer, HolidayStuHomeworkItemAnswer, Long> {

	@Override
	protected Long getId(HolidayStuHomeworkItemAnswer holidayStuHomeworkItemAnswer) {
		return holidayStuHomeworkItemAnswer.getId();
	}

	@Override
	protected VZycStudentHomeworkAnswer convert(HolidayStuHomeworkItemAnswer holidayStuHomeworkItemAnswer) {
		VZycStudentHomeworkAnswer v = new VZycStudentHomeworkAnswer();
		v.setAnswerAt(holidayStuHomeworkItemAnswer.getAnswerAt());
		v.setAnswerId(holidayStuHomeworkItemAnswer.getAnswerId());
		v.setContent(holidayStuHomeworkItemAnswer.getContent());
		v.setContentAscii(holidayStuHomeworkItemAnswer.getContentAscii());
		v.setCorrectAt(holidayStuHomeworkItemAnswer.getCorrectAt());
		v.setId(holidayStuHomeworkItemAnswer.getId());
		v.setResult(holidayStuHomeworkItemAnswer.getResult());
		v.setSequence(holidayStuHomeworkItemAnswer.getSequence());
		v.setStudentHomeworkQuestionId(holidayStuHomeworkItemAnswer.getHolidayStuHomeworkItemQuestionId());
		return v;
	}
}
