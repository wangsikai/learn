package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPeriodTitle;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPeriodTitle;

import org.springframework.stereotype.Component;

/**
 * TeachAssistElementPeriodTitle -> VTeachAssistElementPeriodTitle
 * 
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPeriodTitleConvert extends
		Converter<VTeachAssistElementPeriodTitle, TeachAssistElementPeriodTitle, Long> {
	@Override
	protected Long getId(TeachAssistElementPeriodTitle teachAssistElementPeriodTitle) {
		return teachAssistElementPeriodTitle.getId();
	}

	@Override
	protected VTeachAssistElementPeriodTitle convert(TeachAssistElementPeriodTitle teachAssistElementPeriodTitle) {
		VTeachAssistElementPeriodTitle v = new VTeachAssistElementPeriodTitle();
		v.setId(teachAssistElementPeriodTitle.getId());
		v.setSequence(teachAssistElementPeriodTitle.getSequence());
		v.setType(teachAssistElementPeriodTitle.getType());
		v.setTitle(teachAssistElementPeriodTitle.getTitle());

		return v;
	}
}
