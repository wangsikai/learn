package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPointStructure;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgeSystemConvert;
import com.lanking.uxb.rescon.basedata.value.VKnowledgeSystem;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPointStructure;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementPointStructure -> VTeachAssistElementPointStructure
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPointStructureConvert extends
		Converter<VTeachAssistElementPointStructure, TeachAssistElementPointStructure, Long> {
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private ResconKnowledgeSystemConvert knowledgeSystemConvert;

	@Override
	protected Long getId(TeachAssistElementPointStructure teachAssistElementPointStructure) {
		return teachAssistElementPointStructure.getId();
	}

	@Override
	protected VTeachAssistElementPointStructure convert(
			TeachAssistElementPointStructure teachAssistElementPointStructure) {
		VTeachAssistElementPointStructure v = new VTeachAssistElementPointStructure();
		v.setId(teachAssistElementPointStructure.getId());
		v.setSequence(teachAssistElementPointStructure.getSequence());
		v.setType(teachAssistElementPointStructure.getType());

		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementPointStructure, TeachAssistElementPointStructure, Long, VKnowledgeSystem>() {

					@Override
					public boolean accept(TeachAssistElementPointStructure teachAssistElementPointStructure) {
						return teachAssistElementPointStructure.getKnowpointSystem() != null
								&& teachAssistElementPointStructure.getKnowpointSystem() > 0;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementPointStructure teachAssistElementPointStructure,
							VTeachAssistElementPointStructure vTeachAssistElementPointStructure) {
						return teachAssistElementPointStructure.getKnowpointSystem();
					}

					@Override
					public void setValue(TeachAssistElementPointStructure teachAssistElementPointStructure,
							VTeachAssistElementPointStructure vTeachAssistElementPointStructure, VKnowledgeSystem value) {
						vTeachAssistElementPointStructure.setKnowledgeSystem(value);
					}

					@Override
					public VKnowledgeSystem getValue(Long key) {
						return knowledgeSystemConvert.to(knowledgeSystemService.get(key));
					}

					@Override
					public Map<Long, VKnowledgeSystem> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						return knowledgeSystemConvert.to(knowledgeSystemService.mget(keys));
					}
				});
	}
}
