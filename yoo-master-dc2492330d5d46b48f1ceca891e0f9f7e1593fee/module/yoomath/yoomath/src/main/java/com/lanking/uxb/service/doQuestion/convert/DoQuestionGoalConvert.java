package com.lanking.uxb.service.doQuestion.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.doQuestion.DoQuestionGoal;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.doQuestion.value.VDoQuestionGoal;

@Component
public class DoQuestionGoalConvert extends Converter<VDoQuestionGoal, DoQuestionGoal, Long> {

	@Autowired
	private DoQuestionGoalLevelConvert doQuestionGoalLevelConvert;

	@Override
	protected Long getId(DoQuestionGoal s) {
		return s.getUserId();
	}

	@Override
	protected VDoQuestionGoal convert(DoQuestionGoal s) {
		VDoQuestionGoal v = new VDoQuestionGoal();
		v.setUserId(s.getUserId());
		v.setGoal(s.getGoal());
		v.setLevel(doQuestionGoalLevelConvert.to(s.getLevel()));
		return v;
	}

}
