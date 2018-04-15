package com.lanking.uxb.service.user.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.user.api.QuestionSchoolService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Transactional(readOnly = true)
@Service
public class QuestionSchoolServiceImpl implements QuestionSchoolService {
	@Autowired
	@Qualifier("QuestionSchoolRepo")
	private Repo<QuestionSchool, Long> repo;

	@Override
	@Transactional
	public QuestionSchool save(QuestionSchool questionSchool) {
		return repo.save(questionSchool);
	}

	@Override
	public QuestionSchool getBySchool(long schoolId) {
		return repo.find("$findBySchoolId", Params.param("schoolId", schoolId)).get();
	}
}
