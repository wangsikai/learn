package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAwardStudent;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityAwardStudentService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationActivityAwardStudentServiceImpl implements TaskImperialExaminationActivityAwardStudentService {

	@Autowired
	@Qualifier("ImperialExaminationActivityAwardStudentRepo")
	private Repo<ImperialExaminationActivityAwardStudent, Long> repo;

	@Transactional
	@Override
	public void save(ImperialExaminationActivityAwardStudent award) {
		if (award.getId() == null) {
			repo.save(award);
		} else {
			ImperialExaminationActivityAwardStudent ieaa = repo.get(award.getId());
			if (award.getRank() != null) {
				ieaa.setRank(award.getRank());
				ieaa.setAwardLevel(award.getAwardLevel());
			}
			repo.save(ieaa);
		}
	}

	@Override
	public List<ImperialExaminationActivityAwardStudent> queryRank(long activityCode) {
		return repo.find("$TaskStudentAwardRank", Params.param("code", activityCode)).list();
	}

	@Transactional
	@Override
	public void save(Collection<ImperialExaminationActivityAwardStudent> awards) {
		repo.save(awards);
	}

}
