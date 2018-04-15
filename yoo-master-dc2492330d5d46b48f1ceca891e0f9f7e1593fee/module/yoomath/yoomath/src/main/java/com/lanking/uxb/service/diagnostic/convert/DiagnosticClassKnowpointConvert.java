package com.lanking.uxb.service.diagnostic.convert;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassTopnKnowpointService;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassKnowpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Component
public class DiagnosticClassKnowpointConvert extends
		Converter<VDiagnosticClassKnowpoint, DiagnosticClassKnowpoint, Long> {
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private DiagnosticClassTopnKnowpointService topnKnowpointService;

	@Override
	protected Long getId(DiagnosticClassKnowpoint diagnosticClassKnowpoint) {
		return diagnosticClassKnowpoint.getId();
	}

	@Override
	protected VDiagnosticClassKnowpoint convert(DiagnosticClassKnowpoint p) {
		VDiagnosticClassKnowpoint v = new VDiagnosticClassKnowpoint();

		v.setMinDifficulty(p.getMinDifficulty());
		v.setMaxDifficulty(p.getMaxDifficulty());
		v.setClassId(p.getClassId());
		v.setDoCount(p.getDoCount());
		v.setRightCount(p.getRightCount());
		v.setDoHard1Count(p.getDoHard1Count());
		v.setDoHard2Count(p.getDoHard2Count());
		v.setDoHard3Count(p.getDoHard3Count());
		v.setRightHard1Count(p.getRightHard1Count());
		v.setRightHard2Count(p.getRightHard2Count());
		v.setRightHard3Count(p.getRightHard3Count());

		v.setRightRate(p.getRightRate());
		v.setRightRateTitle(p.getRightRate() == null ? null : p.getRightRate() + "%");

		v.setAvgDifficulty(new BigDecimal((v.getMaxDifficulty().doubleValue() + v.getMinDifficulty().doubleValue()) / 2)
				.setScale(2, BigDecimal.ROUND_HALF_UP));
		v.setKnowledgeCode(p.getKnowpointCode());

		return v;
	}

	/**
	 * 组装知识点完成情况数据
	 *
	 * @param src
	 *            待组装的非树型列表
	 * @return {@link List}
	 */
	public List<VDiagnosticClassKnowpoint> assembleTree(List<VDiagnosticClassKnowpoint> src) {
		List<VDiagnosticClassKnowpoint> dest = Lists.newArrayList();
		for (VDiagnosticClassKnowpoint v : src) {
			internalAssembleKpTree(dest, v);
		}

		return dest;
	}

	private void internalAssembleKpTree(List<VDiagnosticClassKnowpoint> dest, VDiagnosticClassKnowpoint v) {
		if (v.getParentCode() <= 0) {
			dest.add(v);
		} else {
			for (VDiagnosticClassKnowpoint pc : dest) {
				if (pc.getKnowledgeCode() == v.getParentCode()) {
					pc.getChildren().add(v);
					break;
				} else {
					this.internalAssembleKpTree(pc.getChildren(), v);
				}
			}
		}
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VDiagnosticClassKnowpoint, DiagnosticClassKnowpoint, Long, KnowledgePoint>() {
					@Override
					public boolean accept(DiagnosticClassKnowpoint diagnosticClassKnowpoint) {
						return diagnosticClassKnowpoint.getKnowpointCode() > 1000000000;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(DiagnosticClassKnowpoint diagnosticClassKnowpoint,
							VDiagnosticClassKnowpoint vDiagnosticClassKnowpoint) {
						return diagnosticClassKnowpoint.getKnowpointCode();
					}

					@Override
					public void setValue(DiagnosticClassKnowpoint diagnosticClassKnowpoint,
							VDiagnosticClassKnowpoint vDiagnosticClassKnowpoint, KnowledgePoint value) {
						vDiagnosticClassKnowpoint.setKnowpointName(value.getName());
						vDiagnosticClassKnowpoint.setParentCode(value.getPcode());
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

		assemblers
				.add(new ConverterAssembler<VDiagnosticClassKnowpoint, DiagnosticClassKnowpoint, Long, KnowledgeSystem>() {

					@Override
					public boolean accept(DiagnosticClassKnowpoint diagnosticClassKnowpoint) {
						return diagnosticClassKnowpoint.getKnowpointCode() < 1000000000;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(DiagnosticClassKnowpoint diagnosticClassKnowpoint,
							VDiagnosticClassKnowpoint vDiagnosticClassKnowpoint) {
						return vDiagnosticClassKnowpoint.getKnowledgeCode();
					}

					@Override
					public void setValue(DiagnosticClassKnowpoint diagnosticClassKnowpoint,
							VDiagnosticClassKnowpoint vDiagnosticClassKnowpoint, KnowledgeSystem value) {
						vDiagnosticClassKnowpoint.setKnowpointName(value.getName());
						vDiagnosticClassKnowpoint.setParentCode(value.getPcode());
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
	}
}
