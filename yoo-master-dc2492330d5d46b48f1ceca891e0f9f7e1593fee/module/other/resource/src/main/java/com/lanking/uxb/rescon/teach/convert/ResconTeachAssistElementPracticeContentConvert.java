package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPracticeContent;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPracticeContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TeachAssistElementPracticeContent -> VTeachAssistElementPracticeContent
 * 
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPracticeContentConvert extends
		Converter<VTeachAssistElementPracticeContent, TeachAssistElementPracticeContent, Long> {
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;

	@Override
	protected Long getId(TeachAssistElementPracticeContent teachAssistElementPracticeContent) {
		return teachAssistElementPracticeContent.getId();
	}

	@Override
	protected VTeachAssistElementPracticeContent convert(
			TeachAssistElementPracticeContent teachAssistElementPracticeContent) {
		VTeachAssistElementPracticeContent v = new VTeachAssistElementPracticeContent();
		v.setId(teachAssistElementPracticeContent.getId());
		v.setName(teachAssistElementPracticeContent.getName());
		v.setPracticeId(teachAssistElementPracticeContent.getPracticeId());
		v.setSequence(teachAssistElementPracticeContent.getSequence());
		v.setQuestions(questionConvert.to(questionManage.mgetList(teachAssistElementPracticeContent.getQuestions())));
		v.setQuestionIds(teachAssistElementPracticeContent.getQuestions());
		return v;
	}
}
