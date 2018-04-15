package com.lanking.uxb.service.zuoye.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;

@Transactional(readOnly = true)
@Service
public class ZyCorrectingServiceImpl implements ZyCorrectingService {

	@Autowired
	private AnswerService answerService;
	@Autowired
	private QuestionService questionService;

	@Override
	public List<Map<String, Object>> simpleCorrect(List<Long> qIds, List<Map<Long, List<String>>> answerList) {
		List<Map<String, Object>> rets = new ArrayList<Map<String, Object>>(qIds.size());
		Map<Long, Question> questions = questionService.mget(qIds);
		Map<Long, List<Answer>> allAnswers = answerService.getQuestionAnswers(qIds);
		int index = 0;
		for (Long qId : qIds) {
			Question q = questions.get(qId);
			if (q == null || q.isSubFlag() || q.getType() == Type.FILL_BLANK || q.getType() == Type.QUESTION_ANSWERING
					|| q.getType() == Type.COMPOSITE || q.getType() == Type.TRUE_OR_FALSE) {
				// 处理异常
				throw new IllegalArgException();
			}
			Map<String, Object> oneRet = new HashMap<String, Object>(4);

			List<Answer> answers = allAnswers.get(qId);
			List<String> rightAnswers = new ArrayList<String>(answers.size());
			for (Answer answer : answers) {
				rightAnswers.add(answer.getContent());
			}
			Map<Long, List<String>> one = answerList.get(index);
			List<String> stuAnswers = one.get(qId) == null ? one.get(String.valueOf(qId)) : one.get(qId);

			if (CollectionUtils.isEmpty(stuAnswers)) {
				oneRet.put("done", false);
			} else {
				oneRet.put("done", true);
			}
			oneRet.put("answer", one);
			if (q.getType() == Type.SINGLE_CHOICE) {// 单选
				rightAnswers.removeAll(stuAnswers);
				if (rightAnswers.size() == 0) {
					oneRet.put("result", HomeworkAnswerResult.RIGHT);
				} else {
					oneRet.put("result", HomeworkAnswerResult.WRONG);
				}
			} else if (q.getType() == Type.MULTIPLE_CHOICE) {// 多选
				if (rightAnswers.size() != stuAnswers.size()) {
					oneRet.put("result", HomeworkAnswerResult.WRONG);
				} else {
					rightAnswers.removeAll(stuAnswers);
					if (rightAnswers.size() == 0) {
						oneRet.put("result", HomeworkAnswerResult.RIGHT);
					} else {
						oneRet.put("result", HomeworkAnswerResult.WRONG);
					}
				}
			}
			oneRet.put("qId", qId);
			index++;
			rets.add(oneRet);
		}
		return rets;
	}
}
