package com.lanking.uxb.service.examactivity001.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.examactivity001.api.ExamActivity01Service;

@Service
@Transactional(readOnly = true)
public class ExamActivity01ServiceImpl implements ExamActivity01Service {
	@Autowired
	@Qualifier("ExamActivity001Repo")
	private Repo<ExamActivity001, Long> repo;

	@Override
	public ExamActivity001 getActivity(Long code) {
		return repo.find("$getActivity", Params.param("code", code)).get();
	}
	
}
