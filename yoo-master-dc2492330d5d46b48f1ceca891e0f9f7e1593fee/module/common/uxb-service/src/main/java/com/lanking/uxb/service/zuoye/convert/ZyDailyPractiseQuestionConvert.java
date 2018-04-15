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
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractiseQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.value.VDailyPractiseQuestion;

/**
 * DailyPractiseQuestion -> VDailyPractiseQuestion
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
@Component
public class ZyDailyPractiseQuestionConvert extends Converter<VDailyPractiseQuestion, DailyPractiseQuestion, Long> {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;

	@Override
	protected Long getId(DailyPractiseQuestion dailyPractiseQuestion) {
		return dailyPractiseQuestion.getQuestionId();
	}

	@Override
	protected VDailyPractiseQuestion convert(DailyPractiseQuestion dailyPractiseQuestion) {
		VDailyPractiseQuestion v = new VDailyPractiseQuestion();
		v.setId(dailyPractiseQuestion.getId());
		v.setCreateAt(dailyPractiseQuestion.getCreateAt());
		v.setUpdateAt(dailyPractiseQuestion.getUpdateAt());
		v.setLatestResult(dailyPractiseQuestion.getResult());
		v.setDone(dailyPractiseQuestion.isDone());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VDailyPractiseQuestion, DailyPractiseQuestion, Long, VQuestion>() {

			@Override
			public boolean accept(DailyPractiseQuestion dailyPractiseQuestion) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(DailyPractiseQuestion dailyPractiseQuestion,
					VDailyPractiseQuestion vDailyPractiseQuestion) {
				return dailyPractiseQuestion.getQuestionId();
			}

			@Override
			public void setValue(DailyPractiseQuestion dailyPractiseQuestion, VDailyPractiseQuestion v,
					VQuestion value) {
				if (value != null) {
					v.setQuestion(value);
					if (dailyPractiseQuestion.getAnswer() == null) {
						v.setLatestAnswer(Collections.EMPTY_LIST);
					} else {
						v.setLatestAnswer(dailyPractiseQuestion.getAnswer().get(value.getId()));
					}

					if (Security.isClient()) {
						if (CollectionUtils.isNotEmpty(v.getLatestAnswer())) {
							List<String> answers = new ArrayList<String>(v.getLatestAnswer().size());
							for (String answer : v.getLatestAnswer()) {
								answers.add(QuestionUtils.process(answer,
										dailyPractiseQuestion.getResult() == HomeworkAnswerResult.RIGHT, true));
							}
							v.setLatestAnswer(answers);
						}
					}
				}
			}

			@Override
			public VQuestion getValue(Long key) {
				Question question = questionService.get(key);
				QuestionConvertOption opt = new QuestionConvertOption();
				opt.setAnalysis(true);
				opt.setAnswer(true);
				opt.setCollect(true);
				opt.setInitTextbookCategory(false);
				opt.setInitMetaKnowpoint(false);
				opt.setInitPhase(false);
				opt.setInitQuestionType(false);
				opt.setInitKnowledgePoint(true);
				opt.setInitStudentQuestionCount(true);
				opt.setInitQuestionTag(true);
				return questionConvert.to(question, opt);
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}

				List<Question> questions = questionService.mgetList(keys);
				QuestionConvertOption opt = new QuestionConvertOption();
				opt.setAnalysis(true);
				opt.setAnswer(true);
				opt.setCollect(true);
				opt.setInitTextbookCategory(false);
				opt.setInitMetaKnowpoint(false);
				opt.setInitPhase(false);
				opt.setInitQuestionType(false);
				opt.setInitKnowledgePoint(true);
				opt.setInitStudentQuestionCount(true);
				opt.setInitQuestionTag(true);
				return questionConvert.toMap(questions, opt);
			}
		});
	}
}
