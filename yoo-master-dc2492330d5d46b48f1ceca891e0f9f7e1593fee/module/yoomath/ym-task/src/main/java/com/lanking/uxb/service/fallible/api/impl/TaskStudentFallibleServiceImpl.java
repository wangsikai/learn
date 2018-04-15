package com.lanking.uxb.service.fallible.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.uxb.service.fallible.api.TaskStudentFallibleService;

@Transactional(readOnly = true)
@Service
public class TaskStudentFallibleServiceImpl implements TaskStudentFallibleService {
	@Autowired
	@Qualifier("StudentFallibleQuestionRepo")
	Repo<StudentFallibleQuestion, Long> studentFallibleRepo;
}
