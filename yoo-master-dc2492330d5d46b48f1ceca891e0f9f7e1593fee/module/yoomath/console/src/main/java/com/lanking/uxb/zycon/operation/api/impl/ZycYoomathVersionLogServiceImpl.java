package com.lanking.uxb.zycon.operation.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.version.YoomathVersionLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.operation.api.ZycYoomathVersionLogService;
import com.lanking.uxb.zycon.operation.form.VersionForm;

@Transactional(readOnly = true)
@Service
public class ZycYoomathVersionLogServiceImpl implements ZycYoomathVersionLogService {

	@Autowired
	@Qualifier("YoomathVersionLogRepo")
	Repo<YoomathVersionLog, Long> versionLogRepo;

	@Transactional
	@Override
	public YoomathVersionLog add(VersionForm form) {
		YoomathVersionLog versionLog = null;
		if (form.getId() == null) {
			versionLog = new YoomathVersionLog();
			versionLog.setCreateAt(new Date());
		} else {
			versionLog = versionLogRepo.get(form.getId());
		}
		versionLog.setDescription(form.getDescription());
		versionLog.setPublishAt(new Date(form.getPublishDate()));
		versionLog.setStatus(versionLog == null ? Status.DISABLED : versionLog.getStatus());
		versionLog.setUpdateAt(new Date());
		versionLog.setVersion(form.getVersion());
		return versionLogRepo.save(versionLog);
	}

	@Transactional
	@Override
	public YoomathVersionLog edit(Long id, Status status) {
		YoomathVersionLog versionLog = versionLogRepo.get(id);
		versionLog.setStatus(status);
		return versionLogRepo.save(versionLog);
	}

	@Override
	public Page<YoomathVersionLog> query(Pageable index) {
		return versionLogRepo.find("$versionQuery").fetch(index);
	}

}
