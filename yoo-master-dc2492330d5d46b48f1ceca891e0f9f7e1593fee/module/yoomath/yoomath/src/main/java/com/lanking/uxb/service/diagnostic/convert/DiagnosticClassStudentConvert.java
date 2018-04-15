package com.lanking.uxb.service.diagnostic.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassStudent;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassStudent;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Component
public class DiagnosticClassStudentConvert extends Converter<VDiagnosticClassStudent, DiagnosticClassStudent, Long> {
	@Autowired
	private UserConvert userConvert;

	@Override
	protected Long getId(DiagnosticClassStudent diagnosticClassStudent) {
		return diagnosticClassStudent.getId();
	}

	@Override
	protected VDiagnosticClassStudent convert(DiagnosticClassStudent diagnosticClassStudent) {
		VDiagnosticClassStudent v = new VDiagnosticClassStudent();
		v.setFloatRank(diagnosticClassStudent.getFloatRank());
		v.setHomeworkCount(diagnosticClassStudent.getHomeworkCount());
		v.setId(diagnosticClassStudent.getId());
		v.setRank(diagnosticClassStudent.getRank());
		v.setRightRate(diagnosticClassStudent.getRightRate() == null ? null : diagnosticClassStudent.getRightRate()
				.setScale(0, BigDecimal.ROUND_HALF_UP));
		v.setRightRateTitle(v.getRightRate() == null ? null : v.getRightRate() + "%");
		v.setRightRates(diagnosticClassStudent.getRightRates());

		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VDiagnosticClassStudent, DiagnosticClassStudent, Long, VUser>() {
			@Override
			public boolean accept(DiagnosticClassStudent diagnosticClassStudent) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(DiagnosticClassStudent diagnosticClassStudent,
					VDiagnosticClassStudent vDiagnosticClassStudent) {
				return diagnosticClassStudent.getUserId();
			}

			@Override
			public void setValue(DiagnosticClassStudent diagnosticClassStudent,
					VDiagnosticClassStudent vDiagnosticClassStudent, VUser value) {
				vDiagnosticClassStudent.setUser(value);
			}

			@Override
			public VUser getValue(Long key) {
				UserConvertOption option = new UserConvertOption();
				option.setInitMemberType(true);
				return userConvert.get(key, option);
			}

			@Override
			public Map<Long, VUser> mgetValue(Collection<Long> keys) {
				UserConvertOption option = new UserConvertOption();
				option.setInitMemberType(true);
				return userConvert.mget(keys, option);
			}
		});
	}
}
