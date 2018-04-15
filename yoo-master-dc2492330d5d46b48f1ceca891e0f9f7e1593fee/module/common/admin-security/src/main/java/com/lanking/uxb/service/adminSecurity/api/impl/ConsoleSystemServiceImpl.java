package com.lanking.uxb.service.adminSecurity.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.common.auth.ConsoleSystem;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleSystemService;
import com.lanking.uxb.service.adminSecurity.form.ConsoleSystemForm;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ConsoleSystemServiceImpl implements ConsoleSystemService {
	@Autowired
	@Qualifier("ConsoleSystemRepo")
	private Repo<ConsoleSystem, Long> repo;

	@Override
	@Transactional(readOnly = false)
	public ConsoleSystem save(ConsoleSystemForm form) {
		ConsoleSystem consoleSystem = new ConsoleSystem();
		consoleSystem.setName(form.getName());
		consoleSystem.setStatus(Status.ENABLED);
		consoleSystem.setCreateAt(new Date());
		return repo.save(consoleSystem);
	}

	@Override
	public ConsoleSystem get(Long id) {
		return repo.get(id);
	}

	@Override
	public List<ConsoleSystem> findAll() {
		return repo.find("$findAll", Params.param()).list();
	}

	@Override
	public List<ConsoleSystem> mgetList(Collection<Long> systemId) {
		if (CollectionUtils.isEmpty(systemId))
			return Lists.newArrayList();
		return repo.mgetList(systemId);
	}

	@Override
	@Transactional
	public ConsoleSystem updateStatus(long id, Status status) {
		ConsoleSystem system = repo.get(id);
		if (system != null) {
			system.setStatus(status);
			repo.save(system);
		}

		return system;
	}

	@Override
	public Map<Long, ConsoleSystem> mget(Collection<Long> systemIds) {
		return repo.mget(systemIds);
	}
}
