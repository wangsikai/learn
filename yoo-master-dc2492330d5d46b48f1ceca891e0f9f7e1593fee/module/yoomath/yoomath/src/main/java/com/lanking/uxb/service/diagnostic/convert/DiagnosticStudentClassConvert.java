package com.lanking.uxb.service.diagnostic.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClass;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClass;

@Component
public class DiagnosticStudentClassConvert extends Converter<VDiagnosticStudentClass, DiagnosticStudentClass, Long> {

	@Override
	protected Long getId(DiagnosticStudentClass s) {
		return s.getId();
	}

	@Override
	protected VDiagnosticStudentClass convert(DiagnosticStudentClass s) {
		VDiagnosticStudentClass v = new VDiagnosticStudentClass();
		v.setDoCountMonth(s.getDoCountMonth());
		v.setDoHard1Count(s.getDoHard1Count());
		v.setDoHard2Count(s.getDoHard2Count());
		v.setDoHard3Count(s.getDoHard3Count());
		v.setRightCountMonth(s.getRightCountMonth());
		v.setRightHard1Count(s.getRightHard1Count());
		v.setRightHard2Count(s.getRightHard2Count());
		v.setRightHard3Count(s.getRightHard3Count());
		return v;
	}

}
