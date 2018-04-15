package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityAwardService;

@Transactional(readOnly = true)
@Service
public class TaskImperialExaminationActivityAwardServiceImpl implements TaskImperialExaminationActivityAwardService {

	@Autowired
	@Qualifier("ImperialExaminationActivityAwardRepo")
	private Repo<ImperialExaminationActivityAward, Long> repo;

	@Transactional
	@Override
	public void save(ImperialExaminationActivityAward award) {
		if (award.getId() == null) {
			repo.save(award);
		} else {
			ImperialExaminationActivityAward ieaa = repo.get(award.getId());
			if (award.getRank() != null) {
				ieaa.setRank(award.getRank());
				ieaa.setAwardLevel(award.getAwardLevel());
			}
			repo.save(ieaa);
		}
	}

	@Override
	public List<ImperialExaminationActivityAward> queryRank(long activityCode,Integer room) {
		Params params = Params.param("code", activityCode);
		if (room != null) {
			params.put("room", room);
		}
		return repo.find("$TaskAwardRank", params).list();
	}
	

	@Transactional
	@Override
	public void save(Collection<ImperialExaminationActivityAward> awards) {
		repo.save(awards);
	}

}
