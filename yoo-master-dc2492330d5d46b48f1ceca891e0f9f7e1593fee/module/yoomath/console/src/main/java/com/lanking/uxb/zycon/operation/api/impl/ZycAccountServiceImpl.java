package com.lanking.uxb.zycon.operation.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZycAccountServiceImpl implements ZycAccountService {

	@Autowired
	@Qualifier("AccountRepo")
	Repo<Account, Long> accountRepo;

	@Override
	public Account getAccount(long accountId) {
		return accountRepo.get(accountId);
	}

	@Override
	public Map<Long, Account> mgeAccount(Collection<Long> accountIds) {
		return accountRepo.mget(accountIds);
	}

	@Transactional
	@Override
	public void forbiddenAccount(Long accountId) {
		accountRepo.execute("$setAccForbidden", Params.param("accountId", accountId));

	}

	@Override
	public Account getAccountByUserId(long userId) {
		return accountRepo.find("$zycGetByUserId", Params.param("userId", userId)).get();
	}

	@Override
	public Map<Long, Account> mgetByUserId(Collection<Long> userIds) {
		List<Map> list = accountRepo.find("$zycGetByUserIds", Params.param("userIds", userIds)).list(Map.class);
		Map<Long, Account> map = Maps.newHashMap();
		for (Map m : list) {
			Account account = new Account();
			account.setMobile((String) m.get("mobile"));
			account.setEmail((String) m.get("email"));
			account.setName((String) m.get("name"));
			map.put(Long.parseLong(m.get("id").toString()), account);
		}
		return map;
	}
}
