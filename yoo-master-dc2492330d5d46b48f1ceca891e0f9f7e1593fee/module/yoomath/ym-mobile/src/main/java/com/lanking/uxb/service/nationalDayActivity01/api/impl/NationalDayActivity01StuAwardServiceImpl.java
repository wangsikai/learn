package com.lanking.uxb.service.nationalDayActivity01.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01StuAward;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01StuAwardService;

@Transactional(readOnly = true)
@Service
public class NationalDayActivity01StuAwardServiceImpl implements NationalDayActivity01StuAwardService {

	@Autowired
	@Qualifier("NationalDayActivity01StuAwardRepo")
	private Repo<NationalDayActivity01StuAward, Long> nationalDayActivity01StuAwardRepo;

	@Override
	public List<NationalDayActivity01StuAward> getStuAward() {
		return nationalDayActivity01StuAwardRepo.find("$findStuAward").list();
	}

}
