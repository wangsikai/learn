package com.lanking.uxb.service.diagnostic.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.Diagnostic;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.DiagnosticService;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticServiceImpl implements DiagnosticService {
	@Autowired
	@Qualifier("DiagnosticRepo")
	private Repo<Diagnostic, Long> repo;

	@Override
	public Diagnostic findByTextbook(int textbookCode) {
		return repo.find("$getByTextbook", Params.param("code", textbookCode)).get();
	}
}
