package com.lanking.uxb.zycon.homework.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkStudentClazzService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.5
 */
@Transactional(readOnly = true)
@Service
public class ZycHomeworkStudentClazzServiceImpl implements ZycHomeworkStudentClazzService {
	private static int MAX_STUDENT_PER_CLASS = 100;

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	private Repo<HomeworkStudentClazz, Long> repo;

	@Override
	public List<HomeworkStudentClazz> list(long classId) {
		return repo.find("$zyListStudents", Params.param("classId", classId).put("limit", MAX_STUDENT_PER_CLASS))
				.list();
	}
}
