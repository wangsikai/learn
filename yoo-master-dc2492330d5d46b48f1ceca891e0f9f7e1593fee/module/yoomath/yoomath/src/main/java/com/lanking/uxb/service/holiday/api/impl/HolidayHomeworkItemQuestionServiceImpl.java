package com.lanking.uxb.service.holiday.api.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItemQuestion;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.question.api.QuestionService;

@Service
@Transactional(readOnly = true)
public class HolidayHomeworkItemQuestionServiceImpl implements HolidayHomeworkItemQuestionService {
	@Autowired
	@Qualifier("HolidayHomeworkItemQuestionRepo")
	private Repo<HolidayHomeworkItemQuestion, Long> itemQuestionRepo;
	@Autowired
	private QuestionService questionService;

	@Override
	public List<Long> queryQuestions(long holidayHomeworkItemId) {
		return itemQuestionRepo.find("$queryQuestions", Params.param("holidayHomeworkItemId", holidayHomeworkItemId))
				.list(Long.class);
	}

	@Transactional
	public void create(List<Long> questionIds, long homeworkItemId) {
		int sequence = 1;
		for (Long qId : questionIds) {
			HolidayHomeworkItemQuestion question = new HolidayHomeworkItemQuestion();
			question.setHolidayHomeworkItemId(homeworkItemId);
			question.setQuestionId(qId);
			question.setSequence(sequence);
			question.setStatus(Status.ENABLED);
			itemQuestionRepo.save(question);

			sequence++;
		}
	}

	@Override
	public List<HolidayHomeworkItemQuestion> getHomeworkQuestion(Long id) {
		return itemQuestionRepo.find("$getHomeworkItemQuestion", Params.param("holidayHomeworkItemId", id)).list();
	}

	@Override
	public HolidayHomeworkItemQuestion find(Long questionId, Long holidayHomeworkItemId) {
		return itemQuestionRepo.find("$findQuestionByItem",
				Params.param("questionId", questionId).put("holidayHomeworkItemId", holidayHomeworkItemId)).get();
	}

	@Override
	public List<HolidayHomeworkItemQuestion> find(Collection<Long> qIds, Long holidayHomeworkItemId) {
		return itemQuestionRepo.find("$findQuestionByItem",
				Params.param("questionIds", qIds).put("holidayHomeworkItemId", holidayHomeworkItemId)).list();
	}

	@Override
	public List<Map> listWrongStu(long homeworkItemId, long questionId) {
		return itemQuestionRepo.find("$listWrongStu",
				Params.param("homeworkItemId", homeworkItemId).put("questionId", questionId)).list(Map.class);
	}

	@Override
	public void updateStatistics(long questionId, long itemId, int incrRightCount, int incrWrongCount) {
		HolidayHomeworkItemQuestion itemQuestion = itemQuestionRepo.find("$findByQuestionAndItem",
				Params.param("questionId", questionId).put("itemId", itemId)).get();
		int rightCount = (itemQuestion.getRightCount() == null ? 0 : itemQuestion.getRightCount()) + incrRightCount;
		int wrongCount = (itemQuestion.getWrongCount() == null ? 0 : itemQuestion.getWrongCount()) + incrWrongCount;
		double rightRate = (rightCount * 100d) / (rightCount + wrongCount);

		itemQuestion.setRightCount(rightCount);
		itemQuestion.setWrongCount(wrongCount);
		itemQuestion.setRightRate(BigDecimal.valueOf(rightRate).setScale(0, BigDecimal.ROUND_HALF_UP));

		itemQuestionRepo.save(itemQuestion);
	}

	@Override
	public List<Double> getRateStat(long holidayHomeworkItemId) {
		return itemQuestionRepo.find("$getRateStat", Params.param("holidayHomeworkItemId", holidayHomeworkItemId))
				.list(Double.class);
	}

	@Override
	public Map<Long, List<Question>> mgetByItemIds(Collection<Long> itemIds) {
		List<HolidayHomeworkItemQuestion> questions = itemQuestionRepo.find("$mgetByItemIds",
				Params.param("itemIds", itemIds)).list();

		Map<Long, List<Question>> retMap = new HashMap<Long, List<Question>>(questions.size());
		List<Long> questionIds = Lists.newArrayList();
		for (HolidayHomeworkItemQuestion q : questions) {
			questionIds.add(q.getQuestionId());
		}
		Map<Long, Question> questionMap = questionService.mget(questionIds);
		for (HolidayHomeworkItemQuestion q : questions) {
			if (retMap.get(q.getHolidayHomeworkItemId()) == null) {
				retMap.put(q.getHolidayHomeworkItemId(), Lists.<Question> newArrayList());
			}
			retMap.get(q.getHolidayHomeworkItemId()).add(questionMap.get(q.getQuestionId()));
		}
		return retMap;
	}
}
