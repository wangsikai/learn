package com.lanking.uxb.service.diagnostic.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassLatestHomework;

@Component
public class DiagnosticStudentClassLatestHomeworkConvert extends
		Converter<VDiagnosticStudentClassLatestHomework, DiagnosticStudentClassLatestHomework, Long> {

	@Override
	protected Long getId(DiagnosticStudentClassLatestHomework s) {
		return s.getId();
	}

	@Override
	protected VDiagnosticStudentClassLatestHomework convert(DiagnosticStudentClassLatestHomework s) {
		VDiagnosticStudentClassLatestHomework v = new VDiagnosticStudentClassLatestHomework();
		v.setClassRightRate(s.getClassRightRate());
		v.setDifficulty(s.getDifficulty());
		v.setName(s.getName());
		v.setRank(s.getRank());
		v.setRightRate(s.getRightRate());
		v.setStartTime(s.getStartTime());
		v.setClassId(s.getClassId());
		return v;
	}

}
