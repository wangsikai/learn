package com.lanking.uxb.channelSales.memberPackage.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.memberPackage.api.CsConsoleUserService;

/**
 *
 * @author zemin.song
 */
@Service
@Transactional(readOnly = true)
public class CsConsoleUserServiceImpl implements CsConsoleUserService {

	@Autowired
	@Qualifier("ConsoleUserRepo")
	private Repo<ConsoleUser, Long> consoleUserRepo;

	@Override
	public ConsoleUser get(long id) {
		return consoleUserRepo.get(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, ConsoleUser> mget(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_MAP;
		}
		return consoleUserRepo.mget(ids);
	}

}
