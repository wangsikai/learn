package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Phase;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.PhaseService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class PhaseServiceImpl implements PhaseService {

	@Autowired
	@Qualifier("PhaseRepo")
	private Repo<Phase, Long> phaseRepo;

	@Override
	public Phase get(int code) {
		return phaseRepo.find("$getPhaseByCode", Params.param("code", code)).get();
	}

	@Override
	public List<Phase> getAll() {
		return phaseRepo.find("$findAllPhase").list();
	}

	@Override
	public Map<Integer, Phase> mgetAll() {
		List<Phase> phaseList = phaseRepo.find("$findAllPhase").list();
		Map<Integer, Phase> phaseMap = Maps.newHashMap();
		for (Phase phase : phaseList) {
			phaseMap.put(phase.getCode(), phase);
		}
		return phaseMap;
	}

	@Override
	public Map<Integer, Phase> mget(Collection<Integer> codes) {
		List<Phase> phaseList = phaseRepo.find("$getPhaseByCode", Params.param("codes", codes)).list();
		Map<Integer, Phase> phaseMap = new HashMap<Integer, Phase>();
		for (Phase phase : phaseList) {
			phaseMap.put(phase.getCode(), phase);
		}
		return phaseMap;
	}

}
