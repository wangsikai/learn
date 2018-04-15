package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractiseQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPractiseQuestionService;

/**
 * @see ZyDailyPractiseQuestionService
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
@Service
@Transactional(readOnly = true)
public class ZyDailyPractiseQuestionServiceImpl implements ZyDailyPractiseQuestionService {
	@Autowired
	@Qualifier("DailyPractiseQuestionRepo")
	Repo<DailyPractiseQuestion, Long> repo;

	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;

	@Override
	public List<DailyPractiseQuestion> findByPractise(long pId) {
		Params params = Params.param("pId", pId);

		return repo.find("$zyFindByPractise", params).list();
	}

	@Override
	public List<Long> findPulledQuestionIds(long userId, long sectionCode) {
		return repo.find("$zyFindPulledQuestionIds", Params.param("userId", userId).put("sectionCode", sectionCode))
				.list(Long.class);
	}

	@Override
	@Transactional
	public void save(List<Long> qIds, long practiseId) {
		for (Long qId : qIds) {
			DailyPractiseQuestion question = new DailyPractiseQuestion();
			question.setCreateAt(new Date());
			question.setDone(false);
			question.setPractiseId(practiseId);
			question.setQuestionId(qId);
			repo.save(question);
		}
	}

	@Override
	@Transactional
	public void copy(long practiseId, long newPractiseId) {
		List<DailyPractiseQuestion> questions = findByPractise(practiseId);
		for (DailyPractiseQuestion q : questions) {
			DailyPractiseQuestion newQuestion = new DailyPractiseQuestion();
			newQuestion.setPractiseId(newPractiseId);
			newQuestion.setAnswer(q.getAnswer());
			newQuestion.setCreateAt(q.getCreateAt());
			newQuestion.setDone(q.isDone());
			newQuestion.setQuestionId(q.getQuestionId());
			newQuestion.setResult(q.getResult());
			newQuestion.setUpdateAt(q.getUpdateAt());

			repo.save(newQuestion);

			// 重新练习调用将原有的答案清空
			q.setAnswer(null);
			q.setCreateAt(new Date());
			q.setDone(false);
			q.setResult(null);
			q.setUpdateAt(null);

			repo.save(q);
		}
	}

	@Override
	@Transactional
	public void updateResults(List<Map<String, Object>> updateResults, long practiseId, long userId) {
		for (Map<String, Object> m : updateResults) {
			boolean done = (boolean) m.get("done");
			Params params = Params.param("questionId", m.get("questionId"));
			params.put("result", ((HomeworkAnswerResult) m.get("result")).getValue());
			params.put("done", done);
			params.put("answer", JSON.toJSONString(m.get("answer")));
			params.put("practiseId", practiseId);

			repo.execute("$zyUpdateQuestion", params);

		}
	}

	@Override
	public long countStudentQuestion(int textbookCode, long studentId) {
		return repo.find("$zyFindStudentQuestionCount",
				Params.param("textbookCode", textbookCode).put("studentId", studentId)).count();
	}

	@Override
	@Transactional
	public int draft(List<Map<Long, List<String>>> answerList, List<Long> questionIds, long practiseId) {
		List<DailyPractiseQuestion> questions = findByPractise(practiseId);
		int doCount = 0;
		Map<Long, DailyPractiseQuestion> dailyQuestionMap = new HashMap<Long, DailyPractiseQuestion>(
				questionIds.size());
		for (DailyPractiseQuestion q : questions) {
			dailyQuestionMap.put(q.getQuestionId(), q);
		}
		for (int i = 0; i < questionIds.size(); i++) {
			DailyPractiseQuestion question = dailyQuestionMap.get(questionIds.get(i));
			question.setAnswer(answerList.get(i));
			Map<Long, List<String>> one = answerList.get(i);
			long qId = questionIds.get(i);
			List<String> stuAnswers = one.get(qId) == null ? one.get(String.valueOf(qId)) : one.get(qId);
			boolean done = CollectionUtils.isNotEmpty(stuAnswers);
			question.setDone(done);
			if (done) {
				doCount++;
			}
		}

		return doCount;
	}

	@Override
	@Transactional
	public void doOne(Map<Long, List<String>> answers, long id) {
		DailyPractiseQuestion question = repo.get(id);
		if (question == null) {
			throw new IllegalArgException();
		}

		question.setAnswer(answers);
		question.setDone(true);

		repo.save(question);
	}

	@Override
	public int countDone(long practiseId) {
		Long value = repo.find("$zyCountDone", Params.param("practiseId", practiseId)).get(Long.class);
		if (value == null) {
			return 0;
		}

		return value.intValue();
	}
}
