package com.lanking.uxb.service.imperial.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityPrizes;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityPrizesService;

@Service
@Transactional(readOnly = true)
public class ImperialExaminationActivityPrizesServiceImpl implements ImperialExaminationActivityPrizesService {

	@Autowired
	@Qualifier("ImperialExaminationActivityPrizesRepo")
	private Repo<ImperialExaminationActivityPrizes, Long> repo;

	@Override
	public ImperialExaminationActivityPrizes get(Long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void addPrize(ImperialExaminationActivityPrizes prize) {
		repo.save(prize);
	}

}
