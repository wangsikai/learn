package com.lanking.uxb.service.zuoye.convert;

import com.lanking.cloud.domain.yoomath.sectionPractise.SectionPractise;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.zuoye.value.VSectionPractise;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * SectionPractise -> VSectionPractise
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.1
 */
@Component
public class ZySectionPractiseConvert extends Converter<VSectionPractise, SectionPractise, Long> {
	@Override
	protected Long getId(SectionPractise sectionPractise) {
		return sectionPractise.getId();
	}

	@Override
	protected VSectionPractise convert(SectionPractise sectionPractise) {
		VSectionPractise v = new VSectionPractise();
		v.setCreateAt(sectionPractise.getCreateAt());
		v.setDifficulty(sectionPractise.getDifficulty());
		v.setDoCount(sectionPractise.getDoCount());
		v.setHomeworkTime(sectionPractise.getHomeworkTime());
		v.setId(sectionPractise.getId());
		v.setName(sectionPractise.getName());
		v.setQuestionCount(sectionPractise.getQuestionCount());
		v.setRightCount(sectionPractise.getRightCount());
		v.setRightRate(sectionPractise.getRightRate());
		v.setSectionCode(sectionPractise.getSectionCode());
		v.setUpdateAt(sectionPractise.getUpdateAt());
		v.setUserId(sectionPractise.getUserId());
		v.setWrongCount(sectionPractise.getWrongCount());

		if (null != v.getRightRate()) {
			v.setRightRateTitle(v.getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "%");
		}
		v.setCompleteRate(BigDecimal
				.valueOf((100d * sectionPractise.getDoCount()) / sectionPractise.getQuestionCount()).setScale(2,
						BigDecimal.ROUND_HALF_UP));
		v.setCompleteRateTitle(v.getCompleteRate().setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "%");
		return v;
	}
}
