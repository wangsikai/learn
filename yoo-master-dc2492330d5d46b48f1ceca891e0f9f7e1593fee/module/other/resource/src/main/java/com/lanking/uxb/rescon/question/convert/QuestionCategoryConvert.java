package com.lanking.uxb.rescon.question.convert;

import com.lanking.cloud.domain.common.baseData.QuestionCategory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.question.value.VQuestionCategory;

/**
 * 习题转换参数.
 * 
 * @author sunming
 * @version 2017-07-26
 */
public class QuestionCategoryConvert  extends Converter<VQuestionCategory,QuestionCategory,Long> {

	@Override
	protected Long getId(QuestionCategory s) {
		return s.getCode();
	}

	@Override
	protected VQuestionCategory convert(QuestionCategory s) {
		VQuestionCategory v=new VQuestionCategory();
		v.setCode(s.getCode());
		v.setName(s.getName());
		v.setStatus(s.getStatus());
		return v;
	}

}
