package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeSync;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSyncService;

/**
 * 同步知识点相关接口实现.
 * 
 * @author wlche
 *
 */
@Transactional(readOnly = true)
@Service
public class ResconKnowledgeSyncServiceImpl implements ResconKnowledgeSyncService {
	@Autowired
	@Qualifier("KnowledgeSyncRepo")
	private Repo<KnowledgeSync, Long> ksRepo;

	@Override
	public List<KnowledgeSync> findAll(int phaseCode, int subjectCode, Status status) {
		Params params = Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode);
		if (status != null) {
			params.put("status", status.getValue());
		}
		return ksRepo.find("$findAll", params).list();
	}
}
