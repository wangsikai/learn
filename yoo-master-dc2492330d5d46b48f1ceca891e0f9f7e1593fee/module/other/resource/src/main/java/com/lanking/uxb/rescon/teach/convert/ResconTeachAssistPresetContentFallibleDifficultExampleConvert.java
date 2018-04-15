package com.lanking.uxb.rescon.teach.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficultExample;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistPresetContentFallibleDifficultExample;

@Component
public class ResconTeachAssistPresetContentFallibleDifficultExampleConvert
		extends
		Converter<VTeachAssistPresetContentFallibleDifficultExample, TeachAssistPresetContentFallibleDifficultExample, Long> {

	@Override
	protected Long getId(TeachAssistPresetContentFallibleDifficultExample s) {
		return s.getId();
	}

	@Override
	protected VTeachAssistPresetContentFallibleDifficultExample convert(
			TeachAssistPresetContentFallibleDifficultExample s) {
		VTeachAssistPresetContentFallibleDifficultExample v = new VTeachAssistPresetContentFallibleDifficultExample();
		v.setQuestionId(s.getQuestionId());
		v.setSolvingStrategy(s.getSolvingStrategy());
		v.setWrongAnalysis(s.getWrongAnalysis());
		v.setWrongAnswer(s.getWrongAnswer());
		v.setId(s.getId());
		v.setTeachassistPcFallibleDifficultId(s.getTeachassistPcFallibleDifficultId());
		return v;
	}

}
