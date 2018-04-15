package com.lanking.uxb.service.resources.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.resources.value.VQuestionType;

@Component
public class QuestionTypeConvert extends Converter<VQuestionType, QuestionType, Integer> {

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
