package com.lanking.uxb.zycon.base.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExerciseQuestion;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.uxb.zycon.base.api.ZycExerciseService;
import com.lanking.uxb.zycon.base.api.ZycTextbookExerciseService;

@Transactional(readOnly = true)
@Service
public class ZycExerciseServiceImpl implements ZycExerciseService {

	@Autowired
	@Qualifier("ExerciseRepo")
	Repo<Exercise, Long> exerciseRepo;

	@Autowired
	private ZycTextbookExerciseService textbookExerciseService;

	@Override
	public List<Long> listQuestions(long teacherId, long textbookExerciseId) {
		List<Long> questionIds = Lists.newArrayList();
		List<TextbookExerciseQuestion> textbookExerciseQuestions = textbookExerciseService
				.listQuestions(textbookExerciseId);
		for (TextbookExerciseQuestion teq : textbookExerciseQuestions) {
			questionIds.add(teq.getQuestionId());
		}
		return questionIds;
	}

}
