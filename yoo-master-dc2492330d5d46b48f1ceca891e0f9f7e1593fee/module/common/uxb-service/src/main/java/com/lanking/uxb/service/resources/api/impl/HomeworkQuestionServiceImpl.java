package com.lanking.uxb.service.resources.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.counter.api.impl.HomeworkCounterProvider;
import com.lanking.uxb.service.resources.api.ExerciseQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.ex.ExerciseException;
import com.lanking.uxb.service.resources.ex.HomeworkException;

@Transactional(readOnly = true)
@Service
public class HomeworkQuestionServiceImpl implements HomeworkQuestionService {

	@Autowired
	@Qualifier("HomeworkQuestionRepo")
	Repo<HomeworkQuestion, Long> homeworkQuestionRepo;
	@Autowired
	private ExerciseQuestionService exerciseQuestionService;
	@Autowired
	private HomeworkCounterProvider homeworkCounterProvider;

	@Transactional(readOnly = true)
	@Override
	public HomeworkQuestion get(long id) throws HomeworkException {
		return homeworkQuestionRepo.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, HomeworkQuestion> mget(Collection<Long> ids) throws HomeworkException {
		return homeworkQuestionRepo.mget(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public List<HomeworkQuestion> mgetList(Collection<Long> ids) throws HomeworkException {
		return homeworkQuestionRepo.mgetList(ids);
	}

	@Transactional(readOnly = true)
	@Override
	public List<HomeworkQuestion> getHomeworkQuestion(long homeworkId) throws HomeworkException {
		return homeworkQuestionRepo.find("$getHomeworkQuestion", Params.param("homeworkId", homeworkId)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Long> getQuestion(long homeworkId) throws HomeworkException {
		return homeworkQuestionRepo.find("$getQuestion", Params.param("homeworkId", homeworkId)).list(Long.class);
	}

	@Transactional
	@Override
	public HomeworkQuestion appendQuestion(long homeworkId, long questionId, Integer initSequence)
			throws ExerciseException {
		HomeworkQuestion p = new HomeworkQuestion();
		p.setHomeworkId(homeworkId);
		p.setQuestionId(questionId);
		p.setStatus(Status.ENABLED);
		int sequence = 1;
		if (initSequence == null) {
			Integer maxSequence = homeworkQuestionRepo.find("$getMaxSequence", Params.param("homeworkId", homeworkId))
					.get(Integer.class);
			if (maxSequence != null) {
				sequence = maxSequence + 1;
			}
		} else {
			sequence = initSequence;
		}
		p.setSequence(sequence);
		HomeworkQuestion p0 = homeworkQuestionRepo.save(p);
		// homeworkCounterProvider.incrQuestionCount(homeworkId, 1);
		return p0;
	}

	@Transactional
	@Override
	public void removeQuestion(long homeworkId, long questionId) throws HomeworkException {
		homeworkQuestionRepo.execute("$delQuestion",
				Params.param("homeworkId", homeworkId).put("questionId", questionId));
		// homeworkCounterProvider.incrQuestionCount(homeworkId, -1);
	}

	@Transactional
	@Override
	public void upQuestion(long homeworkId, long questionId) throws HomeworkException {
		List<HomeworkQuestion> ps = getHomeworkQuestion(homeworkId);
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
					HomeworkQuestion p = ps.get(index);
					int sequence = p.getSequence();
					HomeworkQuestion p0 = ps.get(index - 1);
					int sequence0 = p0.getSequence();
					p.setSequence(sequence0);
					homeworkQuestionRepo.save(p);
					p0.setSequence(sequence);
					homeworkQuestionRepo.save(p0);
				}
			}
		}
	}

	@Transactional
	@Override
	public void downQuestion(long homeworkId, long questionId) throws HomeworkException {
		List<HomeworkQuestion> ps = getHomeworkQuestion(homeworkId);
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
					HomeworkQuestion p = ps.get(index);
					int sequence = p.getSequence();
					HomeworkQuestion p0 = ps.get(index + 1);
					int sequence0 = p0.getSequence();
					p.setSequence(sequence0);
					homeworkQuestionRepo.save(p);
					p0.setSequence(sequence);
					homeworkQuestionRepo.save(p0);
				}
			}
		}
	}

	@Transactional
	@Override
	public void createByExercise(long homeworkId, long exerciseId) throws HomeworkException {
		List<Long> qids = exerciseQuestionService.getQuestion(exerciseId);
		int i = 1;
		for (Long qid : qids) {
			HomeworkQuestion p = new HomeworkQuestion();
			p.setHomeworkId(homeworkId);
			p.setQuestionId(qid);
			p.setSequence(i);
			p.setStatus(Status.ENABLED);
			homeworkQuestionRepo.save(p);
			i++;
		}
	}

	@Transactional(readOnly = true)
	@Override
	public HomeworkQuestion findOne(long homeworkId, long questionId) throws HomeworkException {
		return homeworkQuestionRepo
				.find("$getHomeworkQuestion", Params.param("homeworkId", homeworkId).put("questionId", questionId))
				.get();
	}

	@Transactional(readOnly = true)
	@Override
	public long countQuestion(long homeworkId) {
		return homeworkQuestionRepo.find("$countQuestion", Params.param("homeworkId", homeworkId)).count();
	}

	@Transactional
	@Override
	public void deleteByHomework(long homeworkId) {
		homeworkQuestionRepo.execute("$delQuestion", Params.param("homeworkId", homeworkId));
	}

	@Override
	public List<Long> findHomeworkQuestionsByType(long hkid, int type) {
		return homeworkQuestionRepo.find("$findHomeworkQuestionsByType", Params.param("homeworkId", hkid).put("questionType", type)).list(Long.class);
	}

}
