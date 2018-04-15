package com.lanking.uxb.zycon.homework.api.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkAnswerService;

@Transactional(readOnly = true)
@Service
public class ZycStudentHomeworkAnswerServiceImpl implements ZycStudentHomeworkAnswerService {

	@Autowired
	@Qualifier("StudentHomeworkAnswerRepo")
	Repo<StudentHomeworkAnswer, Long> studentHomeworkAnswerRepo;

	@Transactional(readOnly = true)
	@Override
	public List<StudentHomeworkAnswer> find(long studentHomeworkQuestionId) {
		return studentHomeworkAnswerRepo.find("$zycFindStudentHomeworkAnswer",
				Params.param("studentHomeworkQuestionId", studentHomeworkQuestionId)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, List<StudentHomeworkAnswer>> find(Collection<Long> studentHomeworkQuestionIds) {
		Map<Long, List<StudentHomeworkAnswer>> map = Maps.newHashMap();
		for (Long id : studentHomeworkQuestionIds) {
			map.put(id, Lists.<StudentHomeworkAnswer> newArrayList());
		}
		List<StudentHomeworkAnswer> list = studentHomeworkAnswerRepo.find("$zycFindStudentHomeworkAnswer",
				Params.param("studentHomeworkQuestionIds", studentHomeworkQuestionIds)).list();
		for (StudentHomeworkAnswer studentHomeworkAnswer : list) {
			List<StudentHomeworkAnswer> sas = map.get(studentHomeworkAnswer.getStudentHomeworkQuestionId());
			sas.add(studentHomeworkAnswer);
			map.put(studentHomeworkAnswer.getStudentHomeworkQuestionId(), sas);
		}
		return map;
	}

	@Override
	public Map<Long, Long> countNotCorrected(Collection<Long> keys) {
		Map<Long, Long> map = Maps.newHashMap();
		for (Long id : keys) {
			map.put(id, 0L);
		}
		List<Map> list = studentHomeworkAnswerRepo.find("$zycCountNotCorrected",
				Params.param("studentHomeworkIds", keys)).list(Map.class);
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
