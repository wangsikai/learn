package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassTextbook;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassTextbookService;

/**
 * 学生诊断对应的教材
 * 
 * @author wangsenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticStudentClassTextbookServiceImpl implements DiagnosticStudentClassTextbookService {
	@Autowired
	@Qualifier("DiagnosticStudentClassTextbookRepo")
	private Repo<DiagnosticStudentClassTextbook, Long> repo;

	@Override
	public List<Integer> queryTextBookList(Long classId, Long studentId, boolean sort, Integer categoryCode) {
		Params params = Params.param("classId", classId).put("studentId", studentId);
		if (sort) {
			params.put("sort", 1);
		}
		if (categoryCode != null) {
			params.put("categoryCode", categoryCode + "%");
		}
		return repo.find("$queryTextBookList", params).list(Integer.class);
	}
}
