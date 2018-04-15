package com.lanking.uxb.service.diagnostic.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomeworkKnowpoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomeworkKnowpoint;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Component
public class DiagnosticClassLatestHomeworkKnowpointConvert extends
		Converter<VDiagnosticClassLatestHomeworkKnowpoint, DiagnosticClassLatestHomeworkKnowpoint, Long> {
	@Autowired
	private KnowledgePointService knowledgePointService;

	@Override
	protected Long getId(DiagnosticClassLatestHomeworkKnowpoint diagnosticClassLatestHomeworkKnowpoint) {
		return diagnosticClassLatestHomeworkKnowpoint.getId();
	}

	@Override
	protected VDiagnosticClassLatestHomeworkKnowpoint convert(
			DiagnosticClassLatestHomeworkKnowpoint diagnosticClassLatestHomeworkKnowpoint) {
		VDiagnosticClassLatestHomeworkKnowpoint v = new VDiagnosticClassLatestHomeworkKnowpoint();
		v.setClassId(diagnosticClassLatestHomeworkKnowpoint.getClassId());
		v.setDoCount(diagnosticClassLatestHomeworkKnowpoint.getDoCount());
		v.setRightRate(diagnosticClassLatestHomeworkKnowpoint.getRightRate());
		v.setRightRateTitle(diagnosticClassLatestHomeworkKnowpoint.getRightRate() == null ? null
				: diagnosticClassLatestHomeworkKnowpoint.getRightRate() + "%");
		v.setCode(diagnosticClassLatestHomeworkKnowpoint.getKnowpointCode());
		v.setMasterRate(new BigDecimal((double) (diagnosticClassLatestHomeworkKnowpoint.getRightCount() + 1)
				/ (diagnosticClassLatestHomeworkKnowpoint.getDoCount() + 2)));
		return v;
	}

	// 获得知识点名称
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VDiagnosticClassLatestHomeworkKnowpoint, DiagnosticClassLatestHomeworkKnowpoint, Long, KnowledgePoint>() {

					@Override
					public boolean accept(DiagnosticClassLatestHomeworkKnowpoint diagnosticClassLatestHomeworkKnowpoint) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(DiagnosticClassLatestHomeworkKnowpoint diagnosticClassLatestHomeworkKnowpoint,
							VDiagnosticClassLatestHomeworkKnowpoint vDiagnosticClassLatestHomeworkKnowpoint) {
						return diagnosticClassLatestHomeworkKnowpoint.getKnowpointCode();
					}

					@Override
					public void setValue(DiagnosticClassLatestHomeworkKnowpoint diagnosticClassLatestHomeworkKnowpoint,
							VDiagnosticClassLatestHomeworkKnowpoint vDiagnosticClassLatestHomeworkKnowpoint,
							KnowledgePoint value) {
						vDiagnosticClassLatestHomeworkKnowpoint.setName(value.getName());
					}

					@Override
					public KnowledgePoint getValue(Long key) {
						return knowledgePointService.get(key);
					}

					@Override
					public Map<Long, KnowledgePoint> mgetValue(Collection<Long> keys) {
						return knowledgePointService.mget(keys);
					}
				});
	}
}
