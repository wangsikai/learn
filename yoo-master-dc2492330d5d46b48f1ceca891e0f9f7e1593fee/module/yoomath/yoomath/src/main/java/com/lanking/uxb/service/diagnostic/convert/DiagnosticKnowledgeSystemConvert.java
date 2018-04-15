package com.lanking.uxb.service.diagnostic.convert;

import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticKnowledgeSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 只为知识图谱相关点进行转换的,其他地方请不要使用
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Component
public class DiagnosticKnowledgeSystemConvert extends Converter<VDiagnosticKnowledgeSystem, KnowledgeSystem, Long> {
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;

	@Override
	protected Long getId(KnowledgeSystem knowledgeSystem) {
		return knowledgeSystem.getCode();
	}

	@Override
	protected VDiagnosticKnowledgeSystem convert(KnowledgeSystem knowledgeSystem) {
		VDiagnosticKnowledgeSystem v = new VDiagnosticKnowledgeSystem();
		v.setCode(knowledgeSystem.getCode());
		v.setName(knowledgeSystem.getName());
		v.setPcode(knowledgeSystem.getPcode());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 得到小专题数据
		assemblers.add(new ConverterAssembler<VDiagnosticKnowledgeSystem, KnowledgeSystem, Long, KnowledgeSystem>() {

			@Override
			public boolean accept(KnowledgeSystem knowledgeSystem) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(KnowledgeSystem knowledgeSystem, VDiagnosticKnowledgeSystem vDiagnosticKnowledgeSystem) {
				return knowledgeSystem.getPcode();
			}

			@Override
			public void setValue(KnowledgeSystem knowledgeSystem,
					VDiagnosticKnowledgeSystem vDiagnosticKnowledgeSystem, KnowledgeSystem value) {
				vDiagnosticKnowledgeSystem.setLevel2Name(value.getName());
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

		// 得到大专题数据
		assemblers.add(new ConverterAssembler<VDiagnosticKnowledgeSystem, KnowledgeSystem, Long, KnowledgeSystem>() {

			@Override
			public boolean accept(KnowledgeSystem knowledgeSystem) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(KnowledgeSystem knowledgeSystem, VDiagnosticKnowledgeSystem vDiagnosticKnowledgeSystem) {
				String code = knowledgeSystem.getPcode().toString();
				String first = code.substring(0, code.length() - 2);
				return Long.valueOf(first);
			}

			@Override
			public void setValue(KnowledgeSystem knowledgeSystem,
					VDiagnosticKnowledgeSystem vDiagnosticKnowledgeSystem, KnowledgeSystem value) {
				vDiagnosticKnowledgeSystem.setLevel1Name(value.getName());
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
