package com.lanking.uxb.zycon.base.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.base.value.CTextbookExercise;

@Component
public class ZycTextbookExerciseConvert extends Converter<CTextbookExercise, TextbookExercise, Long> {

	@Override
	protected Long getId(TextbookExercise s) {
		return s.getId();
	}

	@Override
	protected CTextbookExercise convert(TextbookExercise s) {
		CTextbookExercise v = new CTextbookExercise();
		v.setId(s.getId());
		v.setName(s.getName());
		v.setSectionCode(s.getSectionCode());
		v.setTextbookCode(s.getTextbookCode());
		v.setStatus(s.getStatus());
		return v;
	}

}
