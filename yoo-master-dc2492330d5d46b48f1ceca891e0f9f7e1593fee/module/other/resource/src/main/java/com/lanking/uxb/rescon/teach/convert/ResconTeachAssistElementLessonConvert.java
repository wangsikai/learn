package com.lanking.uxb.rescon.teach.convert;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLesson;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLessonPoint;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementLesson;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementLessonPoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementLesson -> VTeachAssistElementLesson
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementLessonConvert extends
		Converter<VTeachAssistElementLesson, TeachAssistElementLesson, Long> {
	@Autowired
	private ResconTeachAssistElementService elementService;
	@Autowired
	private ResconTeachAssistElementLessonPointConvert pointConvert;

	@Override
	protected Long getId(TeachAssistElementLesson teachAssistElementLesson) {
		return teachAssistElementLesson.getId();
	}

	@Override
	protected VTeachAssistElementLesson convert(TeachAssistElementLesson teachAssistElementLesson) {
		VTeachAssistElementLesson v = new VTeachAssistElementLesson();
		v.setId(teachAssistElementLesson.getId());
		v.setSequence(teachAssistElementLesson.getSequence());
		v.setType(teachAssistElementLesson.getType());

		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementLesson, TeachAssistElementLesson, Long, List<VTeachAssistElementLessonPoint>>() {

					@Override
					public boolean accept(TeachAssistElementLesson teachAssistElementLesson) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementLesson teachAssistElementLesson,
							VTeachAssistElementLesson vTeachAssistElementLesson) {
						return teachAssistElementLesson.getId();
					}

					@Override
					public void setValue(TeachAssistElementLesson teachAssistElementLesson,
							VTeachAssistElementLesson vTeachAssistElementLesson,
							List<VTeachAssistElementLessonPoint> value) {
						vTeachAssistElementLesson.setPoints(value);
					}

					@Override
					public List<VTeachAssistElementLessonPoint> getValue(Long key) {
						List<TeachAssistElementLessonPoint> points = (List<TeachAssistElementLessonPoint>) elementService
								.getContents(key, TeachAssistElementType.LESSON_TEACH);

						return pointConvert.to(points);
					}

					@Override
					public Map<Long, List<VTeachAssistElementLessonPoint>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}

						List<TeachAssistElementLessonPoint> points = (List<TeachAssistElementLessonPoint>) elementService
								.getContents(keys, TeachAssistElementType.LESSON_TEACH);
						Map<Long, List<VTeachAssistElementLessonPoint>> retMap = new HashMap<Long, List<VTeachAssistElementLessonPoint>>();
						List<VTeachAssistElementLessonPoint> vs = pointConvert.to(points);
						for (VTeachAssistElementLessonPoint v : vs) {
							List<VTeachAssistElementLessonPoint> lessonPoints = retMap.get(v.getLessonId());
							if (CollectionUtils.isEmpty(lessonPoints)) {
								lessonPoints = Lists.newArrayList();
							}

							lessonPoints.add(v);

							retMap.put(v.getLessonId(), lessonPoints);
						}
						return retMap;
					}
				});

	}
}
