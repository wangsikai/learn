package com.lanking.uxb.service.nationalDayActivity01.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01TeaAward;
import com.lanking.uxb.service.nationalDayActivity01.api.NationalDayActivity01TeaAwardService;

@Transactional(readOnly = true)
@Service
public class NationalDayActivity01TeaAwardServiceImpl implements NationalDayActivity01TeaAwardService {

	@Autowired
	@Qualifier("NationalDayActivity01TeaAwardRepo")
	private Repo<NationalDayActivity01TeaAward, Long> nationalDayActivity01TeaAwardRepo;

	@Override
	public List<NationalDayActivity01TeaAward> getTeaAward() {
		return nationalDayActivity01TeaAwardRepo.find("$findTeaAward").list();
	}
}
