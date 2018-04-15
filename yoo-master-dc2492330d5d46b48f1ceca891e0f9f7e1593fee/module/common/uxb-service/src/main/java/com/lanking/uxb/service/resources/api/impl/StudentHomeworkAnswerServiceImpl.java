package com.lanking.uxb.service.resources.api.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.ex.HomeworkException;

@Transactional(readOnly = true)
@Service
public class StudentHomeworkAnswerServiceImpl implements StudentHomeworkAnswerService {

	@Autowired
	@Qualifier("StudentHomeworkAnswerRepo")
	Repo<StudentHomeworkAnswer, Long> studentHomeworkAnswerRepo;

	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private StudentHomeworkService shService;

	@Transactional(readOnly = true)
	@Override
	public StudentHomeworkAnswer get(long id) {
		return studentHomeworkAnswerRepo.get(id);
	}

	@Transactional
	@Override
	public StudentHomeworkAnswer create(long studentHomeworkQuestionId, int sequence) {
		StudentHomeworkAnswer p = new StudentHomeworkAnswer();
		p.setResult(HomeworkAnswerResult.INIT);
		p.setStudentHomeworkQuestionId(studentHomeworkQuestionId);
		p.setSequence(sequence);
		return studentHomeworkAnswerRepo.save(p);
	}

	@Transactional
	@Override
	public void save(Collection<StudentHomeworkAnswer> answers) throws HomeworkException {
		if (null != answers) {
			Set<StudentHomeworkAnswer> entities = new HashSet<StudentHomeworkAnswer>(answers.size());
			for (StudentHomeworkAnswer studentHomeworkAnswer : answers) {
				if (null != studentHomeworkAnswer.getId()) {
					StudentHomeworkAnswer old = studentHomeworkAnswerRepo.get(studentHomeworkAnswer.getId());
					old.setAnswerAt(studentHomeworkAnswer.getAnswerAt());
					old.setAnswerId(studentHomeworkAnswer.getAnswerId());
					old.setContent(studentHomeworkAnswer.getContent());

					entities.add(old);
				} else {
					entities.add(studentHomeworkAnswer);
				}
			}
			studentHomeworkAnswerRepo.save(entities);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<StudentHomeworkAnswer> find(long studentHomeworkQuestionId) {
		return studentHomeworkAnswerRepo.find("$findStudentHomeworkAnswer",
				Params.param("studentHomeworkQuestionId", studentHomeworkQuestionId)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, List<StudentHomeworkAnswer>> find(Collection<Long> studentHomeworkQuestionIds) {
		Map<Long, List<StudentHomeworkAnswer>> map = Maps.newHashMap();
		for (Long id : studentHomeworkQuestionIds) {
			map.put(id, Lists.<StudentHomeworkAnswer>newArrayList());
		}
		List<StudentHomeworkAnswer> list = studentHomeworkAnswerRepo.find("$findStudentHomeworkAnswer",
				Params.param("studentHomeworkQuestionIds", studentHomeworkQuestionIds)).list();
		for (StudentHomeworkAnswer studentHomeworkAnswer : list) {
			List<StudentHomeworkAnswer> sas = map.get(studentHomeworkAnswer.getStudentHomeworkQuestionId());
			sas.add(studentHomeworkAnswer);
			map.put(studentHomeworkAnswer.getStudentHomeworkQuestionId(), sas);
		}
		return map;
	}

	@Transactional(readOnly = true)
	@Override
	public long countNotCorrect(long homeworkId) {
		return studentHomeworkAnswerRepo.find("$countNotCorrect", Params.param("homeworkId", homeworkId))
				.get(Long.class);
	}

	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	@Override
	public Map<Long, Long> countNotCorrected(Collection<Long> studentHomeworkIds) {
		Map<Long, Long> map = Maps.newHashMap();
		for (Long id : studentHomeworkIds) {
			map.put(id, 0L);
		}
		List<Map> list = studentHomeworkAnswerRepo
				.find("$countNotCorrected", Params.param("studentHomeworkIds", studentHomeworkIds)).list(Map.class);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map m : list) {
				long id = ((BigInteger) m.get("id")).longValue();
				long count = ((BigInteger) m.get("cou")).longValue();
				map.put(id, count);
			}
		}
		return map;
	}
}
