package com.lanking.uxb.rescon.teach.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficult;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistPresetContentFallibleDifficult;

@Component
public class ResconTeachAssistPresetContentFallibleDifficultConvert extends
		Converter<VTeachAssistPresetContentFallibleDifficult, TeachAssistPresetContentFallibleDifficult, Long> {

	@Override
	protected Long getId(TeachAssistPresetContentFallibleDifficult s) {
		return s.getId();
	}

	@Override
	protected VTeachAssistPresetContentFallibleDifficult convert(TeachAssistPresetContentFallibleDifficult s) {
		VTeachAssistPresetContentFallibleDifficult v = new VTeachAssistPresetContentFallibleDifficult();
		v.setAnalysis(s.getAnalysis());
		v.setCheckStatus(s.getCheckStatus());
		v.setName(s.getName());
		v.setTargetedTrainingQuestions(s.getTargetedTrainingQuestions());
		v.setTeachassistPresetcontentId(s.getTeachassistPresetcontentId());
		v.setId(s.getId());
		return v;
	}

}
