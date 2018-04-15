package com.lanking.uxb.service.report.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.report.api.TaskHomeworkStudentClazzService;

@Transactional(readOnly = true)
@Service
public class TaskStudentHomeworkServiceImpl implements TaskHomeworkStudentClazzService {

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	Repo<HomeworkStudentClazz, Long> hkStudentClazzRepo;

	// 一个班级最多的学生数量
	private static int MAX_STUDENT_PER_CLASS = 100;

	@Override
	public List<HomeworkStudentClazz> list(long classId) {
		return hkStudentClazzRepo.find("$zyListStudents",
				Params.param("classId", classId).put("limit", MAX_STUDENT_PER_CLASS)).list();
	}
}
