package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPointMap;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgeSystemConvert;
import com.lanking.uxb.rescon.basedata.value.VKnowledgeSystem;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPointMap;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementPointMap Convert
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPointMapConvert extends
		Converter<VTeachAssistElementPointMap, TeachAssistElementPointMap, Long> {
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private ResconKnowledgeSystemConvert knowledgeSystemConvert;

	@Override
	protected Long getId(TeachAssistElementPointMap teachAssistElementPointMap) {
		return teachAssistElementPointMap.getId();
	}

	@Override
	protected VTeachAssistElementPointMap convert(TeachAssistElementPointMap teachAssistElementPointMap) {
		VTeachAssistElementPointMap v = new VTeachAssistElementPointMap();
		v.setId(teachAssistElementPointMap.getId());
		v.setSequence(teachAssistElementPointMap.getSequence());
		v.setType(teachAssistElementPointMap.getType());
		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementPointMap, TeachAssistElementPointMap, Long, VKnowledgeSystem>() {

					@Override
					public boolean accept(TeachAssistElementPointMap teachAssistElementPointMap) {
						return teachAssistElementPointMap.getKnowpointSystem() != null
								&& teachAssistElementPointMap.getKnowpointSystem() > 0;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementPointMap teachAssistElementPointMap,
							VTeachAssistElementPointMap vTeachAssistElementPointMap) {
						return teachAssistElementPointMap.getKnowpointSystem();
					}

					@Override
					public void setValue(TeachAssistElementPointMap teachAssistElementPointMap,
							VTeachAssistElementPointMap vTeachAssistElementPointMap, VKnowledgeSystem value) {
						vTeachAssistElementPointMap.setKnowledgeSystem(value);
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
