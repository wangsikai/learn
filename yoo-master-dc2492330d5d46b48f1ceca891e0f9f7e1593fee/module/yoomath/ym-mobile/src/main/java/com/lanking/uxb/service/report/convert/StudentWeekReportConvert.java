package com.lanking.uxb.service.report.convert;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.stat.StudentWeekReport;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.report.value.VStudentWeekReport;

@Component
public class StudentWeekReportConvert extends Converter<VStudentWeekReport, StudentWeekReport, Long> {

	@Override
	protected Long getId(StudentWeekReport s) {
		return s.getId();
	}

	@Override
	protected VStudentWeekReport convert(StudentWeekReport s) {
		VStudentWeekReport v = new VStudentWeekReport();
		v.setCompletionRate(s.getCompletionRate());
		v.setCompletionRateFloat(s.getCompletionRateFloat());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		v.setEndDate(sdf.format(s.getEndDate()));
		v.setStartDate(sdf.format(s.getStartDate()));
		v.setRightRate(s.getRightRate());
		v.setRightRateFloat(s.getRightRateFloat());
		v.setScore(s.getScore());
		v.setUserId(s.getUserId());
		v.setRightRateClassRanks(s.getRightRateClassRanks());
		return v;
	}
}
