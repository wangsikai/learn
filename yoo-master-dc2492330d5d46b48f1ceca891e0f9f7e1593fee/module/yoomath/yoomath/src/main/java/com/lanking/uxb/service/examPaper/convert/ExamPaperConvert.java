package com.lanking.uxb.service.examPaper.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.examPaper.api.ExamPaperQuestionService;
import com.lanking.uxb.service.examPaper.value.VExamPaper;

/**
 * 中央资源库试卷Convert
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Component
public class ExamPaperConvert extends Converter<VExamPaper, ExamPaper, Long> {
	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;
	@Autowired
	private SchoolConvert schoolConvert;

	@Override
	protected Long getId(ExamPaper examPaper) {
		return examPaper.getId();
	}

	@Override
	protected VExamPaper convert(ExamPaper examPaper) {
		VExamPaper v = new VExamPaper();
		v.setId(examPaper.getId());
		v.setDifficulty(examPaper.getDifficulty());
		v.setName(examPaper.getName());
		v.setScore(examPaper.getScore());
		v.setYear(examPaper.getYear());
		v.setCreateAt(examPaper.getCreateAt());
		v.setPhaseCode(examPaper.getPhaseCode());
		v.setCategoryCode(examPaper.getResourceCategoryCode());

		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VExamPaper, ExamPaper, Long, Integer>() {

			@Override
			public boolean accept(ExamPaper examPaper) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper examPaper, VExamPaper vExamPaper) {
				return examPaper.getId();
			}

			@Override
			public void setValue(ExamPaper examPaper, VExamPaper vExamPaper, Integer value) {
				vExamPaper.setQuestionCount(value);
			}

			@Override
			public Integer getValue(Long key) {
				List<Long> keys = new ArrayList<Long>(1);
				keys.add(key);
				return examPaperQuestionService.getExampaperQuestionCount(keys).get(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, Integer> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return examPaperQuestionService.getExampaperQuestionCount(keys);
			}
		});

		assemblers.add(new ConverterAssembler<VExamPaper, ExamPaper, Long, VSchool>() {

			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExamPaper d) {
				return s.getSchoolId();
			}

			@Override
			public void setValue(ExamPaper s, VExamPaper d, VSchool value) {
				if (value != null) {
					d.setSchoolName(value.getName());
				}

			}

			@Override
			public VSchool getValue(Long key) {
				if (key != null) {
					return schoolConvert.get(key);
				}
				return null;
			}

			@Override
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				return schoolConvert.mget(keys);
			}

		});

	}
}
