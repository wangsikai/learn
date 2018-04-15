package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassTextbook;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassTextbookService;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional
public class DiagnosticClassTextbookServiceImpl implements DiagnosticClassTextbookService {
	@Autowired
	@Qualifier("DiagnosticClassTextbookRepo")
	private Repo<DiagnosticClassTextbook, Long> repo;

	@Override
	public List<Integer> getClassTextbooks(long classId, int textbookCategory) {
		return repo
				.find("$getClassTextbooks", Params.param("classId", classId).put("category", textbookCategory + "%"))
				.list(Integer.class);
	}

	@Override
	public List<Integer> getClassSortTextbooks(long classId, int textbookCategory) {
		return repo.find("$getClassSortTextbooks",
				Params.param("classId", classId).put("category", textbookCategory + "%")).list(Integer.class);
	}
}
