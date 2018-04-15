package com.lanking.uxb.service.zuoye.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.version.YoomathVersionLog;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyYoomathVersionLogService;

@Transactional(readOnly = true)
@Service
public class ZyYoomathVersionLogServiceImpl implements ZyYoomathVersionLogService {
	@Autowired
	@Qualifier("YoomathVersionLogRepo")
	Repo<YoomathVersionLog, Long> versionLogRepo;

	@Override
	public List<YoomathVersionLog> latestVersionLogs(Integer limit) {
		return versionLogRepo.find("$latestVersionLog", Params.param("limit", limit)).list();
	}

}
