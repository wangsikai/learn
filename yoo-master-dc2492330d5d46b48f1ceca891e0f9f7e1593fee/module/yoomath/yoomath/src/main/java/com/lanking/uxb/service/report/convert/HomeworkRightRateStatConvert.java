package com.lanking.uxb.service.report.convert;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.stat.HomeworkRightRateStat;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.report.value.VHomeworkRightRateStat;

@Component
public class HomeworkRightRateStatConvert extends Converter<VHomeworkRightRateStat, HomeworkRightRateStat, Long> {

	@Override
	protected Long getId(HomeworkRightRateStat s) {
		return s.getId();
	}

	@Override
	protected VHomeworkRightRateStat convert(HomeworkRightRateStat s) {
		VHomeworkRightRateStat v = new VHomeworkRightRateStat();
		v.setHomeworkClassId(s.getHomeworkClassId());
		v.setRightRate(s.getRightRate());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		v.setStatisticsTime(sdf.format(s.getStatisticsTime()));
		return v;
	}
}
