package com.lanking.uxb.rescon.teach.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFreeEdit;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFreeEditContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementFreeEdit;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementFreeEditContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementFreeEdit -> VTeachAssistElementFreeEdit
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementFreeEditConvert extends
		Converter<VTeachAssistElementFreeEdit, TeachAssistElementFreeEdit, Long> {
	@Autowired
	private ResconTeachAssistElementService elementService;
	@Autowired
	private ResconTeachAssistElementFreeEditContentConvert contentConvert;

	@Override
	protected Long getId(TeachAssistElementFreeEdit teachAssistElementFreeEdit) {
		return teachAssistElementFreeEdit.getId();
	}

	@Override
	protected VTeachAssistElementFreeEdit convert(TeachAssistElementFreeEdit teachAssistElementFreeEdit) {
		VTeachAssistElementFreeEdit v = new VTeachAssistElementFreeEdit();
		v.setId(teachAssistElementFreeEdit.getId());
		v.setSequence(teachAssistElementFreeEdit.getSequence());
		v.setType(teachAssistElementFreeEdit.getType());
		v.setName(teachAssistElementFreeEdit.getName());

		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementFreeEdit, TeachAssistElementFreeEdit, Long, List<VTeachAssistElementFreeEditContent>>() {

					@Override
					public boolean accept(TeachAssistElementFreeEdit teachAssistElementFreeEdit) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementFreeEdit teachAssistElementFreeEdit,
							VTeachAssistElementFreeEdit vTeachAssistElementFreeEdit) {
						return teachAssistElementFreeEdit.getId();
					}

					@Override
					public void setValue(TeachAssistElementFreeEdit teachAssistElementFreeEdit,
							VTeachAssistElementFreeEdit vTeachAssistElementFreeEdit,
							List<VTeachAssistElementFreeEditContent> value) {
						vTeachAssistElementFreeEdit.setContents(value);
					}

					@Override
					public List<VTeachAssistElementFreeEditContent> getValue(Long key) {
						List<TeachAssistElementFreeEditContent> contents = (List<TeachAssistElementFreeEditContent>) elementService
								.getContents(key, TeachAssistElementType.FREE_EDIT);
						return contentConvert.to(contents);
					}

					@Override
					public Map<Long, List<VTeachAssistElementFreeEditContent>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						List<TeachAssistElementFreeEditContent> contents = (List<TeachAssistElementFreeEditContent>) elementService
								.getContents(keys, TeachAssistElementType.FREE_EDIT);
						List<VTeachAssistElementFreeEditContent> vs = contentConvert.to(contents);

						Map<Long, List<VTeachAssistElementFreeEditContent>> retMap = Maps.newHashMap();
						for (VTeachAssistElementFreeEditContent v : vs) {
							List<VTeachAssistElementFreeEditContent> editContents = retMap.get(v.getFreeEditId());
							if (CollectionUtils.isEmpty(editContents)) {
								editContents = Lists.newArrayList();
							}

							retMap.put(v.getFreeEditId(), editContents);
						}
						return retMap;
					}
				});
	}
}
