package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationActivityServiceImpl implements TaskImperialExaminationActivityService {

	@Autowired
	@Qualifier("ImperialExaminationActivityRepo")
	private Repo<ImperialExaminationActivity, Long> repo;

	@Transactional(readOnly = true)
	@Override
	public ImperialExaminationActivity get(long code) {
		return repo.get(code);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ImperialExaminationActivity> listAllProcessingActivity() {
		return repo.find("$TaskQueryActivity").list();
	}

}
