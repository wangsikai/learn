package com.lanking.uxb.zycon.base.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.ExerciseQuestion;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.counter.api.impl.ExerciseCounterProvider;
import com.lanking.uxb.zycon.base.api.ZycExerciseQuestionService;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;

@Transactional(readOnly = true)
@Service
public class ZycExerciseQuestionServiceImpl implements ZycExerciseQuestionService {

	@Autowired
	@Qualifier("ExerciseQuestionRepo")
	Repo<ExerciseQuestion, Long> exerciseQuestionRepo;

	@Autowired
	private ExerciseCounterProvider exerciseCounterProvider;

	@Transactional(readOnly = true)
	@Override
	public ExerciseQuestion get(long id) throws YoomathConsoleException {
		return exerciseQuestionRepo.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, ExerciseQuestion> mget(Collection<Long> ids) throws YoomathConsoleException {
		return exerciseQuestionRepo.mget(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ExerciseQuestion> mgetList(Collection<Long> ids) throws YoomathConsoleException {
		return exerciseQuestionRepo.mgetList(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ExerciseQuestion> getExerciseQuestion(long exerciseId) throws YoomathConsoleException {
		return exerciseQuestionRepo.find("$getExerciseQuestion", Params.param("exerciseId", exerciseId)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Long> getQuestion(long exerciseId) throws YoomathConsoleException {
		return exerciseQuestionRepo.find("$getQuestion", Params.param("exerciseId", exerciseId)).list(Long.class);
	}

	@Transactional
	@Override
	public ExerciseQuestion appendQuestion(long exerciseId, long questionId, Integer initSequence)
			throws YoomathConsoleException {
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

	@Transactional
	@Override
	public void removeQuestion(long exerciseId, long questionId) throws YoomathConsoleException {
		exerciseQuestionRepo.execute("$delQuestion",
				Params.param("exerciseId", exerciseId).put("questionId", questionId));
		exerciseCounterProvider.incrQuestionCount(exerciseId, -1);
	}

	@Transactional
	@Override
	public void upQuestion(long exerciseId, long questionId) throws YoomathConsoleException {
		List<ExerciseQuestion> ps = getExerciseQuestion(exerciseId);
		if (CollectionUtils.isNotEmpty(ps)) {
			int len = ps.size();
			if (len > 1) {
				int index = -1;
				for (int i = 0; i < len; i++) {
					if (ps.get(i).getQuestionId() == questionId) {
						index = i;
						break;
					}
				}
				if (index != -1 && index != 0) {
					ExerciseQuestion p = ps.get(index);
					int sequence = p.getSequence();
					ExerciseQuestion p0 = ps.get(index - 1);
					int sequence0 = p0.getSequence();
					p.setSequence(sequence0);
					exerciseQuestionRepo.save(p);
					p0.setSequence(sequence);
					exerciseQuestionRepo.save(p0);
				}
			}
		}
	}

	@Transactional
	@Override
	public void downQuestion(long exerciseId, long questionId) throws YoomathConsoleException {
		List<ExerciseQuestion> ps = getExerciseQuestion(exerciseId);
		if (CollectionUtils.isNotEmpty(ps)) {
			int len = ps.size();
			if (len > 1) {
				int index = -1;
				for (int i = 0; i < len; i++) {
					if (ps.get(i).getQuestionId() == questionId) {
						index = i;
						break;
					}
				}
				if (index != -1 && index != len - 1) {
					ExerciseQuestion p = ps.get(index);
					int sequence = p.getSequence();
					ExerciseQuestion p0 = ps.get(index + 1);
					int sequence0 = p0.getSequence();
					p.setSequence(sequence0);
					exerciseQuestionRepo.save(p);
					p0.setSequence(sequence);
					exerciseQuestionRepo.save(p0);
				}
			}
		}
	}

	@Transactional(readOnly = true)
	@Override
	public ExerciseQuestion findOne(long exerciseId, long questionId) throws YoomathConsoleException {
		return exerciseQuestionRepo.find("$getExerciseQuestion",
				Params.param("exerciseId", exerciseId).put("questionId", questionId)).get();
	}

}
