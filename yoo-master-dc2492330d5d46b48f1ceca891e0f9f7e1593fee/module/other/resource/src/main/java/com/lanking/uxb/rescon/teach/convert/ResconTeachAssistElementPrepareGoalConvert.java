package com.lanking.uxb.rescon.teach.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareGoal;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareGoalContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPrepareGoal;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPrepareGoalContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementPrepareGoal -> VTeachAssistElementPrepareGoal
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPrepareGoalConvert extends
		Converter<VTeachAssistElementPrepareGoal, TeachAssistElementPrepareGoal, Long> {

	@Autowired
	private ResconTeachAssistElementService elementService;
	@Autowired
	private ResconTeachAssistElementPrepareGoalContentConvert contentConvert;

	@Override
	protected Long getId(TeachAssistElementPrepareGoal teachAssistElementPrepareGoal) {
		return teachAssistElementPrepareGoal.getId();
	}

	@Override
	protected VTeachAssistElementPrepareGoal convert(TeachAssistElementPrepareGoal teachAssistElementPrepareGoal) {
		VTeachAssistElementPrepareGoal v = new VTeachAssistElementPrepareGoal();
		v.setId(teachAssistElementPrepareGoal.getId());
		v.setSequence(teachAssistElementPrepareGoal.getSequence());
		v.setType(teachAssistElementPrepareGoal.getType());

		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementPrepareGoal, TeachAssistElementPrepareGoal, Long, List<VTeachAssistElementPrepareGoalContent>>() {

					@Override
					public boolean accept(TeachAssistElementPrepareGoal teachAssistElementPrepareGoal) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementPrepareGoal teachAssistElementPrepareGoal,
							VTeachAssistElementPrepareGoal vTeachAssistElementPrepareGoal) {
						return teachAssistElementPrepareGoal.getId();
					}

					@Override
					public void setValue(TeachAssistElementPrepareGoal teachAssistElementPrepareGoal,
							VTeachAssistElementPrepareGoal vTeachAssistElementPrepareGoal,
							List<VTeachAssistElementPrepareGoalContent> value) {
						vTeachAssistElementPrepareGoal.setContents(value);
					}

					@Override
					public List<VTeachAssistElementPrepareGoalContent> getValue(Long key) {
						List<TeachAssistElementPrepareGoalContent> contents = (List<TeachAssistElementPrepareGoalContent>) elementService
								.getContents(key, TeachAssistElementType.PREPARE_GOAL);

						return contentConvert.to(contents);
					}

					@Override
					public Map<Long, List<VTeachAssistElementPrepareGoalContent>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						List<TeachAssistElementPrepareGoalContent> contents = (List<TeachAssistElementPrepareGoalContent>) elementService
								.getContents(keys, TeachAssistElementType.PREPARE_GOAL);
						List<VTeachAssistElementPrepareGoalContent> vs = contentConvert.to(contents);
						Map<Long, List<VTeachAssistElementPrepareGoalContent>> retMap = Maps.newHashMap();
						for (VTeachAssistElementPrepareGoalContent v : vs) {
							List<VTeachAssistElementPrepareGoalContent> goalContents = retMap.get(v.getGoalId());
							if (CollectionUtils.isEmpty(goalContents)) {
								goalContents = Lists.newArrayList();
							}

							goalContents.add(v);

							retMap.put(v.getGoalId(), goalContents);
						}
						return retMap;
					}
				});
	}
}
