package com.lanking.uxb.zycon.base.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.base.value.CQuestionType;

@Component
public class ZycQuestionTypeConvert extends Converter<CQuestionType, QuestionType, Integer> {

	@Override
	protected Integer getId(QuestionType s) {
		return s.getCode();
	}

	@Override
	protected CQuestionType convert(QuestionType s) {
		CQuestionType v = new CQuestionType();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setSubjectCode(s.getSubjectCode());
		return v;
	}

}
