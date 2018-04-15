package com.lanking.uxb.service.fallible.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.fallible.FallibleOcrSearchRecord;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.fallible.api.TaskFallibleOcrSearchRecordService;

@Transactional(readOnly = true)
@Service
public class TaskFallibleOcrSearchRecordServiceImpl implements TaskFallibleOcrSearchRecordService {

	@Autowired
	@Qualifier("FallibleOcrSearchRecordRepo")
	private Repo<FallibleOcrSearchRecord, Long> fallibleOcrSearchRecordRepo;

	@Transactional
	@Override
	public FallibleOcrSearchRecord create(long fileId, List<Long> questions, Date createAt) {
		FallibleOcrSearchRecord record = new FallibleOcrSearchRecord();
		record.setFileId(fileId);
		record.setQuestions(questions);
		record.setCreateAt(createAt);
		return fallibleOcrSearchRecordRepo.save(record);
	}

	@Transactional
	@Override
	public void choose(long fileId, long chooseQuestion, Date chooseAt) {
		FallibleOcrSearchRecord record = fallibleOcrSearchRecordRepo.find("$taskFindByFileId",
				Params.param("fileId", fileId)).get();
		if (record != null) {
			record.setChooseAt(chooseAt);
			record.setChooseQuestion(chooseQuestion);
			fallibleOcrSearchRecordRepo.save(record);
		}
	}

}
