package com.lanking.uxb.service.imperial02.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.imperial02.value.VImperialExaminationActivity2;

/**
 * 科举活动转换.
 * 
 * @author peng.zhao
 *
 * @version 2017年11月9日
 */
@Component
public class ImperialExaminationActivityConvert2
		extends Converter<VImperialExaminationActivity2, ImperialExaminationActivity, Long> {

	@Override
	protected Long getId(ImperialExaminationActivity s) {
		return s.getCode();
	}

	@Override
	protected VImperialExaminationActivity2 convert(ImperialExaminationActivity s) {
		VImperialExaminationActivity2 v = new VImperialExaminationActivity2();
		v.setActivityStartTime(s.getStartTime());
		v.setActivityEndTime(s.getEndTime());

		return v;
	}

}
