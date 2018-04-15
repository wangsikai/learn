package com.lanking.uxb.service.resources.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.resources.value.VQuestionTag;

@Component
public class QuestionTagConvert extends Converter<VQuestionTag, QuestionTag, Long> {

	@Override
	protected Long getId(QuestionTag s) {
		return s.getCode();
	}

	@Override
	protected VQuestionTag convert(QuestionTag s) {
		VQuestionTag v = new VQuestionTag();
		v.setName(s.getName());
		v.setShortName(s.getShortName());
		v.setUrl(FileUtil.getUrl(s.getIcon()));
		v.setSequence(s.getSequence());
		return v;
	}

}
