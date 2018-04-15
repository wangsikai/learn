package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeReview;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeReviewService;

/**
 * 复习知识点接口实现.
 * 
 * @author wlche
 *
 */
@Transactional(readOnly = true)
@Service
public class ResconKnowledgeReviewServiceImpl implements ResconKnowledgeReviewService {
	@Autowired
	@Qualifier("KnowledgeReviewRepo")
	private Repo<KnowledgeReview, Long> krRepo;

	@Override
	public List<KnowledgeReview> findAll(int phaseCode, int subjectCode, Status status) {
		Params params = Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode);
		if (status != null) {
			params.put("status", status.getValue());
		}
		return krRepo.find("$findAll", params).list();
	}

}
