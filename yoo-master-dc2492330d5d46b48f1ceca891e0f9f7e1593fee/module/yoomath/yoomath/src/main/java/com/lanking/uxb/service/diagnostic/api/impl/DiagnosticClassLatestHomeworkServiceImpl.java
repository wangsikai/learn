package com.lanking.uxb.service.diagnostic.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassLatestHomework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassLatestHomeworkConvert;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomework;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticClassLatestHomeworkServiceImpl implements DiagnosticClassLatestHomeworkService {
	@Autowired
	@Qualifier("DiagnosticClassLatestHomeworkRepo")
	private Repo<DiagnosticClassLatestHomework, Long> repo;
	@Autowired
	private DiagnosticClassLatestHomeworkConvert latestHkConvert;

	@Override
	public List<DiagnosticClassLatestHomework> findByClassId(long classId, int times) {
		return repo.find("$findByClassId", Params.param("classId", classId).put("times", times)).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Double> getSubmitRate(List<Long> homeworkIds) {
		Map<Long, Double> map = new HashMap<Long, Double>();
		if (CollectionUtils.isNotEmpty(homeworkIds)) {
			List<Map> list = repo.find("$getSubmitRate", Params.param("homeworkIds", homeworkIds)).list(Map.class);
			for (Map m : list) {
				Long homworkId = Long.parseLong(String.valueOf(m.get("id")));
				Double submitRate = Double.parseDouble(String.valueOf(m.get("submitrate") == null ? 0 : m
						.get("submitrate")));
				map.put(homworkId, submitRate);
			}
		}
		return map;
	}

	@Override
	public Map<Long, List<VDiagnosticClassLatestHomework>> findByClassIds(List<Long> classIds, int times) {
		List<DiagnosticClassLatestHomework> list = new ArrayList<DiagnosticClassLatestHomework>();
		for (Long classId : classIds) {
			list.addAll(this.findByClassId(classId, times));
		}
		List<Long> homeworkIds = new ArrayList<Long>();
		for (DiagnosticClassLatestHomework hk : list) {
			homeworkIds.add(hk.getHomeworkId());
		}
		Map<Long, Double> map = this.getSubmitRate(homeworkIds);
		List<VDiagnosticClassLatestHomework> vlist = latestHkConvert.to(list);
		Map<Long, List<VDiagnosticClassLatestHomework>> interMap = new HashMap<Long, List<VDiagnosticClassLatestHomework>>();
		for (VDiagnosticClassLatestHomework v : vlist) {
			v.setSubmitRate(BigDecimal.valueOf(map.get(v.getHomeworkId())));
			List<VDiagnosticClassLatestHomework> temp = interMap.get(v.getClassId());
			if (temp == null) {
				temp = new ArrayList<VDiagnosticClassLatestHomework>();
				interMap.put(v.getClassId(), temp);
			}
			temp.add(v);
		}
		return interMap;
	}
}
