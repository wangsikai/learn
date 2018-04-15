package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementKnowledgeSpecKp;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementKnowledgeSpecKp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementKnowledgeSpecKp -> VTeachAssistElementKnowledgeKp
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementKnowledgeSpecKpConvert extends
		Converter<VTeachAssistElementKnowledgeSpecKp, TeachAssistElementKnowledgeSpecKp, Long> {

	@Autowired
	private ResconKnowledgePointService knowledgePointService;
	@Autowired
	private ResconKnowledgePointConvert knowledgePointConvert;

	@Override
	protected Long getId(TeachAssistElementKnowledgeSpecKp teachAssistElementKnowledgeSpecKp) {
		return null;
	}

	@Override
	protected VTeachAssistElementKnowledgeSpecKp convert(
			TeachAssistElementKnowledgeSpecKp teachAssistElementKnowledgeSpecKp) {
		VTeachAssistElementKnowledgeSpecKp v = new VTeachAssistElementKnowledgeSpecKp();
		v.setContent(teachAssistElementKnowledgeSpecKp.getContent());
		v.setId(teachAssistElementKnowledgeSpecKp.getId());
		v.setKnowledgeSpecId(teachAssistElementKnowledgeSpecKp.getKnowledgeSpecId());
		v.setSequence(teachAssistElementKnowledgeSpecKp.getSequence());
		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementKnowledgeSpecKp, TeachAssistElementKnowledgeSpecKp, Long, VKnowledgePoint>() {

					@Override
					public boolean accept(TeachAssistElementKnowledgeSpecKp teachAssistElementKnowledgeSpecKp) {
						return teachAssistElementKnowledgeSpecKp.getKnowledgeCode() != null
								&& teachAssistElementKnowledgeSpecKp.getKnowledgeCode() > 0;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementKnowledgeSpecKp teachAssistElementKnowledgeSpecKp,
							VTeachAssistElementKnowledgeSpecKp vTeachAssistElementKnowledgeSpecKp) {
						return teachAssistElementKnowledgeSpecKp.getKnowledgeCode();
					}

					@Override
					public void setValue(TeachAssistElementKnowledgeSpecKp teachAssistElementKnowledgeSpecKp,
							VTeachAssistElementKnowledgeSpecKp vTeachAssistElementKnowledgeSpecKp, VKnowledgePoint value) {
						vTeachAssistElementKnowledgeSpecKp.setKnowledgePoint(value);
					}

					@Override
					public VKnowledgePoint getValue(Long key) {
						return knowledgePointConvert.to(knowledgePointService.get(key));
					}

					@Override
					public Map<Long, VKnowledgePoint> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						return knowledgePointConvert.to(knowledgePointService.mget(keys));
					}
				});
	}
}
