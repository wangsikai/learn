package com.lanking.uxb.rescon.teach.convert;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopic;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopicContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementTopic;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementTopicContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementTopic -> VTeachAssistElementTopic
 * 
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementTopicConvert extends
		Converter<VTeachAssistElementTopic, TeachAssistElementTopic, Long> {

	@Autowired
	private ResconTeachAssistElementService elementService;
	@Autowired
	private ResconTeachAssistElementTopicContentConvert contentConvert;

	@Override
	protected Long getId(TeachAssistElementTopic teachAssistElementTopic) {
		return teachAssistElementTopic.getId();
	}

	@Override
	protected VTeachAssistElementTopic convert(TeachAssistElementTopic teachAssistElementTopic) {
		VTeachAssistElementTopic v = new VTeachAssistElementTopic();
		v.setId(teachAssistElementTopic.getId());
		v.setTopicType(teachAssistElementTopic.getTopicType());
		v.setType(teachAssistElementTopic.getType());
		v.setSequence(teachAssistElementTopic.getSequence());
		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementTopic, TeachAssistElementTopic, Long, List<VTeachAssistElementTopicContent>>() {

					@Override
					public boolean accept(TeachAssistElementTopic teachAssistElementTopic) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementTopic teachAssistElementTopic,
							VTeachAssistElementTopic vTeachAssistElementTopic) {
						return teachAssistElementTopic.getId();
					}

					@Override
					public void setValue(TeachAssistElementTopic teachAssistElementTopic,
							VTeachAssistElementTopic vTeachAssistElementTopic,
							List<VTeachAssistElementTopicContent> value) {
						vTeachAssistElementTopic.setContents(value);
					}

					@Override
					public List<VTeachAssistElementTopicContent> getValue(Long key) {
						List<TeachAssistElementTopicContent> contents = (List<TeachAssistElementTopicContent>) elementService
								.getContents(key, TeachAssistElementType.TOPIC);
						return contentConvert.to(contents);
					}

					@Override
					public Map<Long, List<VTeachAssistElementTopicContent>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						List<TeachAssistElementTopicContent> contents = (List<TeachAssistElementTopicContent>) elementService
								.getContents(keys, TeachAssistElementType.TOPIC);
						List<VTeachAssistElementTopicContent> vs = contentConvert.to(contents);
						Map<Long, List<VTeachAssistElementTopicContent>> retMap = new HashMap<Long, List<VTeachAssistElementTopicContent>>();
						for (VTeachAssistElementTopicContent v : vs) {
							List<VTeachAssistElementTopicContent> topicContents = retMap.get(v.getTopicId());
							if (CollectionUtils.isEmpty(topicContents)) {
								topicContents = Lists.newArrayList();
							}

							topicContents.add(v);
							retMap.put(v.getTopicId(), topicContents);
						}
						return retMap;
					}
				});
	}
}
