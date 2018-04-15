package com.lanking.uxb.service.imperialExamination.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityProcessLog;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityProcessLogService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationActivityProcessLogServiceImpl implements
		TaskImperialExaminationActivityProcessLogService {

	@Autowired
	@Qualifier("ImperialExaminationActivityProcessLogRepo")
	private Repo<ImperialExaminationActivityProcessLog, Long> repo;

	@Override
	public ImperialExaminationActivityProcessLog get(long code, ImperialExaminationProcess process,String data) {
		return repo.find("$TaskQueryActivityProcessLog", Params.param("code", code).put("process", process.ordinal()).put("data", data))
				.get();
	}

	@Transactional
	@Override
	public void create(ImperialExaminationActivityProcessLog log) {
		repo.save(log);
	}

}
