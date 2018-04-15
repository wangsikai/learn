package com.lanking.uxb.zycon.qs.convert;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.base.api.ZycQuestionService;
import com.lanking.uxb.zycon.base.convert.ZycQuestionConvert;
import com.lanking.uxb.zycon.base.value.CQuestion;
import com.lanking.uxb.zycon.qs.value.VZycSchoolQuestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Component
public class ZycSchoolQuestionConverter extends Converter<VZycSchoolQuestion, SchoolQuestion, Long> {
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycQuestionConvert questionConvert;

	@Override
	protected Long getId(SchoolQuestion schoolQuestion) {
		return schoolQuestion.getId();
	}

	@Override
	protected VZycSchoolQuestion convert(SchoolQuestion schoolQuestion) {
		VZycSchoolQuestion v = new VZycSchoolQuestion();
		v.setCreateAt(schoolQuestion.getCreateAt());
		v.setId(schoolQuestion.getId());
		v.setDifficulty(schoolQuestion.getDifficulty());
		v.setSchoolId(schoolQuestion.getSchoolId());
		v.setQuestionId(schoolQuestion.getQuestionId());
		v.setSubjectCode(schoolQuestion.getSubjectCode());
		v.setType(schoolQuestion.getType());
		v.setTypeCode(schoolQuestion.getTypeCode());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycSchoolQuestion, SchoolQuestion, Long, CQuestion>() {

			@Override
			public boolean accept(SchoolQuestion schoolQuestion) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(SchoolQuestion schoolQuestion, VZycSchoolQuestion vZycSchoolQuestion) {
				return schoolQuestion.getQuestionId();
			}

			@Override
			public void setValue(SchoolQuestion schoolQuestion, VZycSchoolQuestion vZycSchoolQuestion, CQuestion value) {
				if (value != null) {
					vZycSchoolQuestion.setQuestion(value);
				}
			}

			@Override
			public CQuestion getValue(Long key) {
				if (null == key)
					return null;

				return questionConvert.to(questionService.get(key), true, true, false, null);
			}

			@Override
			public Map<Long, CQuestion> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();
				List<CQuestion> cs = questionConvert.to(questionService.mgetList(keys), true, true, false, null);
				Map<Long, CQuestion> map = Maps.newHashMap();
				for (CQuestion c : cs) {
					map.put(c.getId(), c);
				}
				return map;
			}
		});
	}
}
