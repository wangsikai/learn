package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationClassStudentService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationClassStudentServiceImpl implements TaskImperialExaminationClassStudentService {

	@Autowired
	@Qualifier("StudentRepo")
	private Repo<Student, Long> repo;

	@Override
	public List<Student> query(long classId) {
		return repo.find("$taskListAllStudents", Params.param("classId", classId))
				.list(Student.class);
	}
}
