package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Duty;
import com.lanking.uxb.service.code.api.DutyService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class DutyServiceImpl implements DutyService {

	@Autowired
	@Qualifier("DutyRepo")
	private Repo<Duty, Integer> dutyRepo;

	@Override
	public Duty get(Integer code) {
		return dutyRepo.get(code);
	}

	@Override
	public List<Duty> getAll() {
		return dutyRepo.find("$findAllDuty").list();
	}

	@Override
	public Map<Integer, Duty> mget(Collection<Integer> codes) {
		return dutyRepo.mget(codes);
	}

}
