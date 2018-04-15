package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExerciseQuestion;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.domain.yoomath.homework.ExerciseQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.counter.api.impl.UserCounterProvider;
import com.lanking.uxb.service.resources.api.ExerciseQuestionService;
import com.lanking.uxb.service.resources.ex.ExerciseException;
import com.lanking.uxb.service.zuoye.api.ZyExerciseService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyTextbookExerciseService;

@Transactional(readOnly = true)
@Service
public class ZyExerciseServiceImpl implements ZyExerciseService {

	@Autowired
	@Qualifier("ExerciseRepo")
	Repo<Exercise, Long> exerciseRepo;

	@Autowired
	private ZyTextbookExerciseService textbookExerciseService;
	@Autowired
	private ExerciseQuestionService exerciseQuestionService;
	@Autowired
	private ZyQuestionService questionService;
	@Autowired
	private UserCounterProvider userCounterProvider;

	@Override
	public Exercise findLatestOne(long teacherId, Integer textbookCode) {
		Params params = Params.param("teacherId", teacherId).put("filterTextbookExerciseId", 1);
		if (textbookCode != null) {
			params.put("textbookCode", textbookCode);
		}
		return exerciseRepo.find("$zyFindLatestOne", params).get();
	}

	@Override
	public Exercise findNoBooksIdLatestOne(long teacherId, Integer textbookCode) {
		Params params = Params.param("teacherId", teacherId).put("filterTextbookExerciseId", 1).put("filterBookId", 0);
		if (textbookCode != null) {
			params.put("textbookCode", textbookCode);
		}
		return exerciseRepo.find("$zyFindLatestOne", params).get();
	}

	@Override
	public Exercise findLatestOne(long teacherId, Long textbookExerciseId, Long sectionCode, Long bookId) {
		Params params = Params.param("teacherId", teacherId).put("filterTextbookExerciseId", 1);
		if (textbookExerciseId != null && textbookExerciseId > 0) {
			params.put("textbookExerciseId", textbookExerciseId);
		}
		if (sectionCode != null && sectionCode > 0) {
			params.put("sectionCode", sectionCode);
		}
		if (bookId != null && bookId > 0) {
			params.put("bookId", bookId);
			params.put("filterTextbookExerciseId", 0);
		}
		return exerciseRepo.find("$zyFindLatestOne", params).get();
	}

	@Override
	public List<Long> listQuestions(long teacherId, long textbookExerciseId, Long exerciseId) {
		List<Long> questionIds = Lists.newArrayList();
		if (exerciseId == null) {
			List<TextbookExerciseQuestion> textbookExerciseQuestions = textbookExerciseService
					.listQuestions(textbookExerciseId);
			for (TextbookExerciseQuestion teq : textbookExerciseQuestions) {
				questionIds.add(teq.getQuestionId());
			}
		} else {
			List<ExerciseQuestion> exerciseQuestions = exerciseQuestionService.getExerciseQuestion(exerciseId);
			for (ExerciseQuestion eq : exerciseQuestions) {
				questionIds.add(eq.getQuestionId());
			}
		}
		return questionIds;
	}

	@Override
	public List<Question> pullQuestions(Long textbookExerciseId, Long sectionCode, Set<Type> types, int count,
			List<Long> exQIds, BigDecimal minDifficulty, BigDecimal maxDifficulty) {
		if (sectionCode == null && textbookExerciseId != null) {
			sectionCode = textbookExerciseService.get(textbookExerciseId).getSectionCode();
		}
		return questionService.pullQuestions(sectionCode, types, count, exQIds, minDifficulty, maxDifficulty);
	}

	@Override
	public Long countTodayExercise(long teacherId, long textbookExerciseId, Long sectionCode) {
		Params params = Params.param("teacherId", teacherId);
		if (textbookExerciseId > 0) {
			params.put("textbookExerciseId", textbookExerciseId);
		}
		if (sectionCode != null && sectionCode > 0) {
			params.put("sectionCode", sectionCode);
		}
		return exerciseRepo.find("$zyCountTodayExercise", params).count();
	}

	@Transactional
	@Override
	public Exercise createBlankExercise(Exercise exercise) throws ExerciseException {
		if (StringUtils.isBlank(exercise.getName())) {
			throw new ExerciseException(ExerciseException.EXERCISE_NAME_BLANK);
		}
		if (exercise.getCreateId() == null) {
			throw new IllegalArgException();
		}
		exercise.setCreateAt(new Date());
		exercise.setUpdateAt(exercise.getCreateAt());
		exercise.setStatus(Status.ENABLED);
		Exercise p = exerciseRepo.save(exercise);
		// userCounterProvider.incrExercise(exercise.getCreateId(), 1);
		return p;
	}

	@Transactional
	@Override
	public Exercise createExercise(Exercise exercise, List<Long> questionIds) throws ExerciseException {
		Exercise p = createBlankExercise(exercise);
		if (CollectionUtils.isNotEmpty(questionIds)) {
			int sequence = 1;
			for (Long qid : questionIds) {
				exerciseQuestionService.appendQuestion(p.getId(), qid, sequence);
				sequence++;

			}
		}
		// userCounterProvider.incrExercise(exercise.getCreateId(), 1);
		return p;
	}

	@Override
	public Page<Exercise> query(long teacherId, long sectionCode, Pageable pageable) {
		return exerciseRepo.find("$zyQuery", Params.param("teacherId", teacherId).put("sectionCode", sectionCode))
				.fetch(pageable);
	}

}
