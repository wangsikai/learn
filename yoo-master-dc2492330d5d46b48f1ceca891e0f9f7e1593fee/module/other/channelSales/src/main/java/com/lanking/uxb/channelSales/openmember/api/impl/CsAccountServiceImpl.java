package com.lanking.uxb.channelSales.openmember.api.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.openmember.api.CsAccountService;

/**
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Service
@Transactional(readOnly = true)
public class CsAccountServiceImpl implements CsAccountService {
	@Autowired
	@Qualifier("AccountRepo")
	private Repo<Account, Long> accountRepo;

	@Override
	public Account get(long id) {
		return accountRepo.get(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, Account> mget(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_MAP;
		}
		return accountRepo.mget(ids);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, Account> mgetByNames(Collection<String> names) {
		if (CollectionUtils.isEmpty(names)) {
			return Collections.EMPTY_MAP;
		}
		List<Account> accounts = accountRepo.find("$csFindByNames", Params.param("names", names)).list();
		Map<Long, Account> retMap = new HashMap<Long, Account>(accounts.size());
		for (Account a : accounts) {
			retMap.put(a.getId(), a);
		}
		return retMap;
	}
}
