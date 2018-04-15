package com.lanking.uxb.service.sys.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.common.EmbeddedApp;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.sys.api.EmbeddedAppQuery;
import com.lanking.uxb.service.sys.api.EmbeddedAppService;

@Transactional(readOnly = true)
@Service
public class EmbeddedAppServiceImpl implements EmbeddedAppService {
	@Autowired
	@Qualifier("EmbeddedAppRepo")
	private Repo<EmbeddedApp, Long> embeddedAppRepo;

	@Override
	public List<EmbeddedApp> list(EmbeddedAppQuery query) {
		Params params = Params.param("location", query.getLocation().getValue());
		if (query.getApp() == null) {
			params.put("app", -1);
		} else {
			params.put("app", query.getApp().getValue());
		}
		return embeddedAppRepo.find("$list", params).list();
	}
}
