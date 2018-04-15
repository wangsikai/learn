package com.lanking.uxb.service.correct.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkAnswerService;
import com.lanking.uxb.service.correct.vo.AnswerCorrectResult;

@Service
public class CorrectStudentHomeworkAnswerServiceImpl implements CorrectStudentHomeworkAnswerService {

	@Autowired
	@Qualifier("StudentHomeworkAnswerRepo")
	Repo<StudentHomeworkAnswer, Long> studentHomeworkAnswerRepo;

	@Override
	@Transactional
	public void saveCorrectResults(List<AnswerCorrectResult> answerResults) {
		if (CollectionUtils.isNotEmpty(answerResults)) {
			Date date = new Date();
			List<StudentHomeworkAnswer> anwsers = new ArrayList<StudentHomeworkAnswer>();
			for (AnswerCorrectResult result : answerResults) {
				StudentHomeworkAnswer anwser = studentHomeworkAnswerRepo.get(result.getStudentHomeworkAnswerId());
				anwser.setAutoResult(result.getAutoResult());
				anwser.setResult(result.getResult());
				anwser.setCorrectAt(date);
				anwsers.add(anwser);
			}
			studentHomeworkAnswerRepo.save(anwsers);
		}
	}

	@Override
	@Transactional
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
}
