package com.lanking.uxb.service.diagnostic.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomeworkKnowpoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassLatestHomeworkKnowpoint;

/**
 * 学生诊断最近作业知识点掌握情况convert
 * 
 * @author wangsenhao
 *
 */
@Component
public class DiagnosticStudentClassLatestHomeworkKnowpointConvert extends
		Converter<VDiagnosticStudentClassLatestHomeworkKnowpoint, DiagnosticStudentClassLatestHomeworkKnowpoint, Long> {

	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private KnowledgePointService knowledgePointService;

	@Override
	protected Long getId(DiagnosticStudentClassLatestHomeworkKnowpoint s) {
		return s.getId();
	}

	@Override
	protected VDiagnosticStudentClassLatestHomeworkKnowpoint convert(DiagnosticStudentClassLatestHomeworkKnowpoint s) {
		VDiagnosticStudentClassLatestHomeworkKnowpoint v = new VDiagnosticStudentClassLatestHomeworkKnowpoint();
		v.setDoCount(s.getDoCount());
		v.setKnowpointCode(s.getKnowpointCode());
		v.setRightCount(s.getRightCount());
		v.setRightRate(s.getRightRate());
		v.setClassId(s.getClassId());
		// UE要求做平滑处理 (n+1)/(N+2)
		Double tempRate = new BigDecimal((v.getRightCount() + 1) * 100d / (v.getDoCount() + 2)).setScale(0,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		v.setMasterRate(BigDecimal.valueOf(tempRate));
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 知识体系
		assemblers
				.add(new ConverterAssembler<VDiagnosticStudentClassLatestHomeworkKnowpoint, DiagnosticStudentClassLatestHomeworkKnowpoint, Long, KnowledgeSystem>() {

					@Override
					public boolean accept(DiagnosticStudentClassLatestHomeworkKnowpoint s) {
						return String.valueOf(s.getKnowpointCode()).length() != 10;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(DiagnosticStudentClassLatestHomeworkKnowpoint s,
							VDiagnosticStudentClassLatestHomeworkKnowpoint d) {
						return s.getKnowpointCode();
					}

					@Override
					public void setValue(DiagnosticStudentClassLatestHomeworkKnowpoint s,
							VDiagnosticStudentClassLatestHomeworkKnowpoint d, KnowledgeSystem value) {
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
				.add(new ConverterAssembler<VDiagnosticStudentClassLatestHomeworkKnowpoint, DiagnosticStudentClassLatestHomeworkKnowpoint, Long, KnowledgePoint>() {

					@Override
					public boolean accept(DiagnosticStudentClassLatestHomeworkKnowpoint s) {
						return String.valueOf(s.getKnowpointCode()).length() == 10;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(DiagnosticStudentClassLatestHomeworkKnowpoint s,
							VDiagnosticStudentClassLatestHomeworkKnowpoint d) {
						return s.getKnowpointCode();
					}

					@Override
					public void setValue(DiagnosticStudentClassLatestHomeworkKnowpoint s,
							VDiagnosticStudentClassLatestHomeworkKnowpoint d, KnowledgePoint value) {
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
