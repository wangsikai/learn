package com.lanking.uxb.service.diagnostic.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClass;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassService;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticClassServiceImpl implements DiagnosticClassService {
	@Autowired
	@Qualifier("DiagnosticClassRepo")
	private Repo<DiagnosticClass, Long> repo;

	@Override
	public DiagnosticClass getByTextbook(int textbookCode, long classId) {
		return repo.find("$getByTextbook", Params.param("code", textbookCode).put("classId", classId)).get();
	}
}
