package com.lanking.uxb.service.resources.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.value.VAnswer;
import com.lanking.uxb.service.session.api.impl.Security;

@Component
public class AnswerConvert extends Converter<VAnswer, Answer, Long> {

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
		// 为客户端处理
		if (Security.isClient()) {
			v.setContent(QuestionUtils.process(v.getContent(), null, true));
		}
		return v;
	}

}
