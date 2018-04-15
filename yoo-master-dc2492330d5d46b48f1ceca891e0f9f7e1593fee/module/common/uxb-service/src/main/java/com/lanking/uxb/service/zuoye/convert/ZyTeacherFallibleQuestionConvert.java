package com.lanking.uxb.service.zuoye.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.value.VTeacherFallibleQuestion;

@Component
public class ZyTeacherFallibleQuestionConvert extends
		Converter<VTeacherFallibleQuestion, TeacherFallibleQuestion, Long> {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyQuestionCarService questionCarService;

	@Override
	protected Long getId(TeacherFallibleQuestion s) {
		return s.getId();
	}

	@Override
	protected VTeacherFallibleQuestion convert(TeacherFallibleQuestion s) {
		VTeacherFallibleQuestion v = new VTeacherFallibleQuestion();
		v.setCreateAt(s.getCreateAt());
		v.setDoNum(s.getDoNum());
		v.setId(s.getId());
		v.setQuestionId(s.getQuestionId());
		v.setRightNum(s.getRightNum());
		v.setRightRate(s.getRightRate());
		v.setUpdateAt(s.getUpdateAt());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VTeacherFallibleQuestion, TeacherFallibleQuestion, Long, VQuestion>() {

			@Override
			public boolean accept(TeacherFallibleQuestion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(TeacherFallibleQuestion s, VTeacherFallibleQuestion d) {
				return s.getQuestionId();
			}

			@Override
			public void setValue(TeacherFallibleQuestion s, VTeacherFallibleQuestion d, VQuestion value) {
				d.setQuestion(value);
			}

			@Override
			public VQuestion getValue(Long key) {
				// @since 教师端v1.3.0
				QuestionConvertOption option = new QuestionConvertOption(true, true, true, true,
						true, null);
				option.setInitPublishCount(true);
				option.setInitQuestionSimilarCount(true);
				option.setInitExamination(true);
				option.setInitQuestionTag(true);
				option.setInitQuestionSimilarCount(true);
				
				// 设置题目是否被加入作业篮子
				VQuestion vq = questionConvert.to(questionService.get(key), option);
				List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
				if (vq.getType() != Type.COMPOSITE) {
					vq.setInQuestionCar(carQuestions.contains(vq.getId()));
				}
				
				return vq;
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				// @since 教师端v1.3.0
				QuestionConvertOption option = new QuestionConvertOption(true, true, true, true,
						true, null);
				option.setInitPublishCount(true);
				option.setInitQuestionSimilarCount(true);
				option.setInitExamination(true);
				option.setInitQuestionTag(true);
				option.setInitQuestionSimilarCount(true);
				
				// 设置题目是否被加入作业篮子
				Map<Long, VQuestion> map = questionConvert.to(questionService.mget(keys), option);
				List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
				for (Long qId : map.keySet()) {
					VQuestion v = map.get(qId);

					if (v.getType() != Type.COMPOSITE) {
						v.setInQuestionCar(carQuestions.contains(v.getId()));
						map.put(qId, v);
					}
				}

				return map;
			}

		});
	}

}
