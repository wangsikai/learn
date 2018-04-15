package com.lanking.uxb.service.resources.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.uxb.service.resources.api.ExerciseService;

@Transactional(readOnly = true)
@Service
public class ExerciseServiceImpl implements ExerciseService {

	@Autowired
	@Qualifier("ExerciseRepo")
	Repo<Exercise, Long> exerciseRepo;

	@Transactional(readOnly = true)
	@Override
	public Exercise get(long id) {
		return exerciseRepo.get(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, Exercise> mget(Collection<Long> ids) {
		return exerciseRepo.mget(ids);
	}

}
