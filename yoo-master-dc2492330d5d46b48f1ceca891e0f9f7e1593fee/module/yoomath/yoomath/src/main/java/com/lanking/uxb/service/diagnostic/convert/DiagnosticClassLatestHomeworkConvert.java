package com.lanking.uxb.service.diagnostic.convert;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomework;

import org.springframework.stereotype.Component;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Component
public class DiagnosticClassLatestHomeworkConvert extends
		Converter<VDiagnosticClassLatestHomework, DiagnosticClassLatestHomework, Long> {
	@Override
	protected Long getId(DiagnosticClassLatestHomework diagnosticClassLatestHomework) {
		return diagnosticClassLatestHomework.getId();
	}

	@Override
	protected VDiagnosticClassLatestHomework convert(DiagnosticClassLatestHomework diagnosticClassLatestHomework) {
		VDiagnosticClassLatestHomework v = new VDiagnosticClassLatestHomework();
		v.setId(diagnosticClassLatestHomework.getId());
		v.setClassId(diagnosticClassLatestHomework.getClassId());
		v.setClassRank(diagnosticClassLatestHomework.getClassRank());
		v.setDifficulty(diagnosticClassLatestHomework.getDifficulty());
		v.setHomeworkId(diagnosticClassLatestHomework.getHomeworkId());
		v.setName(diagnosticClassLatestHomework.getName());
		v.setRightRate(diagnosticClassLatestHomework.getRightRate());
		v.setStartTime(diagnosticClassLatestHomework.getStartTime());
		v.setRightRateTitle(diagnosticClassLatestHomework.getRightRate() == null ? null : diagnosticClassLatestHomework
				.getRightRate() + "%");

		return v;
	}
}
