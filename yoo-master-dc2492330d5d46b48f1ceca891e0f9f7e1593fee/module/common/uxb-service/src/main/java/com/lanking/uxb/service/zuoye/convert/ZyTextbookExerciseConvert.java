package com.lanking.uxb.service.zuoye.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.zuoye.value.VTextbookExercise;

@Component
public class ZyTextbookExerciseConvert extends Converter<VTextbookExercise, TextbookExercise, Long> {

	@Override
	protected Long getId(TextbookExercise s) {
		return s.getId();
	}

	@Override
	protected VTextbookExercise convert(TextbookExercise s) {
		VTextbookExercise v = new VTextbookExercise();
		v.setId(s.getId());
		v.setName(s.getName());
		v.setSectionCode(s.getSectionCode());
		v.setTextbookCode(s.getTextbookCode());
		v.setType(s.getType());
		return v;
	}

}
