package com.lanking.uxb.service.imperialExamination.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityStudentHomeworkAnswerService;

@Transactional(readOnly = true)
@Service
public class TaskActivityStudentHomeworkAnswerServiceImpl implements TaskActivityStudentHomeworkAnswerService {

	@Autowired
	@Qualifier("StudentHomeworkAnswerRepo")
	Repo<StudentHomeworkAnswer, Long> studentHomeworkAnswerRepo;

	@Transactional
	@Override
	public StudentHomeworkAnswer create(long studentHomeworkQuestionId, int sequence) {
		StudentHomeworkAnswer p = new StudentHomeworkAnswer();
		p.setResult(HomeworkAnswerResult.INIT);
		p.setStudentHomeworkQuestionId(studentHomeworkQuestionId);
		p.setSequence(sequence);
		return studentHomeworkAnswerRepo.save(p);
	}
}
