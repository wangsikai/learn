package com.lanking.uxb.service.base.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.version.YoomathClientVersion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.base.api.YoomathClientVersionService;

@Transactional(readOnly = true)
@Service
public class YoomathClientVersionServiceImpl implements YoomathClientVersionService {

	@Autowired
	@Qualifier("YoomathClientVersionRepo")
	private Repo<YoomathClientVersion, Long> repo;

	@Override
	public List<YoomathClientVersion> findUpVersions(int version, YooApp app, DeviceType deviceType) {
		return repo.find("$zyFindUpVersions",
				Params.param("version", version).put("app", app.getValue()).put("deviceType", deviceType.getValue()))
				.list();
	}
}
