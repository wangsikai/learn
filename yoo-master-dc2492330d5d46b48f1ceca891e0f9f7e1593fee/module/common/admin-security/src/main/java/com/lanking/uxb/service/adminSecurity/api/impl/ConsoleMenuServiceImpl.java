package com.lanking.uxb.service.adminSecurity.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.common.auth.ConsoleMenuEntity;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.adminSecurity.api.ConsoleMenuService;
import com.lanking.uxb.service.adminSecurity.form.ConsoleMenuForm;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ConsoleMenuServiceImpl implements ConsoleMenuService {
	@Autowired
	@Qualifier("ConsoleMenuEntityRepo")
	private Repo<ConsoleMenuEntity, Long> repo;

	@Override
	@Transactional(readOnly = false)
	public ConsoleMenuEntity save(ConsoleMenuForm form) {
		ConsoleMenuEntity consoleMenuEntity = null;
		if (form.getId() == null) {
			consoleMenuEntity = new ConsoleMenuEntity();
			consoleMenuEntity.setpId(form.getpId());
			consoleMenuEntity.setStatus(Status.ENABLED);
			consoleMenuEntity.setSystemId(form.getSystemId());
			consoleMenuEntity.setCreateAt(new Date());
		} else {
			consoleMenuEntity = repo.get(form.getId());
			consoleMenuEntity.setUpdateAt(new Date());
		}
		consoleMenuEntity.setName(form.getName());
		consoleMenuEntity.setUrl(form.getUrl());
		return repo.save(consoleMenuEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public ConsoleMenuEntity update(ConsoleMenuForm form) {
		ConsoleMenuEntity consoleMenuEntity = repo.get(form.getId());
		consoleMenuEntity.setName(form.getName());
		consoleMenuEntity.setUpdateAt(new Date());
		consoleMenuEntity.setUrl(form.getUrl());
		return repo.save(consoleMenuEntity);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Long id) {
		ConsoleMenuEntity consoleMenuEntity = repo.get(id);
		consoleMenuEntity.setStatus(Status.DELETED);
	}

	@Override
	public List<ConsoleMenuEntity> getBySystem(Long systemId, Status status) {
		if (systemId == null) {
			return Lists.newArrayList();
		}
		Params params = Params.param("systemId", systemId);
		if (status != null) {
			params.put("status", status.getValue());
		}
		return repo.find("$getBySystem", params).list();
	}

	@Override
	public List<ConsoleMenuEntity> findByUser(Long systemId, Long userId) {
		return repo.find("$getByUser", Params.param("userId", userId).put("systemId", systemId)).list();
	}

	@Override
	@Transactional
	public void updateStatus(Collection<Long> ids, Status status) {
		if (CollectionUtils.isNotEmpty(ids)) {
			repo.execute("$updateStatus", Params.param("ids", ids).put("status", status.getValue()));
		}
	}
}
