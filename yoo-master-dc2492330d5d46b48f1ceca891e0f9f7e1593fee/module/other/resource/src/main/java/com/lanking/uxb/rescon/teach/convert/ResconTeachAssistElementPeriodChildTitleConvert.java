package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPeriodChildTitle;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPeriodChildTitle;

import org.springframework.stereotype.Component;

/**
 * TeachAssistElementChildTitle -> VTeachAssistElementChildTitle
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPeriodChildTitleConvert extends
		Converter<VTeachAssistElementPeriodChildTitle, TeachAssistElementPeriodChildTitle, Long> {
	@Override
	protected Long getId(TeachAssistElementPeriodChildTitle teachAssistElementPeriodChildTitle) {
		return teachAssistElementPeriodChildTitle.getId();
	}

	@Override
	protected VTeachAssistElementPeriodChildTitle convert(
			TeachAssistElementPeriodChildTitle teachAssistElementPeriodChildTitle) {
		VTeachAssistElementPeriodChildTitle v = new VTeachAssistElementPeriodChildTitle();
		v.setId(teachAssistElementPeriodChildTitle.getId());
		v.setSequence(teachAssistElementPeriodChildTitle.getSequence());
		v.setType(teachAssistElementPeriodChildTitle.getType());
		v.setTitle(teachAssistElementPeriodChildTitle.getTitle());
		return v;
	}
}
