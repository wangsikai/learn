package com.lanking.uxb.rescon.teach.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFalliblePoint;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFalliblePointContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementFalliblePoint;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementFalliblePointContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementFalliblePoint - > VTeachAssistElementFalliblePoint
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementFalliblePointConvert extends
		Converter<VTeachAssistElementFalliblePoint, TeachAssistElementFalliblePoint, Long> {
	@Autowired
	private ResconTeachAssistElementService elementService;
	@Autowired
	private ResconTeachAssistElementFalliblePointContentConvert contentConvert;

	@Override
	protected Long getId(TeachAssistElementFalliblePoint teachAssistElementFalliblePoint) {
		return teachAssistElementFalliblePoint.getId();
	}

	@Override
	protected VTeachAssistElementFalliblePoint convert(TeachAssistElementFalliblePoint teachAssistElementFalliblePoint) {
		VTeachAssistElementFalliblePoint v = new VTeachAssistElementFalliblePoint();
		v.setId(teachAssistElementFalliblePoint.getId());
		v.setSequence(teachAssistElementFalliblePoint.getSequence());
		v.setType(teachAssistElementFalliblePoint.getType());
		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementFalliblePoint, TeachAssistElementFalliblePoint, Long, List<VTeachAssistElementFalliblePointContent>>() {

					@Override
					public boolean accept(TeachAssistElementFalliblePoint teachAssistElementFalliblePoint) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementFalliblePoint teachAssistElementFalliblePoint,
							VTeachAssistElementFalliblePoint vTeachAssistElementFalliblePoint) {
						return teachAssistElementFalliblePoint.getId();
					}

					@Override
					public void setValue(TeachAssistElementFalliblePoint teachAssistElementFalliblePoint,
							VTeachAssistElementFalliblePoint vTeachAssistElementFalliblePoint,
							List<VTeachAssistElementFalliblePointContent> value) {
						if (CollectionUtils.isNotEmpty(value)) {
							vTeachAssistElementFalliblePoint.setContents(value);
						}
					}

					@Override
					public List<VTeachAssistElementFalliblePointContent> getValue(Long key) {
						List<TeachAssistElementFalliblePointContent> contents = (List<TeachAssistElementFalliblePointContent>) elementService
								.getContents(key, TeachAssistElementType.FALLIBLE_POINT);
						return contentConvert.to(contents);
					}

					@Override
					public Map<Long, List<VTeachAssistElementFalliblePointContent>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						List<TeachAssistElementFalliblePointContent> contents = elementService.getContents(keys,
								TeachAssistElementType.FALLIBLE_POINT);

						List<VTeachAssistElementFalliblePointContent> vs = contentConvert.to(contents);
						Map<Long, List<VTeachAssistElementFalliblePointContent>> retMap = Maps.newHashMap();
						for (VTeachAssistElementFalliblePointContent v : vs) {
							List<VTeachAssistElementFalliblePointContent> pointContents = retMap.get(v.getFallpointId());
							if (CollectionUtils.isEmpty(pointContents)) {
								pointContents = Lists.newArrayList();
							}

							pointContents.add(v);

							retMap.put(v.getFallpointId(), pointContents);
						}
						return retMap;
					}
				});
	}
}
