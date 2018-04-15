package com.lanking.uxb.rescon.question.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.QuestionCategory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.question.value.VQuestionCategory;

/**
 * 习题类别转换参数.
 * 
 * @author sunming
 * @version 2017-07-26
 */
@Component
public class ResconQuestionCategoryConvert extends Converter<VQuestionCategory, QuestionCategory, Long> {

	@Override
	protected Long getId(QuestionCategory s) {
		return s.getCode();
	}

	@Override
	protected VQuestionCategory convert(QuestionCategory s) {
		if (s == null) {
			return null;
		}
		VQuestionCategory v = new VQuestionCategory();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setStatus(s.getStatus());
		return v;
	}
}
