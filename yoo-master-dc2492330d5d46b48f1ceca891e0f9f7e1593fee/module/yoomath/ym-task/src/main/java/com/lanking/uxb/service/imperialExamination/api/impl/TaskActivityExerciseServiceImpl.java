package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.counter.api.impl.UserCounterProvider;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityExerciseQuestionService;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityExerciseService;
import com.lanking.uxb.service.resources.ex.ExerciseException;

@Transactional(readOnly = true)
@Service
public class TaskActivityExerciseServiceImpl implements TaskActivityExerciseService {

	@Autowired
	@Qualifier("ExerciseRepo")
	private Repo<Exercise, Long> exerciseRepo;

	@Autowired
	private TaskActivityExerciseQuestionService exerciseQuestionService;
	@Autowired
	private UserCounterProvider userCounterProvider;

	@Transactional(readOnly = true)
	@Override
	public Exercise get(long id) {
		return exerciseRepo.get(id);
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

}
