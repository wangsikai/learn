package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClass;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassService;

@Service
@Transactional(readOnly = true)
public class DiagnosticStudentClassServiceImpl implements DiagnosticStudentClassService {
	@Autowired
	@Qualifier("DiagnosticStudentClassRepo")
	private Repo<DiagnosticStudentClass, Long> repo;

	@Override
	public DiagnosticStudentClass queryListByTextbookCode(Long textbookCode, Long studentId, Long classId) {
		Params params = Params.param("studentId", studentId).put("classId", classId).put("textbookCode", textbookCode);
		return repo.find("$queryListByTextbookCode", params).get();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getClassAvgDoQuestion(Long textbookCode, Long classId) {
		return repo.find("$getClassAvgDoQuestion", Params.param("classId", classId).put("textbookCode", textbookCode))
				.get(Map.class);
	}

}
