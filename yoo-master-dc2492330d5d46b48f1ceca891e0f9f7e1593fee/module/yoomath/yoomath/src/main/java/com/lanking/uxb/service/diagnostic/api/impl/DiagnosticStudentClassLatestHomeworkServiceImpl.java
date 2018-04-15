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
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassLatestHomework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticStudentClassLatestHomeworkConvert;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticStudentClassLatestHomework;

@Service
@Transactional(readOnly = true)
public class DiagnosticStudentClassLatestHomeworkServiceImpl implements DiagnosticStudentClassLatestHomeworkService {
	@Autowired
	@Qualifier("DiagnosticStudentClassLatestHomeworkRepo")
	private Repo<DiagnosticStudentClassLatestHomework, Long> repo;

	@Autowired
	private DiagnosticStudentClassLatestHomeworkConvert stuhkConvert;

	@Override
	public List<DiagnosticStudentClassLatestHomework> queryStat(Long studentId, Long classId, int times) {
		return repo
				.find("$queryStat", Params.param("studentId", studentId).put("classId", classId).put("times", times))
				.list();
	}

	@Override
	public Map<Long, List<VDiagnosticStudentClassLatestHomework>> findByClassIds(Long studentId, List<Long> classIds,
			int times) {
		List<DiagnosticStudentClassLatestHomework> list = new ArrayList<DiagnosticStudentClassLatestHomework>();
		for (Long classId : classIds) {
			list.addAll(this.queryStat(studentId, classId, times));
		}
		List<VDiagnosticStudentClassLatestHomework> vlist = stuhkConvert.to(list);
		Map<Long, List<VDiagnosticStudentClassLatestHomework>> interMap = new HashMap<Long, List<VDiagnosticStudentClassLatestHomework>>();
		for (VDiagnosticStudentClassLatestHomework v : vlist) {
			List<VDiagnosticStudentClassLatestHomework> temp = interMap.get(v.getClassId());
			if (temp == null) {
				temp = new ArrayList<VDiagnosticStudentClassLatestHomework>();
				interMap.put(v.getClassId(), temp);
			}
			temp.add(v);
		}
		return interMap;
	}
}
