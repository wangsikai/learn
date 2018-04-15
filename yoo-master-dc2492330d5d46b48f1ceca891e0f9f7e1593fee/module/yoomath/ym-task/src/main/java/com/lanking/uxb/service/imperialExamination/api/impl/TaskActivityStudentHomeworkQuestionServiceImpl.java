package com.lanking.uxb.service.imperialExamination.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityStudentHomeworkQuestionService;

@Transactional(readOnly = true)
@Service
public class TaskActivityStudentHomeworkQuestionServiceImpl implements TaskActivityStudentHomeworkQuestionService {

	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	Repo<StudentHomeworkQuestion, Long> studentHomeworkQuestionRepo;

	@Transactional
	@Override
	public StudentHomeworkQuestion create(long studentHomeworkId, long questionId, boolean sub, boolean correct,
			Type type) {
		StudentHomeworkQuestion p = new StudentHomeworkQuestion();
		p.setQuestionId(questionId);
		p.setResult(HomeworkAnswerResult.INIT);
		p.setStudentHomeworkId(studentHomeworkId);
		p.setCorrect(correct);
		p.setSubFlag(sub);
		p.setType(type);
		return studentHomeworkQuestionRepo.save(p);
	}
}
