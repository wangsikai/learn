package com.lanking.uxb.service.resources.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.ExerciseQuestion;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.counter.api.impl.ExerciseCounterProvider;
import com.lanking.uxb.service.resources.api.ExerciseQuestionService;
import com.lanking.uxb.service.resources.ex.ExerciseException;

@Transactional(readOnly = true)
@Service
public class ExerciseQuestionServiceImpl implements ExerciseQuestionService {

	@Autowired
	@Qualifier("ExerciseQuestionRepo")
	Repo<ExerciseQuestion, Long> exerciseQuestionRepo;

	@Autowired
	private ExerciseCounterProvider exerciseCounterProvider;

	@Transactional(readOnly = true)
	@Override
	public List<ExerciseQuestion> getExerciseQuestion(long exerciseId) throws ExerciseException {
		return exerciseQuestionRepo.find("$getExerciseQuestion", Params.param("exerciseId", exerciseId)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Long> getQuestion(long exerciseId) throws ExerciseException {
		return exerciseQuestionRepo.find("$getQuestion", Params.param("exerciseId", exerciseId)).list(Long.class);
	}

	@Transactional
	@Override
	public ExerciseQuestion appendQuestion(long exerciseId, long questionId, Integer initSequence)
			throws ExerciseException {
		ExerciseQuestion p = new ExerciseQuestion();
		p.setExerciseId(exerciseId);
		p.setQuestionId(questionId);
		p.setStatus(Status.ENABLED);
		int sequence = 1;
		if (initSequence == null) {
			Integer maxSequence = exerciseQuestionRepo.find("$getMaxSequence", Params.param("exerciseId", exerciseId))
					.get(Integer.class);
			if (maxSequence != null) {
				sequence = maxSequence + 1;
			}
		} else {
			sequence = initSequence;
		}
		p.setSequence(sequence);
		exerciseCounterProvider.incrQuestionCount(exerciseId, 1);
		return exerciseQuestionRepo.save(p);
	}

}
