package com.lanking.uxb.rescon.teach.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalogElement;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistCatalogElementService;

/**
 * @see ResconTeachAssistCatalogElementService
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Service
@Transactional(readOnly = true)
public class ResconTeachAssistCatalogElementServiceImpl implements ResconTeachAssistCatalogElementService {
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	private Repo<TeachAssistCatalogElement, Long> catalogElementRepo;

	@Override
	public List<TeachAssistCatalogElement> findByCatalog(long catalogId) {
		return catalogElementRepo.find("$findByCatalog", Params.param("catalogId", catalogId)).list();
	}

	@Override
	@Transactional
	public void updateSequence(long elementId, long catalogId, int sequence) {
		catalogElementRepo.execute("$updateSequence", Params.param("elementId", elementId).put("catalogId", catalogId)
				.put("sequence", sequence));
	}

	@Override
	@Transactional
	public void deleteByElement(long elementId) {
		catalogElementRepo.execute("$deleteByElement", Params.param("elementId", elementId));
	}
}
