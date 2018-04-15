package com.lanking.uxb.service.zuoye.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.card.KnowpointCard;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyKnowpointLearnService;

@Service
@Transactional(readOnly = true)
public class ZyKnowpointLearnServiceImpl implements ZyKnowpointLearnService {
	@Autowired
	@Qualifier("KnowpointCardRepo")
	Repo<KnowpointCard, Long> repo;

	@Override
	public KnowpointCard getByCode(long knowpointCode) {
		return repo.find("$getByCode", Params.param("knowpointCode", knowpointCode)).get();
	}

}
