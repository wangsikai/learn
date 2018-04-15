package com.lanking.uxb.rescon.teach.convert;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPractice;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPracticeContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPractice;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPracticeContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementPractice -> VTeachAssistElementPractice
 * 
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPracticeConvert extends
		Converter<VTeachAssistElementPractice, TeachAssistElementPractice, Long> {
	@Autowired
	private ResconTeachAssistElementService elementService;
	@Autowired
	private ResconTeachAssistElementPracticeContentConvert contentConvert;

	@Override
	protected Long getId(TeachAssistElementPractice teachAssistElementPractice) {
		return teachAssistElementPractice.getId();
	}

	@Override
	protected VTeachAssistElementPractice convert(TeachAssistElementPractice teachAssistElementPractice) {
		VTeachAssistElementPractice v = new VTeachAssistElementPractice();
		v.setId(teachAssistElementPractice.getId());
		v.setSequence(teachAssistElementPractice.getSequence());
		v.setType(teachAssistElementPractice.getType());
		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementPractice, TeachAssistElementPractice, Long, List<VTeachAssistElementPracticeContent>>() {

					@Override
					public boolean accept(TeachAssistElementPractice teachAssistElementPractice) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementPractice teachAssistElementPractice,
							VTeachAssistElementPractice vTeachAssistElementPractice) {
						return teachAssistElementPractice.getId();
					}

					@Override
					public void setValue(TeachAssistElementPractice teachAssistElementPractice,
							VTeachAssistElementPractice vTeachAssistElementPractice,
							List<VTeachAssistElementPracticeContent> value) {
						vTeachAssistElementPractice.setContents(value);
					}

					@Override
					public List<VTeachAssistElementPracticeContent> getValue(Long key) {
						List<TeachAssistElementPracticeContent> contents = (List<TeachAssistElementPracticeContent>) elementService
								.getContents(key, TeachAssistElementType.PRACTICE);
						return contentConvert.to(contents);
					}

					@Override
					public Map<Long, List<VTeachAssistElementPracticeContent>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						List<TeachAssistElementPracticeContent> contents = (List<TeachAssistElementPracticeContent>) elementService
								.getContents(keys, TeachAssistElementType.PRACTICE);

						List<VTeachAssistElementPracticeContent> vs = contentConvert.to(contents);
						Map<Long, List<VTeachAssistElementPracticeContent>> retMap = new HashMap<Long, List<VTeachAssistElementPracticeContent>>();
						for (VTeachAssistElementPracticeContent v : vs) {
							List<VTeachAssistElementPracticeContent> practiceContents = retMap.get(v.getPracticeId());
							if (CollectionUtils.isEmpty(practiceContents)) {
								practiceContents = Lists.newArrayList();
							}

							practiceContents.add(v);
							retMap.put(v.getPracticeId(), practiceContents);
						}
						return retMap;
					}
				});
	}
}
