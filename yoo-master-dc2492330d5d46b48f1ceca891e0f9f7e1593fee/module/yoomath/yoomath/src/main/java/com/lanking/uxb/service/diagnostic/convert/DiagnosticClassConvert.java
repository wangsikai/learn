package com.lanking.uxb.service.diagnostic.convert;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClass;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClass;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Component
public class DiagnosticClassConvert extends Converter<VDiagnosticClass, DiagnosticClass, Long> {
	@Override
	protected Long getId(DiagnosticClass diagnosticClass) {
		return diagnosticClass.getId();
	}

	@Override
	protected VDiagnosticClass convert(DiagnosticClass diagnosticClass) {
		VDiagnosticClass v = new VDiagnosticClass();

		v.setClassId(diagnosticClass.getClassId());
		v.setDoCountMonth(diagnosticClass.getDoCountMonth());
		v.setDoHard1Count(diagnosticClass.getDoHard1Count());
		v.setDoHard2Count(diagnosticClass.getDoHard2Count());
		v.setDoHard3Count(diagnosticClass.getDoHard3Count());
		v.setRightHard1Count(diagnosticClass.getRightHard1Count());
		v.setRightHard2Count(diagnosticClass.getRightHard2Count());
		v.setRightHard3Count(diagnosticClass.getRightHard3Count());

		if (v.getDoHard1Count() > 0) {
			v.setDoHard1RightRate(new BigDecimal(v.getRightHard1Count() * 100d / (v.getDoHard1Count())).setScale(0,
					RoundingMode.HALF_UP));
			v.setDoHard1RightRateTitle(v.getDoHard1RightRate() + "%");
		}
		if (v.getDoHard2Count() > 0) {
			v.setDoHard2RightRate(new BigDecimal(v.getRightHard2Count() * 100d / (v.getDoHard2Count())).setScale(0,
					RoundingMode.HALF_UP));
			v.setDoHard2RightRateTitle(v.getDoHard2RightRate() + "%");
		}
		if (v.getDoHard3Count() > 0) {
			v.setDoHard3RightRate(new BigDecimal(v.getRightHard3Count() * 100d / (v.getDoHard3Count())).setScale(0,
					RoundingMode.HALF_UP));
			v.setDoHard3RightRateTitle(v.getDoHard3RightRate() + "%");
		}

		return v;
	}
}
