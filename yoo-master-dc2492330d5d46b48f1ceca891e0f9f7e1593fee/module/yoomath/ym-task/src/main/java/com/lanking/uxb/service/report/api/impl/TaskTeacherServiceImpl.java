package com.lanking.uxb.service.report.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.uxb.service.report.api.TaskTeacherService;

@Transactional(readOnly = true)
@Service
public class TaskTeacherServiceImpl implements TaskTeacherService {

	@Autowired
	@Qualifier("TeacherRepo")
	Repo<Teacher, Long> teacherRepo;

	@Override
	public Teacher getId(Long teacherId) {
		return teacherRepo.get(teacherId);
	}

}
