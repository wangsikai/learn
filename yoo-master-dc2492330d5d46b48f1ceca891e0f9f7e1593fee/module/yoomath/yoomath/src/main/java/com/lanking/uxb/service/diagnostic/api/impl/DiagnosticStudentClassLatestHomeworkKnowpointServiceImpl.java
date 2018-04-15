package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomeworkKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassLatestHomeworkKnowpointService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassLatestHomeworkKnowpointConvert;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassLatestHomeworkKnowpoint;

@Service
@Transactional(readOnly = true)
public class DiagnosticStudentClassLatestHomeworkKnowpointServiceImpl implements
		DiagnosticStudentClassLatestHomeworkKnowpointService {
	@Autowired
	@Qualifier("DiagnosticStudentClassLatestHomeworkKnowpointRepo")
	private Repo<DiagnosticStudentClassLatestHomeworkKnowpoint, Long> repo;
	@Autowired
	private DiagnosticStudentClassLatestHomeworkKnowpointConvert kpConvert;
	@Autowired
	private KnowledgePointCardService knowledgePointCardService;

	@Override
	public List<DiagnosticStudentClassLatestHomeworkKnowpoint> queryWeakList(Long studentId, Long classId,
			Integer times, int kpCount) {
		return repo.find(
				"$queryWeakList",
				Params.param("studentId", studentId).put("classId", classId).put("times", times)
						.put("kpCount", kpCount)).list();
	}

	@Override
	public List<DiagnosticStudentClassLatestHomeworkKnowpoint> querySamllTopicList(Collection<Long> codes,
			Long studentId, Long classId) {
		Params params = Params.param("studentId", studentId).put("classId", classId).put("codes", codes);
		return repo.find("$querySamllTopicList", params).list();
	}

	@Override
	public List<DiagnosticStudentClassLatestHomeworkKnowpoint> queryKnowledgeListByPcode(Long classId, Long studentId,
			Long pcode) {
		Params params = Params.param("pcode", pcode).put("classId", classId).put("studentId", studentId);
		// 说明是知识专项,第三层
		if (pcode.toString().length() == 8) {
			params.put("level", 3);
		}
		// 说明是小专题,第二层
		if (pcode.toString().length() == 7) {
			params.put("level", 2);
		}
		return repo.find("$queryKnowledgeListByPcode", params).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getClassAvgRightRateByPcode(Long classId, Long pcode) {
		Params params = Params.param("pcode", pcode).put("classId", classId);
		// 说明是知识专项,第三层
		if (pcode.toString().length() == 8) {
			params.put("level", 3);
		}
		// 说明是小专题,第二层
		if (pcode.toString().length() == 7) {
			params.put("level", 2);
		}
		return repo.find("$getClassAvgRightRateByPcode", params).list(Map.class);
	}

	@Override
	public List<Map> querySmallTopicClassRateList(Collection<Long> codes, Long classId) {
		Params params = Params.param("codes", codes).put("classId", classId);
		return repo.find("$querySmallTopicClassRateList", params).list(Map.class);
	}

	@Override
	public Map<Long, List<VDiagnosticStudentClassLatestHomeworkKnowpoint>> findByClassIds(Long studentId,
			List<Long> classIds, Integer times) {
		List<DiagnosticStudentClassLatestHomeworkKnowpoint> list = new ArrayList<DiagnosticStudentClassLatestHomeworkKnowpoint>();
		for (Long classId : classIds) {
			list.addAll(this.queryWeakList(studentId, classId, times, 4));
		}
		List<VDiagnosticStudentClassLatestHomeworkKnowpoint> vlist = kpConvert.to(list);
		Map<Long, List<VDiagnosticStudentClassLatestHomeworkKnowpoint>> interMap = new HashMap<Long, List<VDiagnosticStudentClassLatestHomeworkKnowpoint>>();
		List<Long> codes = new ArrayList<Long>();
		for (VDiagnosticStudentClassLatestHomeworkKnowpoint v : vlist) {
			codes.add(v.getKnowpointCode());
		}
		List<Long> kcodes = knowledgePointCardService.findHasCardKnowledgePoint(codes);
		for (VDiagnosticStudentClassLatestHomeworkKnowpoint v : vlist) {
			if (kcodes.contains(v.getKnowpointCode())) {
				v.setHasCard(true);
			}
			List<VDiagnosticStudentClassLatestHomeworkKnowpoint> temp = interMap.get(v.getClassId());
			if (temp == null) {
				temp = new ArrayList<VDiagnosticStudentClassLatestHomeworkKnowpoint>();
				interMap.put(v.getClassId(), temp);
			}
			temp.add(v);
		}
		return interMap;
	}
}
