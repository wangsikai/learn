package com.lanking.uxb.service.code.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VExaminationPoint;

/**
 * 考点Convert
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Component
public class ExaminationPointConvert extends Converter<VExaminationPoint, ExaminationPoint, Long> {
	@Override
	protected Long getId(ExaminationPoint examinationPoint) {
		return examinationPoint.getId();
	}

	@Override
	protected VExaminationPoint convert(ExaminationPoint examinationPoint) {
		VExaminationPoint v = new VExaminationPoint();
		v.setId(examinationPoint.getId());
		v.setName(examinationPoint.getName());
		v.setFrequency(examinationPoint.getFrequency());
		return v;
	}
}
