package com.lanking.uxb.service.school.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.school.api.SchoolQuestionService;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
@Service
@Transactional(readOnly = true)
public class SchoolQuestionServiceImpl implements SchoolQuestionService {
	@Autowired
	@Qualifier("SchoolQuestionRepo")
	private Repo<SchoolQuestion, Long> repo;

	@Override
	public long countBySchool(long schoolId) {
		return repo.find("$countBySchool", Params.param("schoolId", schoolId)).count();
	}
}
