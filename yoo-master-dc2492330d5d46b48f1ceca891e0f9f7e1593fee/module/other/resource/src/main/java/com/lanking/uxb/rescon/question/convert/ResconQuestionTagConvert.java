package com.lanking.uxb.rescon.question.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.question.value.VQuestionTag;
import com.lanking.uxb.service.file.util.FileUtil;

@Component
public class ResconQuestionTagConvert extends Converter<VQuestionTag, QuestionTag, Long> {

	@Override
	protected Long getId(QuestionTag s) {
		return s.getCode();
	}

	@Override
	protected VQuestionTag convert(QuestionTag s) {
		if (s != null) {
			VQuestionTag v = new VQuestionTag();
			v.setCfg(s.getCfg());
			v.setCode(s.getCode());
			v.setIcon(s.getIcon());
			v.setName(s.getName());
			v.setShortName(s.getShortName());
			v.setSequence(s.getSequence());
			v.setStatus(s.getStatus());
			v.setType(s.getType());
			if (s.getIcon() != null) {
				v.setUrl(FileUtil.getUrl(s.getIcon()));
			}
			return v;
		}
		return null;
	}

}
