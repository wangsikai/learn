package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskActivityStudentClazzService;

@Transactional(readOnly = true)
@Service
public class TaskActivityStudentClazzServiceImpl implements TaskActivityStudentClazzService {

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	Repo<HomeworkStudentClazz, Long> hkStudentClazzRepo;

	@Override
	public List<Long> listClassStudents(long classId) {

		return hkStudentClazzRepo.find("$TaskListClassStudents", Params.param("classId", classId)).list(Long.class);
	}

}
