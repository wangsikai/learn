package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkStudentClazzService;

@Service
@Transactional
public class StaticHomeworkStudentClazzServiceImpl implements StaticHomeworkStudentClazzService {

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	private Repo<HomeworkStudentClazz, Long> homeworkStudentClazzRepo;

	@Override
	public HomeworkStudentClazz getByStudentExit(long classId, long studentId) {
		List<HomeworkStudentClazz> scs = homeworkStudentClazzRepo
				.find("$getByStudentExit", Params.param("classId", classId).put("studentId", studentId)).list();

		if (CollectionUtils.isNotEmpty(scs)) {
			return scs.get(0);
		}
		return null;
	}
}
