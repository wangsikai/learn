package com.lanking.uxb.service.report.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStatistic;
import com.lanking.uxb.service.report.api.StudentHomeworkStatisticService;

@Transactional(readOnly = true)
@Service
public class StudentHomeworkStatisticServiceImpl implements StudentHomeworkStatisticService {

	@Autowired
	@Qualifier("StudentHomeworkStatisticRepo")
	private Repo<StudentHomeworkStatistic, Long> studentHomeworkStatisticRepo;

	@Override
	public StudentHomeworkStatistic get(Long userId) {
		return studentHomeworkStatisticRepo.get(userId);
	}

}
