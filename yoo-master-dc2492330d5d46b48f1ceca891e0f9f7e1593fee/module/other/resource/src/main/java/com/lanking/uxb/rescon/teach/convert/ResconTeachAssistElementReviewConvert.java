package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementReview;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementReview;

import org.springframework.stereotype.Component;

/**
 * TeachAssistElementReview -> VTeachAssistElementReview
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementReviewConvert extends
		Converter<VTeachAssistElementReview, TeachAssistElementReview, Long> {
	@Override
	protected Long getId(TeachAssistElementReview teachAssistElementReview) {
		return teachAssistElementReview.getId();
	}

	@Override
	protected VTeachAssistElementReview convert(TeachAssistElementReview teachAssistElementReview) {
		VTeachAssistElementReview v = new VTeachAssistElementReview();
		v.setId(teachAssistElementReview.getId());
		v.setSequence(teachAssistElementReview.getSequence());
		v.setType(teachAssistElementReview.getType());
		return v;
	}
}
