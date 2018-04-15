package com.lanking.uxb.service.zuoye.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.sectionPractise.SectionPractiseQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.value.VSectionPractiseQuestion;

/**
 * SectionPractiseQuestion -> VSectionPractiseQuestion
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.1
 */
@Component
public class ZySectionPractiseQuestionConvert extends
		Converter<VSectionPractiseQuestion, SectionPractiseQuestion, Long> {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;

	@Override
	protected Long getId(SectionPractiseQuestion sectionPractiseQuestion) {
		return sectionPractiseQuestion.getId();
	}

	@Override
	protected VSectionPractiseQuestion convert(SectionPractiseQuestion sectionPractiseQuestion) {
		VSectionPractiseQuestion v = new VSectionPractiseQuestion();
		v.setId(sectionPractiseQuestion.getId());
		v.setCreateAt(sectionPractiseQuestion.getCreateAt());
		v.setDone(sectionPractiseQuestion.isDone());
		v.setUpdateAt(sectionPractiseQuestion.getUpdateAt());
		v.setLatestResult(sectionPractiseQuestion.getResult());

		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VSectionPractiseQuestion, SectionPractiseQuestion, Long, VQuestion>() {

			@Override
			public boolean accept(SectionPractiseQuestion sectionPractiseQuestion) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(SectionPractiseQuestion sectionPractiseQuestion,
					VSectionPractiseQuestion vSectionPractiseQuestion) {
				return sectionPractiseQuestion.getQuestionId();
			}

			@Override
			public void setValue(SectionPractiseQuestion sectionPractiseQuestion, VSectionPractiseQuestion v,
					VQuestion value) {
				if (value != null) {
					v.setQuestion(value);
					if (sectionPractiseQuestion.getAnswer() == null) {
						v.setLatestAnswer(Collections.EMPTY_LIST);
					} else {
						v.setLatestAnswer(sectionPractiseQuestion.getAnswer().get(value.getId()));
					}

					if (Security.isClient()) {
						if (CollectionUtils.isNotEmpty(v.getLatestAnswer())) {
							List<String> answers = new ArrayList<String>(v.getLatestAnswer().size());
							for (String answer : v.getLatestAnswer()) {
								answers.add(QuestionUtils.process(answer,
										sectionPractiseQuestion.getResult() == HomeworkAnswerResult.RIGHT, true));
							}
							v.setLatestAnswer(answers);
						}
					}
				}
			}

			@Override
			public VQuestion getValue(Long key) {
				Question question = questionService.get(key);
				QuestionConvertOption option = new QuestionConvertOption(true, true, true, true, null);
				return questionConvert.to(question, option);
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}

				List<Question> questions = questionService.mgetList(keys);
				QuestionConvertOption option = new QuestionConvertOption(true, true, true, true, null);
				return questionConvert.toMap(questions, option);
			}
		});
	}
}
