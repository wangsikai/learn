package com.lanking.uxb.service.holiday.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomeworkItemAnswer;
import com.lanking.uxb.service.question.util.QuestionUtils;

/**
 * 假期学生作业专项题目答案convert
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
@Component
public class HolidayStuHomeworkItemAnswerConvert
		extends Converter<VHolidayStuHomeworkItemAnswer, HolidayStuHomeworkItemAnswer, Long> {

	@Override
	protected Long getId(HolidayStuHomeworkItemAnswer s) {
		return s.getId();
	}

	@Override
	protected VHolidayStuHomeworkItemAnswer convert(HolidayStuHomeworkItemAnswer s) {
		VHolidayStuHomeworkItemAnswer v = new VHolidayStuHomeworkItemAnswer();
		v.setId(s.getId());
		v.setHolidayHomeworkId(s.getHolidayHomeworkId());
		v.setHolidayStuHomeworkId(s.getHolidayStuHomeworkId());
		v.setHolidayStuHomeworkItemId(s.getHolidayStuHomeworkItemId());
		v.setHolidayStuHomeworkItemQuestionId(s.getHolidayStuHomeworkItemQuestionId());
		v.setResult(s.getResult());
		v.setAnswerAt(s.getAnswerAt());
		v.setComment(s.getComment());
		v.setType(s.getType());
		v.setCorrectAt(s.getCorrectAt());
		v.setSequence(s.getSequence());
		v.setContent(validBlank(s.getContent()));
		v.setContentAscii(validBlank(s.getContentAscii()));
		v.setNoLabelContentAscii(
				validBlank(s.getContentAscii()).replaceAll("<ux-mth>", "").replaceAll("</ux-mth>", ""));
		v.setImageContent(QuestionUtils.process(validBlank(s.getContent()), null, true));
		return v;
	}
}
