package com.lanking.intercomm.yoocorrect.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.correct.CorrectQuestionFailRecord;
import com.lanking.intercomm.yoocorrect.service.CorrectQuestionFailRecordService;

@Service
public class CorrectQuestionFailRecordServiceImpl implements CorrectQuestionFailRecordService {

	@Autowired
	@Qualifier("CorrectQuestionFailRecordRepo")
	private Repo<CorrectQuestionFailRecord, Long> repo;

	@Transactional
	@Override
	public void save(Collection<CorrectQuestionFailRecord> records) {
		repo.save(records);
	}
}
