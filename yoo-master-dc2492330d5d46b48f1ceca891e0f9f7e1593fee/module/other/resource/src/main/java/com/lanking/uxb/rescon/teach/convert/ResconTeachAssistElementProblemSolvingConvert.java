package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementProblemSolving;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementProblemSolving;

import org.springframework.stereotype.Component;

/**
 * TeachAssistElementProblemSolving -> VTeachAssistElementProblemSolving
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementProblemSolvingConvert extends
		Converter<VTeachAssistElementProblemSolving, TeachAssistElementProblemSolving, Long> {
	@Override
	protected Long getId(TeachAssistElementProblemSolving teachAssistElementProblemSolving) {
		return teachAssistElementProblemSolving.getId();
	}

	@Override
	protected VTeachAssistElementProblemSolving convert(
			TeachAssistElementProblemSolving teachAssistElementProblemSolving) {
		VTeachAssistElementProblemSolving v = new VTeachAssistElementProblemSolving();
		v.setId(teachAssistElementProblemSolving.getId());
		v.setSequence(teachAssistElementProblemSolving.getSequence());
		v.setType(teachAssistElementProblemSolving.getType());
		v.setContent(teachAssistElementProblemSolving.getContent());
		return v;
	}
}
