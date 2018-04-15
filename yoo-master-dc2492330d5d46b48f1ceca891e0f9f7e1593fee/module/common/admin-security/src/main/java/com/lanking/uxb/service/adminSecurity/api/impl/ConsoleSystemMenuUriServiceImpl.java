package com.lanking.uxb.service.adminSecurity.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.common.auth.ConsoleSystemMenuUri;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.adminSecurity.api.ConsoleSystemMenuUriService;

@Service
@Transactional(readOnly = true)
public class ConsoleSystemMenuUriServiceImpl implements ConsoleSystemMenuUriService {

	@Autowired
	@Qualifier("ConsoleSystemMenuUriRepo")
	private Repo<ConsoleSystemMenuUri, Long> repo;

	@Override
	public List<ConsoleSystemMenuUri> queryBySystem(long systemId) {
		return repo.find("$queryBySystem", Params.param("systemId", systemId)).list();
	}

	@Transactional
	@Override
	public void addUri(long systemId, String menuUri, String description) {
		ConsoleSystemMenuUri mu = new ConsoleSystemMenuUri();
		mu.setMenuUri(menuUri);
		mu.setSystemId(systemId);
		mu.setDescription(description);
		repo.save(mu);
	}

	@Transactional
	@Override
	public void updateUri(long id, Long systemId, String menuUri, String description) {
		ConsoleSystemMenuUri mu = repo.get(id);
		if (mu != null) {
			if (systemId != null) {
				mu.setSystemId(systemId);
			}
			mu.setMenuUri(menuUri);
			mu.setDescription(description);
			repo.save(mu);
		}
	}
}
