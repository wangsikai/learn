package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFalliblePointContent;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementFalliblePointContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementFalliblePointContentConvert extends
		Converter<VTeachAssistElementFalliblePointContent, TeachAssistElementFalliblePointContent, Long> {

	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;

	@Override
	protected Long getId(TeachAssistElementFalliblePointContent teachAssistElementFalliblePointContent) {
		return teachAssistElementFalliblePointContent.getId();
	}

	@Override
	protected VTeachAssistElementFalliblePointContent convert(
			TeachAssistElementFalliblePointContent teachAssistElementFalliblePointContent) {
		VTeachAssistElementFalliblePointContent v = new VTeachAssistElementFalliblePointContent();
		v.setAnalysis(teachAssistElementFalliblePointContent.getAnalysis());
		v.setId(teachAssistElementFalliblePointContent.getId());
		v.setName(teachAssistElementFalliblePointContent.getName());
		v.setSequence(teachAssistElementFalliblePointContent.getSequence());
		v.setStrategy(teachAssistElementFalliblePointContent.getStrategy());
		v.setWrongAnalysis(teachAssistElementFalliblePointContent.getWrongAnalysis());
		v.setWrongAnswer(teachAssistElementFalliblePointContent.getWrongAnswer());
		v.setFallpointId(teachAssistElementFalliblePointContent.getFallpointId());

		// 处理例题
		v.setFallExampleQuestion(questionConvert.to(questionManage.get(teachAssistElementFalliblePointContent
				.getFallExampleQuestion())));
		v.setPracticeQuestions(questionConvert.to(questionManage.mgetList(teachAssistElementFalliblePointContent
				.getPracticeQuestions())));
		v.setPracticeQuestionIds(teachAssistElementFalliblePointContent.getPracticeQuestions());

		return v;
	}

}
