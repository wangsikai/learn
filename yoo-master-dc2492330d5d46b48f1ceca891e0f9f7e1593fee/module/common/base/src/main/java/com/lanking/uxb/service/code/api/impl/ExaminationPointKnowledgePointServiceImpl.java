package com.lanking.uxb.service.code.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.ExaminationPointKnowledgePoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.ExaminationPointKnowledgePointService;

@Service
@Transactional(readOnly = true)
public class ExaminationPointKnowledgePointServiceImpl implements ExaminationPointKnowledgePointService {
	@Autowired
	@Qualifier("ExaminationPointKnowledgePointRepo")
	private Repo<ExaminationPointKnowledgePoint, Long> repo;

	@Override
	public List<ExaminationPointKnowledgePoint> findByKnowledgePoint(long knowledgePointCode) {
		return repo.find("$findByKnowledgePoint", Params.param("knowledgePointCode", knowledgePointCode)).list();
	}

	@Override
	@Transactional
	public void save(long examinationPointID, Collection<Long> knowpointCodes) {
		if (null != knowpointCodes && knowpointCodes.size() > 0) {
			repo.execute("$deleteByExaminationPointID", Params.param("examinationPointID", examinationPointID));
			Set<ExaminationPointKnowledgePoint> eps = new HashSet<ExaminationPointKnowledgePoint>(
					knowpointCodes.size());
			for (Long knowpointCode : knowpointCodes) {
				ExaminationPointKnowledgePoint ep = new ExaminationPointKnowledgePoint();
				ep.setExaminationPointId(examinationPointID);
				ep.setKnowledgePointCode(knowpointCode);
				eps.add(ep);
			}
			repo.save(eps);
		}
	}

	@Override
	public List<ExaminationPointKnowledgePoint> findByExaminationPoint(long examinationPointCode) {
		return repo.find("$findByExaminationPoint", Params.param("examinationPointCode", examinationPointCode)).list();
	}

	@Override
	public Map<Long, List<ExaminationPointKnowledgePoint>> findByExaminationPoints(
			Collection<Long> examinationPointCodes) {
		List<ExaminationPointKnowledgePoint> examinationPointKnowledgePoints = repo
				.find("$findByExaminationPoints", Params.param("examinationPointCodes", examinationPointCodes)).list();
		Map<Long, List<ExaminationPointKnowledgePoint>> map = new HashMap<Long, List<ExaminationPointKnowledgePoint>>();
		for (ExaminationPointKnowledgePoint ep : examinationPointKnowledgePoints) {
			List<ExaminationPointKnowledgePoint> eps = map.get(ep.getExaminationPointId());
			if (eps == null) {
				eps = new ArrayList<ExaminationPointKnowledgePoint>();
				map.put(ep.getExaminationPointId(), eps);
			}
			eps.add(ep);
		}
		return map;
	}
}
