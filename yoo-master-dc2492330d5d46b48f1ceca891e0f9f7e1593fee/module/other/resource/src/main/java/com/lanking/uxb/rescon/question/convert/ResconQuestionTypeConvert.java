package com.lanking.uxb.rescon.question.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.question.value.VQuestionType;

@Component
public class ResconQuestionTypeConvert extends Converter<VQuestionType, QuestionType, Integer> {

	@Override
	protected Integer getId(QuestionType s) {
		return s.getCode();
	}

	@Override
	protected VQuestionType convert(QuestionType s) {
		VQuestionType v = new VQuestionType();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setSubjectCode(s.getSubjectCode());
		return v;
	}

}
