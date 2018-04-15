package com.lanking.uxb.service.examPaper.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examPaper.value.VExamPaperQuestion;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 商品试卷Convert
 *
 * @author zemin.song
 */
@Component
public class ExamPaperQuestionConvert extends Converter<VExamPaperQuestion, ExamPaperQuestion, Long> {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;

	@Override
	protected Long getId(ExamPaperQuestion examPaperQuestion) {
		return examPaperQuestion.getId();
	}

	@Override
	protected VExamPaperQuestion convert(ExamPaperQuestion examPaperQuestion) {
		VExamPaperQuestion v = new VExamPaperQuestion();
		v.setId(examPaperQuestion.getId());
		v.setCreateAt(examPaperQuestion.getCreateAt());
		v.setSequence(examPaperQuestion.getSequence());
		v.setTopicId(examPaperQuestion.getTopicId());
		v.setScore(examPaperQuestion.getScore());
		return v;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VExamPaperQuestion, ExamPaperQuestion, Long, VQuestion>() {

			@Override
			public boolean accept(ExamPaperQuestion examPaperQuestion) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaperQuestion examPaperQuestion, VExamPaperQuestion vExamPaperQuestion) {
				return examPaperQuestion.getQuestionId();
			}

			@Override
			public void setValue(ExamPaperQuestion examPaperQuestion, VExamPaperQuestion vExamPaperQuestion,
					VQuestion value) {
				if (value != null) {
					vExamPaperQuestion.setQuestion(value);
				}
			}

			@Override
			public VQuestion getValue(Long key) {
				return questionConvert.to(questionService.get(key), new QuestionConvertOption(true, true, true, false,
						true, null));
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return questionConvert.to(questionService.mget(keys), new QuestionConvertOption(true, true, true,
						false, true, null));
			}
		});
	}
}
