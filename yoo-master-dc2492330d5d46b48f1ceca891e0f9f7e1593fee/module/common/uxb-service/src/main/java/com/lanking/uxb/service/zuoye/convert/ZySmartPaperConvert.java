package com.lanking.uxb.service.zuoye.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaperQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.value.VPaperQuestion;

@Component
public class ZySmartPaperConvert extends Converter<VPaperQuestion, SmartExamPaperQuestion, Long> {
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private QuestionService questionService;

	@Override
	protected Long getId(SmartExamPaperQuestion s) {
		return s.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected VPaperQuestion convert(SmartExamPaperQuestion s) {
		VPaperQuestion vPaperQuestion = new VPaperQuestion();
		if (s.getAnswer() == null) {
			vPaperQuestion.setLatestAnswer(Collections.EMPTY_LIST);
		} else {
			vPaperQuestion.setLatestAnswer(s.getAnswer().get(s.getQuestionId()));
		}
		vPaperQuestion.setDone(s.isDone());
		vPaperQuestion.setQuestionId(s.getQuestionId());
		vPaperQuestion.setLatestResult(s.getResult());
		// 为客户端处理
		if (Security.isClient()) {
			if (CollectionUtils.isNotEmpty(vPaperQuestion.getLatestAnswer())) {
				List<String> answers = new ArrayList<String>(vPaperQuestion.getLatestAnswer().size());
				for (String answer : vPaperQuestion.getLatestAnswer()) {
					answers.add(QuestionUtils.process(answer, null, true));
				}
				vPaperQuestion.setLatestAnswer(answers);
			}
		}
		return vPaperQuestion;
	}

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VPaperQuestion, SmartExamPaperQuestion, Long, VQuestion>() {

			@Override
			public boolean accept(SmartExamPaperQuestion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(SmartExamPaperQuestion s, VPaperQuestion d) {
				return s.getQuestionId();
			}

			@Override
			public void setValue(SmartExamPaperQuestion s, VPaperQuestion d, VQuestion value) {
				d.setQuestion(value);

			}

			@Override
			public VQuestion getValue(Long key) {

				QuestionConvertOption opt = new QuestionConvertOption();
				opt.setInitSub(false);
				opt.setAnalysis(true);
				opt.setAnswer(true);
				opt.setCollect(true);
				opt.setInitTextbookCategory(false);
				opt.setInitMetaKnowpoint(false);
				opt.setInitPhase(false);
				opt.setInitQuestionType(false);
				return questionConvert.to(questionService.get(key), opt);
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				QuestionConvertOption opt = new QuestionConvertOption();
				opt.setInitSub(false);
				opt.setAnalysis(true);
				opt.setAnswer(true);
				opt.setCollect(true);
				opt.setInitTextbookCategory(false);
				opt.setInitMetaKnowpoint(false);
				opt.setInitPhase(false);
				opt.setInitQuestionType(false);
				return questionConvert.to(questionService.mget(keys), opt);
			}

		});
	}
}
