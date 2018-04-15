package com.lanking.uxb.service.resources.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;

@Component
public class StudentHomeworkAnswerConvert extends Converter<VStudentHomeworkAnswer, StudentHomeworkAnswer, Long> {

	@Override
	protected Long getId(StudentHomeworkAnswer s) {
		return s.getId();
	}

	@Override
	protected VStudentHomeworkAnswer convert(StudentHomeworkAnswer s) {
		VStudentHomeworkAnswer v = new VStudentHomeworkAnswer();
		v.setAnswerAt(s.getAnswerAt());
		v.setAnswerId(s.getAnswerId() == null ? 0 : s.getAnswerId());
		v.setContent(validBlank(s.getContent()));
		v.setContentAscii(validBlank(s.getContentAscii()));
		v.setNoLabelContentAscii(
				validBlank(s.getContentAscii()).replaceAll("<ux-mth>", "").replaceAll("</ux-mth>", ""));
		v.setImageContent(QuestionUtils.process(validBlank(s.getContent()), null, true));
		v.setCorrectAt(s.getCorrectAt());
		v.setId(s.getId());
		v.setResult(s.getResult());
		v.setSequence(s.getSequence());
		v.setStudentHomeworkQuestionId(s.getStudentHomeworkQuestionId());
		return v;
	}
}
