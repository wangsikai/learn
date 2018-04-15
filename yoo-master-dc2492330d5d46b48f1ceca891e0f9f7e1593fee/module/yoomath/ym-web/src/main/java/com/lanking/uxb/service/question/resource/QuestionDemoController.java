package com.lanking.uxb.service.question.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;

@RestController
@RequestMapping(value = "q/demo")
public class QuestionDemoController {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private AnswerService answerService;

	private String delPContent(String str) {
		if (StringUtils.isNotBlank(str) && str.indexOf("<p>") == 0 && str.lastIndexOf("</p>") == str.length() - 4) {
			// 去除首尾的P标签
			str = str.substring(3, str.length() - 4);
		}
		return str;
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "detail")
	@MemberAllowed
	public Value query(long id) {
		Map<String, Object> map = new HashMap<String, Object>(2);

		Question question = questionService.get(id);
		QuestionConvertOption option = new QuestionConvertOption();
		option.setAnalysis(true);
		option.setAnswer(true);
		option.setInitKnowledgePoint(true);
		option.setInitMetaKnowpoint(true);
		option.setInitExamination(true);
		option.setInitPhase(true);
		option.setInitTextbookCategory(true);
		option.setInitSubject(true);
		option.setInitQuestionType(true);
		option.setInitQuestionTag(true);
		VQuestion mQuestion = questionConvert.to(question, option);
		map.put("mquestion", mQuestion);

		VQuestion hQuestion = questionConvert.to(question, option);
		// 还原题目中的相关内容
		hQuestion.setContent(question.getContent());
		hQuestion.setHint(question.getHint());
		hQuestion.setAnalysis(question.getAnalysis());
		List<String> choices = Lists.newArrayList();
		if (StringUtils.isNotBlank(question.getChoiceA())) {
			choices.add(this.delPContent(question.getChoiceA()));
		}
		if (StringUtils.isNotBlank(question.getChoiceB())) {
			choices.add(this.delPContent(question.getChoiceB()));
		}
		if (StringUtils.isNotBlank(question.getChoiceC())) {
			choices.add(this.delPContent(question.getChoiceC()));
		}
		if (StringUtils.isNotBlank(question.getChoiceD())) {
			choices.add(this.delPContent(question.getChoiceD()));
		}
		if (StringUtils.isNotBlank(question.getChoiceE())) {
			choices.add(this.delPContent(question.getChoiceE()));
		}
		if (StringUtils.isNotBlank(question.getChoiceF())) {
			choices.add(this.delPContent(question.getChoiceF()));
		}
		hQuestion.setChoices(choices);
		// 还原答案中的相关内容
		List<Answer> answers = answerService.getQuestionAnswers(id);
		for (int i = 0; i < answers.size(); i++) {
			hQuestion.getAnswers().get(i).setContent(answers.get(i).getContent());
		}
		map.put("hQuestion", hQuestion);
		return new Value(map);
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "details")
	@MemberAllowed
	public Value mutilQuery(@RequestParam List<Long> ids) {
		if (ids == null || ids.size() == 0) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> map = new HashMap<String, Object>(2);

		Map<Long, Question> questionMap = questionService.mget(ids);
		QuestionConvertOption option = new QuestionConvertOption();
		option.setAnalysis(true);
		option.setAnswer(true);
		option.setInitKnowledgePoint(true);
		option.setInitMetaKnowpoint(true);
		option.setInitExamination(true);
		option.setInitPhase(true);
		option.setInitTextbookCategory(true);
		option.setInitSubject(true);
		option.setInitQuestionType(true);
		option.setInitQuestionTag(true);

		Map<Long, VQuestion> mQuestionMap = questionConvert.to(questionMap, option);
		List<VQuestion> hQuestions = new ArrayList<VQuestion>(questionMap.size());
		Map<Long, List<Answer>> answerMap = answerService.getQuestionAnswers(ids);

		for (Long questionId : ids) {
			Question question = questionMap.get(questionId);
			VQuestion hQuestion = mQuestionMap.get(questionId);
			if (question == null) {
				continue;
			}

			// 还原题目中的相关内容
			hQuestion.setContent(question.getContent());
			hQuestion.setHint(question.getHint());
			hQuestion.setAnalysis(question.getAnalysis());
			List<String> choices = Lists.newArrayList();
			if (StringUtils.isNotBlank(question.getChoiceA())) {
				choices.add(this.delPContent(question.getChoiceA()));
			}
			if (StringUtils.isNotBlank(question.getChoiceB())) {
				choices.add(this.delPContent(question.getChoiceB()));
			}
			if (StringUtils.isNotBlank(question.getChoiceC())) {
				choices.add(this.delPContent(question.getChoiceC()));
			}
			if (StringUtils.isNotBlank(question.getChoiceD())) {
				choices.add(this.delPContent(question.getChoiceD()));
			}
			if (StringUtils.isNotBlank(question.getChoiceE())) {
				choices.add(this.delPContent(question.getChoiceE()));
			}
			if (StringUtils.isNotBlank(question.getChoiceF())) {
				choices.add(this.delPContent(question.getChoiceF()));
			}
			hQuestion.setChoices(choices);
			// 还原答案中的相关内容
			List<Answer> answers = answerMap.get(questionId);
			for (int i = 0; i < answers.size(); i++) {
				hQuestion.getAnswers().get(i).setContent(answers.get(i).getContent());
			}
			hQuestions.add(hQuestion);
		}
		map.put("hQuestions", hQuestions);
		return new Value(map);
	}
}
