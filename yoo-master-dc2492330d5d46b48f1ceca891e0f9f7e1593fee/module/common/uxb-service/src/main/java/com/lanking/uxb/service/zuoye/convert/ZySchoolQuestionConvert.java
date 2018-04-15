package com.lanking.uxb.service.zuoye.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.zuoye.value.VSchoolQuestion;

@Component
public class ZySchoolQuestionConvert extends Converter<VSchoolQuestion, SchoolQuestion, Long> {

	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private QuestionService questionService;

	@Override
	protected Long getId(SchoolQuestion s) {
		return s.getId();
	}

	@Override
	protected VSchoolQuestion convert(SchoolQuestion s) {
		VSchoolQuestion vq = new VSchoolQuestion();
		vq.setSchoolQuestionId(s.getId());
		return vq;
	}

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VSchoolQuestion, SchoolQuestion, Long, VQuestion>() {

			@Override
			public boolean accept(SchoolQuestion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(SchoolQuestion s, VSchoolQuestion d) {
				return s.getQuestionId();
			}

			@Override
			public void setValue(SchoolQuestion s, VSchoolQuestion d, VQuestion value) {
				d.setvQuestion(value);

			}

			@Override
			public VQuestion getValue(Long key) {
				QuestionConvertOption qco = new QuestionConvertOption();
				qco.setAnalysis(true);
				qco.setAnswer(true);
				qco.setCollect(true);
				qco.setInitSub(true);
				qco.setInitExamination(true);
				return questionConvert.to(questionService.get(key), qco);
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				QuestionConvertOption qco = new QuestionConvertOption();
				qco.setAnalysis(true);
				qco.setAnswer(true);
				qco.setCollect(true);
				qco.setInitSub(true);
				qco.setInitExamination(true);
				return questionConvert.to(questionService.mget(keys), qco);
			}

		});
	}
}
