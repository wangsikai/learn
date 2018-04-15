package com.lanking.uxb.service.imperial.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.imperial.value.VImperialExaminationActivity;

@Component
public class ImperialExaminationActivityConvert extends
		Converter<VImperialExaminationActivity, ImperialExaminationActivity, Long> {

	@Override
	protected Long getId(ImperialExaminationActivity s) {
		return s.getCode();
	}

	@Override
	protected VImperialExaminationActivity convert(ImperialExaminationActivity s) {
		VImperialExaminationActivity v = new VImperialExaminationActivity();
		v.setActivityStartTime(s.getStartTime());
		v.setActivityEndTime(s.getEndTime());
		v.setAwardList(s.getCfg().getAwardList());
		v.setIndexList(s.getCfg().getIndexList());
		return v;
	}

}
