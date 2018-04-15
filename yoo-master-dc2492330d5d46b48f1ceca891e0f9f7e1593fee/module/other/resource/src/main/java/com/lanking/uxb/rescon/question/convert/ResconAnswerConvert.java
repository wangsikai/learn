package com.lanking.uxb.rescon.question.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.question.value.VAnswer;

@Component
public class ResconAnswerConvert extends Converter<VAnswer, Answer, Long> {

	@Override
	protected Long getId(Answer s) {
		return s.getId();
	}

	private String delPContent(String str) {
		if (StringUtils.isNotBlank(str) && str.indexOf("<p>") == 0 && str.lastIndexOf("</p>") == str.length() - 4) {
			// 去除首尾的P标签
			str = str.substring(3, str.length() - 4);
		}
		return str;
	}

	@Override
	protected VAnswer convert(Answer s) {
		VAnswer v = new VAnswer();
		v.setId(s.getId());
		v.setQuestionId(s.getQuestionId());
		v.setSequence(s.getSequence());
		v.setContent(this.delPContent(validBlank(s.getContent())));
		v.setContentAscii(this.delPContent(validBlank(s.getContentAscii())));
		v.setContentLatex(this.delPContent(validBlank(s.getContentLatex())));
		return v;
	}

}
