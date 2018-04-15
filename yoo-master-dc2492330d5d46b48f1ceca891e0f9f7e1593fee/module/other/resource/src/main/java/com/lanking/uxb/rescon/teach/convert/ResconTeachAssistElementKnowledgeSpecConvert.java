package com.lanking.uxb.rescon.teach.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementKnowledgeSpec;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementKnowledgeSpecKp;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementKnowledgeSpec;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementKnowledgeSpecKp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementKnowledgeSpec -> VTeachAssistElementKnowledgeSpec
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementKnowledgeSpecConvert extends
		Converter<VTeachAssistElementKnowledgeSpec, TeachAssistElementKnowledgeSpec, Long> {

	@Autowired
	private ResconTeachAssistElementService elementService;
	@Autowired
	private ResconTeachAssistElementKnowledgeSpecKpConvert kpConvert;

	@Override
	protected Long getId(TeachAssistElementKnowledgeSpec teachAssistElementKnowledgeSpec) {
		return teachAssistElementKnowledgeSpec.getId();
	}

	@Override
	protected VTeachAssistElementKnowledgeSpec convert(TeachAssistElementKnowledgeSpec teachAssistElementKnowledgeSpec) {
		VTeachAssistElementKnowledgeSpec v = new VTeachAssistElementKnowledgeSpec();
		v.setReview(teachAssistElementKnowledgeSpec.isReview());
		v.setId(teachAssistElementKnowledgeSpec.getId());
		v.setSequence(teachAssistElementKnowledgeSpec.getSequence());
		v.setType(teachAssistElementKnowledgeSpec.getType());
		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementKnowledgeSpec, TeachAssistElementKnowledgeSpec, Long, List<VTeachAssistElementKnowledgeSpecKp>>() {

					@Override
					public boolean accept(TeachAssistElementKnowledgeSpec teachAssistElementKnowledgeSpec) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementKnowledgeSpec teachAssistElementKnowledgeSpec,
							VTeachAssistElementKnowledgeSpec vTeachAssistElementKnowledgeSpec) {
						return teachAssistElementKnowledgeSpec.getId();
					}

					@Override
					public void setValue(TeachAssistElementKnowledgeSpec teachAssistElementKnowledgeSpec,
							VTeachAssistElementKnowledgeSpec vTeachAssistElementKnowledgeSpec,
							List<VTeachAssistElementKnowledgeSpecKp> value) {
						vTeachAssistElementKnowledgeSpec.setKps(value);
					}

					@Override
					public List<VTeachAssistElementKnowledgeSpecKp> getValue(Long key) {
						List<TeachAssistElementKnowledgeSpecKp> kps = (List<TeachAssistElementKnowledgeSpecKp>) elementService
								.getContents(key, TeachAssistElementType.KNOWLEDGE_SPEC);

						return kpConvert.to(kps);
					}

					@Override
					public Map<Long, List<VTeachAssistElementKnowledgeSpecKp>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						List<TeachAssistElementKnowledgeSpecKp> kps = (List<TeachAssistElementKnowledgeSpecKp>) elementService
								.getContents(keys, TeachAssistElementType.KNOWLEDGE_SPEC);
						List<VTeachAssistElementKnowledgeSpecKp> vs = kpConvert.to(kps);
						Map<Long, List<VTeachAssistElementKnowledgeSpecKp>> retMap = Maps.newHashMap();
						for (VTeachAssistElementKnowledgeSpecKp v : vs) {
							List<VTeachAssistElementKnowledgeSpecKp> specKps = retMap.get(v.getKnowledgeSpecId());
							if (CollectionUtils.isEmpty(specKps)) {
								specKps = Lists.newArrayList();
							}
							specKps.add(v);

							retMap.put(v.getKnowledgeSpecId(), specKps);
						}
						return retMap;
					}
				});
	}
}
