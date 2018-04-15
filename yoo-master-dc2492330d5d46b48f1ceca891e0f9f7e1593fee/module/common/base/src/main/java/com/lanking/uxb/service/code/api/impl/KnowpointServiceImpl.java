package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.KnowpointService;

@Transactional(readOnly = true)
@Service
public class KnowpointServiceImpl implements KnowpointService {

	@Autowired
	@Qualifier("KnowpointRepo")
	Repo<Knowpoint, Integer> knowpointRepo;

	@Transactional(readOnly = true)
	@Override
	public Knowpoint get(Integer code) {
		return knowpointRepo.get(code);
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Integer, Knowpoint> mget(Collection<Integer> codes) {
		return knowpointRepo.mget(codes);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Knowpoint> mgetList(Collection<Integer> codes) {
		return knowpointRepo.mgetList(codes);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Knowpoint> findByPcode(int pcode) {
		return knowpointRepo.find("$findByPcode", Params.param("pcode", pcode)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public Knowpoint getParent(int code) {
		return knowpointRepo.find("$getParent", Params.param("code", code)).get();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Knowpoint> listBySubject(Integer subjectCode) {
		return knowpointRepo.find("$listBySubject", Params.param("subjectCode", subjectCode)).list();
	}

	@Override
	public List<Knowpoint> listAllBySubject(Integer subjectCode) {
		return knowpointRepo.find("$listAllBySubject", Params.param("subjectCode", subjectCode)).list();
	}

}
