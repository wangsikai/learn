package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomeworkKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassLatestHomeworkKnowpointService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassLatestHomeworkKnowpointConvert;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomeworkKnowpoint;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticClassLatestHomeworkKnowpointServiceImpl implements DiagnosticClassLatestHomeworkKnowpointService {
	@Autowired
	@Qualifier("DiagnosticClassLatestHomeworkKnowpointRepo")
	private Repo<DiagnosticClassLatestHomeworkKnowpoint, Long> repo;

	@Autowired
	private DiagnosticClassLatestHomeworkKnowpointConvert kpConvert;
	@Autowired
	private KnowledgePointCardService knowledgePointCardService;

	@Override
	public List<DiagnosticClassLatestHomeworkKnowpoint> findByPage(long classId, int times, int kpCount) {
		return repo.find("$findByClass", Params.param("classId", classId).put("times", times).put("kpCount", kpCount))
				.list();
	}

	@Override
	public Map<Long, List<VDiagnosticClassLatestHomeworkKnowpoint>> findByClassIds(List<Long> classIds, int times) {
		List<DiagnosticClassLatestHomeworkKnowpoint> list = new ArrayList<DiagnosticClassLatestHomeworkKnowpoint>();
		for (Long classId : classIds) {
			list.addAll(this.findByPage(classId, times, 4));
		}
		List<VDiagnosticClassLatestHomeworkKnowpoint> vlist = kpConvert.to(list);
		List<Long> codes = new ArrayList<Long>();
		for (VDiagnosticClassLatestHomeworkKnowpoint v : vlist) {
			codes.add(v.getCode());
		}
		List<Long> kcodes = knowledgePointCardService.findHasCardKnowledgePoint(codes);
		Map<Long, List<VDiagnosticClassLatestHomeworkKnowpoint>> interMap = new HashMap<Long, List<VDiagnosticClassLatestHomeworkKnowpoint>>();
		for (VDiagnosticClassLatestHomeworkKnowpoint v : vlist) {
			if (kcodes.contains(v.getCode())) {
				v.setHasCard(true);
			}
			List<VDiagnosticClassLatestHomeworkKnowpoint> temp = interMap.get(v.getClassId());
			if (temp == null) {
				temp = new ArrayList<VDiagnosticClassLatestHomeworkKnowpoint>();
				interMap.put(v.getClassId(), temp);
			}
			temp.add(v);
		}
		return interMap;
	}

	@Override
	public List<DiagnosticClassLatestHomeworkKnowpoint> findByCodes(long classId, List<Long> codes) {
		Params params = Params.param("classId", classId);
		if (CollectionUtils.isNotEmpty(codes)) {
			params.put("codes", codes);
		}
		return repo.find("$findByCodes", params).list();
	}
}
