package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class KnowledgePointCardServiceImpl implements KnowledgePointCardService {

	@Autowired
	@Qualifier("KnowledgePointCardRepo")
	private Repo<KnowledgePointCard, Long> knowledgePointCardRepo;

	@Override
	public KnowledgePointCard get(long id) {
		return knowledgePointCardRepo.get(id);
	}

	@Override
	public List<KnowledgePointCard> findByKnowledgePoint(long knowledgePointCode) {
		return knowledgePointCardRepo
				.find("$findByKnowledgePoint", Params.param("knowledgePointCode", knowledgePointCode)).list();
	}

	@SuppressWarnings({"rawtypes", "unused"})
	@Override
	public Map<Long, Long> statisByPoints(Collection<Long> codes) {
		List<Map> list = knowledgePointCardRepo.find("$statisByPoints", Params.param("codes", codes)).list(Map.class);
		Map<Long, Long> data = new HashMap<Long, Long>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map map : list) {
				Long knowpoint_code = Long.parseLong(map.get("knowpoint_code").toString());
				Long count = Long.parseLong(map.get("count").toString());
				data.put(knowpoint_code, count);
			}
		}
		return data;
	}

	@Override
	public List<Long> findHasCardKnowledgePoint(Collection<Long> codes) {
		if (CollectionUtils.isEmpty(codes)) {
			return Collections.EMPTY_LIST;
		}
		return knowledgePointCardRepo.find("$findKnowledgePoint", Params.param("codes", codes)).list(Long.class);
	}

	@Override
	public KnowledgePointCard getFirstKnowpointCardByPhaseCode(Integer phaseCode) {
		return knowledgePointCardRepo.find("$getFirstKnowpointCard", Params.param("phaseCode", phaseCode)).get();
	}

	@Override
	public List<KnowledgePointCard> findByKnowledgePoints(Collection<Long> knowledgePointCodes) {
		if (CollectionUtils.isEmpty(knowledgePointCodes)) {
			return Collections.EMPTY_LIST;
		}
		return knowledgePointCardRepo.find("$findByKnowledgePoints", Params.param("codes", knowledgePointCodes)).list();
	}

	@Override
	public List<KnowledgePointCard> findBySubject(int subjectCode) {
		return knowledgePointCardRepo.find("$findBySubject", Params.param("subjectCode", subjectCode)).list();
	}
}
