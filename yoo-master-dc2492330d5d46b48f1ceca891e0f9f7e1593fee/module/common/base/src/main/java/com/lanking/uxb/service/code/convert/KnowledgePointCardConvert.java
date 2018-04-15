package com.lanking.uxb.service.code.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.value.VKnowledgePointCard;
import com.lanking.uxb.service.session.api.impl.Security;

@Component
public class KnowledgePointCardConvert extends Converter<VKnowledgePointCard, KnowledgePointCard, Long> {
	@Autowired
	private ParameterService parameterService;

	@Override
	protected Long getId(KnowledgePointCard s) {
		return s.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected VKnowledgePointCard convert(KnowledgePointCard s) {
		VKnowledgePointCard v = new VKnowledgePointCard();
		v.setId(s.getId());
		v.setKnowpointCode(s.getKnowpointCode());
		v.setName(s.getName());
		v.setDescription(s.getDescription());
		v.setDetailDescription(s.getDetailDescription());
		JSONArray jsonArray = JSONArray.parseArray(s.getHints());
		int hitsSize = 0;
		if (jsonArray != null && (hitsSize = jsonArray.size()) > 0) {
			v.setHints(new ArrayList<String>(hitsSize));
			for (Object object : jsonArray) {
				v.getHints().add(JSONObject.parseObject(object.toString()).getString("content"));
			}
		} else {
			v.setHints(Collections.EMPTY_LIST);
		}
		v.setQuestionIds(s.getQuestions());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VKnowledgePointCard, KnowledgePointCard, Long, String>() {
			@Override
			public boolean accept(KnowledgePointCard knowledgePoint) {
				return Security.isClient();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(KnowledgePointCard knowledgePoint, VKnowledgePointCard v) {
				return knowledgePoint.getId();
			}

			@Override
			public void setValue(KnowledgePointCard knowledgePoint, VKnowledgePointCard v, String value) {
				v.setH5PageUrl(value);
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
				for (Long key : keys) {
					Parameter parameter = parameterService.get(Product.YOOMATH, "h5.knowledge-card.url",
							String.valueOf(key));
					retMap.put(key, parameter == null ? null : parameter.getValue());
				}
				return retMap;
			}
		});
	}
}
