package com.lanking.uxb.service.code.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Component
public class KnowledgePointConvert extends Converter<VKnowledgePoint, KnowledgePoint, Long> {
	@Autowired
	private KnowledgePointService kpService;
	@Autowired
	private ParameterService parameterService;

	@Override
	protected Long getId(KnowledgePoint knowledgePoint) {
		return knowledgePoint.getCode();
	}

	@Override
	protected VKnowledgePoint convert(KnowledgePoint knowledgePoint) {
		VKnowledgePoint v = new VKnowledgePoint();
		v.setCode(knowledgePoint.getCode());
		v.setPcode(knowledgePoint.getPcode());
		v.setDifficulty(knowledgePoint.getDifficulty());
		v.setFocalDifficult(knowledgePoint.getFocalDifficult());
		v.setName(knowledgePoint.getName());
		v.setPhaseCode(knowledgePoint.getPhaseCode());
		v.setSequence(knowledgePoint.getSequence());
		v.setStatus(knowledgePoint.getStatus());
		v.setStudyDifficulty(knowledgePoint.getStudyDifficulty());
		v.setSubjectCode(knowledgePoint.getSubjectCode());
		return v;
	}

	@Override
	protected KnowledgePoint internalGet(Long id) {
		return kpService.get(id);
	}

	@Override
	protected Map<Long, KnowledgePoint> internalMGet(Collection<Long> ids) {
		return kpService.mget(ids);
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VKnowledgePoint, KnowledgePoint, Long, String>() {
			@Override
			public boolean accept(KnowledgePoint knowledgePoint) {
				return Security.isClient();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(KnowledgePoint knowledgePoint, VKnowledgePoint vKnowledgePoint) {
				return knowledgePoint.getCode();
			}

			@Override
			public void setValue(KnowledgePoint knowledgePoint, VKnowledgePoint vKnowledgePoint, String value) {
				vKnowledgePoint.setH5PageUrl(value);
			}

			@Override
			public String getValue(Long key) {
				Parameter parameter = parameterService.get(Product.YOOMATH, "h5.knowledge-card.url",
						String.valueOf(key));
				return parameter == null ? null : parameter.getValue();
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				Map<Long, String> retMap = new HashMap<Long, String>(keys.size());
				List<String[]> argsList = new ArrayList<String[]>(keys.size());
				for (Long key : keys) {
					argsList.add(new String[]{String.valueOf(key)});
				}

				List<String> list = parameterService.mgetValueList(Product.YOOMATH, "h5.knowledge-card.url", argsList);
				List<Long> keyList = Lists.newArrayList(keys);
				for (int i = 0; i < keyList.size(); i++) {
					retMap.put(keyList.get(i), list == null ? null : list.get(i));
				}
				return retMap;
			}
		});
	}
}
