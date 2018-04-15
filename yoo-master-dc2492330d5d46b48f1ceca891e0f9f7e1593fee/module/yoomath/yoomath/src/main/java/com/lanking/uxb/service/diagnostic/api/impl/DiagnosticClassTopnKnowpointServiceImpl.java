package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassTopnKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassTopnKnowpointService;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticClassTopnKnowpointServiceImpl implements DiagnosticClassTopnKnowpointService {
	@Autowired
	@Qualifier("DiagnosticClassTopnKnowpointRepo")
	private Repo<DiagnosticClassTopnKnowpoint, Long> repo;

	@Override
	public Map<Long, DiagnosticClassTopnKnowpoint> query(long classId, Collection<Long> codes) {
		List<DiagnosticClassTopnKnowpoint> topnKnowpointList = repo.find("$queryByClass",
				Params.param("classId", classId).put("codes", codes)).list();
		Map<Long, DiagnosticClassTopnKnowpoint> retMap = new HashMap<Long, DiagnosticClassTopnKnowpoint>(
				topnKnowpointList.size());

		for (DiagnosticClassTopnKnowpoint p : topnKnowpointList) {
			retMap.put(p.getKnowpointCode(), p);
		}
		return retMap;
	}
}
