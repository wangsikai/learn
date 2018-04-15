package com.lanking.uxb.zycon.homework.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.homework.value.VZycStudentHomeworkAnswer;

@Component
public class ZycStudentHomeworkAnswerConvert extends Converter<VZycStudentHomeworkAnswer, StudentHomeworkAnswer, Long> {

	@Override
	protected Long getId(StudentHomeworkAnswer s) {
		return s.getId();
	}

	@Override
	protected VZycStudentHomeworkAnswer convert(StudentHomeworkAnswer s) {
		VZycStudentHomeworkAnswer v = new VZycStudentHomeworkAnswer();
		v.setAnswerAt(s.getAnswerAt());
		v.setAnswerId(s.getAnswerId() == null ? 0 : s.getAnswerId());
		v.setContent(validBlank(s.getContent()));
		v.setContentAscii(validBlank(s.getContentAscii()));
		v.setCorrectAt(s.getCorrectAt());
		v.setId(s.getId());
		v.setResult(s.getResult());
		v.setSequence(s.getSequence());
		v.setStudentHomeworkQuestionId(s.getStudentHomeworkQuestionId());
		return v;
	}
}
