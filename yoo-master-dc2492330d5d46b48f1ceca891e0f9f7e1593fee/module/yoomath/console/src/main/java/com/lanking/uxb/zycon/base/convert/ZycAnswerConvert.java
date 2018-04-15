package com.lanking.uxb.zycon.base.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.base.value.CAnswer;

@Component
public class ZycAnswerConvert extends Converter<CAnswer, Answer, Long> {

	@Override
	protected Long getId(Answer s) {
		return s.getId();
	}

	@Override
	protected CAnswer convert(Answer s) {
		CAnswer v = new CAnswer();
		v.setId(s.getId());
		v.setQuestionId(s.getQuestionId());
		v.setSequence(s.getSequence());
		v.setContent(validBlank(s.getContent()));
		return v;
	}

}
