package com.lanking.uxb.service.diagnostic.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.diagnostic.value.MasterStatus;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassKnowpoint;

@Component
public class DiagnosticStudentClassKnowpointConvert extends
		Converter<VDiagnosticStudentClassKnowpoint, DiagnosticStudentClassKnowpoint, Long> {
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private KnowledgePointService knowledgePointService;

	@Override
	protected Long getId(DiagnosticStudentClassKnowpoint s) {
		return s.getId();
	}

	@Override
	protected VDiagnosticStudentClassKnowpoint convert(DiagnosticStudentClassKnowpoint s) {
		VDiagnosticStudentClassKnowpoint v = new VDiagnosticStudentClassKnowpoint();
		v.setDoCount(s.getDoCount());
		v.setDoHard1Count(s.getDoHard1Count());
		v.setDoHard2Count(s.getDoHard2Count());
		v.setDoHard3Count(s.getDoHard3Count());
		if (s.getDoHard1Count() == 0) {
			v.setHard1RightRate(BigDecimal.valueOf(0));
		} else {
			v.setHard1RightRate(new BigDecimal(s.getRightHard1Count() * 100d / s.getDoHard1Count()).setScale(0,
					BigDecimal.ROUND_HALF_UP));
		}
		if (s.getDoHard2Count() == 0) {
			v.setHard2RightRate(BigDecimal.valueOf(0));
		} else {
			v.setHard2RightRate(new BigDecimal(s.getRightHard2Count() * 100d / s.getDoHard2Count()).setScale(0,
					BigDecimal.ROUND_HALF_UP));
		}
		if (s.getDoHard3Count() == 0) {
			v.setHard3RightRate(BigDecimal.valueOf(0));
		} else {
			v.setHard3RightRate(new BigDecimal(s.getRightHard3Count() * 100d / s.getDoHard3Count()).setScale(0,
					BigDecimal.ROUND_HALF_UP));
		}
		v.setKnowpointCode(s.getKnowpointCode());
		v.setMaxDifficulty(s.getMaxDifficulty());
		v.setMinDifficulty(s.getMinDifficulty());
		v.setRightCount(s.getRightCount());
		v.setRightHard1Count(s.getRightHard1Count());
		v.setRightHard2Count(s.getRightHard2Count());
		v.setRightHard3Count(s.getRightHard3Count());
		v.setRightRate(s.getRightRate());
		// UE要求做平滑处理 (n+1)/(N+2)
		Double tempRate = new BigDecimal((v.getRightCount() + 1) * 100d / (v.getDoCount() + 2)).setScale(0,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		v.setMasterRate(BigDecimal.valueOf(tempRate));
		if (tempRate > 90 && tempRate <= 100) {
			v.setMasterStatus(MasterStatus.EXCELLENT);
		} else if (tempRate > 60 && tempRate <= 90) {
			v.setMasterStatus(MasterStatus.GOOD);
		} else if (tempRate > 30 && tempRate <= 60) {
			v.setMasterStatus(MasterStatus.COMMONLY);
		} else if (tempRate >= 0 && tempRate <= 30) {
			v.setMasterStatus(MasterStatus.WEAK);
		}
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 知识体系
		assemblers
				.add(new ConverterAssembler<VDiagnosticStudentClassKnowpoint, DiagnosticStudentClassKnowpoint, Long, KnowledgeSystem>() {

					@Override
					public boolean accept(DiagnosticStudentClassKnowpoint s) {
						return String.valueOf(s.getKnowpointCode()).length() != 10;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(DiagnosticStudentClassKnowpoint s, VDiagnosticStudentClassKnowpoint d) {
						return s.getKnowpointCode();
					}

					@Override
					public void setValue(DiagnosticStudentClassKnowpoint s, VDiagnosticStudentClassKnowpoint d,
							KnowledgeSystem value) {
						d.setKnowpointName(value.getName());

					}

					@Override
					public KnowledgeSystem getValue(Long key) {
						return knowledgeSystemService.get(key);
					}

					@Override
					public Map<Long, KnowledgeSystem> mgetValue(Collection<Long> keys) {
						return knowledgeSystemService.mget(keys);
					}

				});

		// 知识点
		assemblers
				.add(new ConverterAssembler<VDiagnosticStudentClassKnowpoint, DiagnosticStudentClassKnowpoint, Long, KnowledgePoint>() {

					@Override
					public boolean accept(DiagnosticStudentClassKnowpoint s) {
						return String.valueOf(s.getKnowpointCode()).length() == 10;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(DiagnosticStudentClassKnowpoint s, VDiagnosticStudentClassKnowpoint d) {
						return s.getKnowpointCode();
					}

					@Override
					public void setValue(DiagnosticStudentClassKnowpoint s, VDiagnosticStudentClassKnowpoint d,
							KnowledgePoint value) {
						d.setKnowpointName(value.getName());

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
