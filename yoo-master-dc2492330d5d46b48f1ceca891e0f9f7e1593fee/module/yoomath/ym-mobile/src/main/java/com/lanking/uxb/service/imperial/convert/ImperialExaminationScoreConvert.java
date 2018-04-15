package com.lanking.uxb.service.imperial.convert;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.imperial.value.VImperialExaminationScore;

@Component
public class ImperialExaminationScoreConvert extends Converter<VImperialExaminationScore, Homework, Long> {

	@Override
	protected Long getId(Homework s) {
		return s.getId();
	}

	@Override
	protected VImperialExaminationScore convert(Homework s) {
		VImperialExaminationScore v = new VImperialExaminationScore();
		v.setBestClassId(s.getHomeworkClassId());
		if (s.getStatus() == HomeworkStatus.ISSUED) {
			v.setCommitRate(BigDecimal.valueOf(s.getCommitCount() * 100 / s.getDistributeCount()));
			v.setHomeworkTime(s.getHomeworkTime());
			v.setRightRate(s.getRightRate());
		}
		return v;
	}

}
