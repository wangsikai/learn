package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLearnGoal;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementLearnGoal;

import org.springframework.stereotype.Component;

/**
 * TeachAssistElementLearnGoal -> VTeachAssistElementLearnGoal
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementLearnGoalConvert extends
		Converter<VTeachAssistElementLearnGoal, TeachAssistElementLearnGoal, Long> {
	@Override
	protected Long getId(TeachAssistElementLearnGoal teachAssistElementLearnGoal) {
		return teachAssistElementLearnGoal.getId();
	}

	@Override
	protected VTeachAssistElementLearnGoal convert(TeachAssistElementLearnGoal teachAssistElementLearnGoal) {
		VTeachAssistElementLearnGoal v = new VTeachAssistElementLearnGoal();
		v.setId(teachAssistElementLearnGoal.getId());
		v.setSequence(teachAssistElementLearnGoal.getSequence());
		v.setType(teachAssistElementLearnGoal.getType());
		v.setContent(teachAssistElementLearnGoal.getContent());

		return v;
	}
}
