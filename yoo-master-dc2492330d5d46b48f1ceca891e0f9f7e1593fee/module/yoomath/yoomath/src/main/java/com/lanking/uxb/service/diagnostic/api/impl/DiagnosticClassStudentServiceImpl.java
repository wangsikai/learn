package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassStudent;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassStudentService;

/**
 * @see DiagnosticClassStudentService
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticClassStudentServiceImpl implements DiagnosticClassStudentService {
	@Autowired
	@Qualifier("DiagnosticClassStudentRepo")
	private Repo<DiagnosticClassStudent, Long> repo;

	@Override
	public Page<DiagnosticClassStudent> query(Pageable pageable, int day0, int orderBy, long classId) {
		return repo.find("$query", Params.param("day0", day0).put("orderBy", orderBy).put("classId", classId)).fetch(
				pageable);
	}

	@Override
	public List<DiagnosticClassStudent> query(int day0, int orderBy, long classId) {
		return repo.find("$query", Params.param("day0", day0).put("orderBy", orderBy).put("classId", classId)).list();
	}
}
