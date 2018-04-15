package com.lanking.uxb.service.doQuestion.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoalLevel;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.doQuestion.value.VDoQuestionGoalLevel;

@Component
public class DoQuestionGoalLevelConvert extends Converter<VDoQuestionGoalLevel, DoQuestionGoalLevel, Integer> {

	@Override
	protected Integer getId(DoQuestionGoalLevel s) {
		return s.getValue();
	}

	@Override
	protected VDoQuestionGoalLevel convert(DoQuestionGoalLevel s) {
		VDoQuestionGoalLevel v = new VDoQuestionGoalLevel();
		v.setCode(s.name());
		v.setValue(s.getValue());
		v.setGoal(s.getGoal());
		v.setName(s.getName());
		v.setUrl(s.getUrl());
		return v;
	}
}
