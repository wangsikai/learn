package com.lanking.uxb.rescon.teach.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContent;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistPresetContent;

@Component
public class ResconTeachAssistPresetContentConvert extends
		Converter<VTeachAssistPresetContent, TeachAssistPresetContent, Long> {

	@Override
	protected Long getId(TeachAssistPresetContent s) {
		return s.getId();
	}

	@Override
	protected VTeachAssistPresetContent convert(TeachAssistPresetContent s) {
		VTeachAssistPresetContent v = new VTeachAssistPresetContent();
		v.setKnowledgeSystemCode(s.getKnowledgeSystemCode());
		v.setLearningGoals(s.getLearningGoals());
		v.setSolvingMethod(s.getSolvingMethod());
		return v;
	}

}
