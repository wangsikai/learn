package com.lanking.uxb.service.user.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.AccountPasswordQuestion;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.user.api.AccountPasswordQuestionService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.UserService;

@Service
@Transactional(readOnly = true)
public class AccountPasswordQuestionServiceImpl implements AccountPasswordQuestionService {
	@Autowired
	@Qualifier("AccountPasswordQuestionRepo")
	Repo<AccountPasswordQuestion, Long> apqRepo;
	@Autowired
	@Qualifier("AccountRepo")
	Repo<Account, Long> accountRepo;

	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;

	@Transactional(readOnly = true)
	@Override
	public List<AccountPasswordQuestion> findByAccountId(long accountId) {
		return apqRepo.find("$findByAccountId", Params.param("accountId", accountId)).list();
	}

	@Transactional
	@Override
	public void deleteByAccount(long accountId) {
		apqRepo.execute("$deleteByAccount", Params.param("accountId", accountId));
	}

	@Transactional
	@Override
	public void create(List<AccountPasswordQuestion> apqs) {
		deleteByAccount(apqs.get(0).getAccountId());
		Date date = new Date();
		for (AccountPasswordQuestion apq : apqs) {
			AccountPasswordQuestion p = new AccountPasswordQuestion();
			p.setAccountId(apq.getAccountId());
			p.setAnswer(apq.getAnswer());
			p.setCreateAt(date);
			p.setPasswordQuestionCode(apq.getPasswordQuestionCode());
			p.setUpdateAt(date);
			apqRepo.save(p);
		}
		User user = accountService.getUserByAccountId(apqs.get(0).getAccountId());
		// 修改是否设置密保问题
		Account account = accountService.getAccount(apqs.get(0).getAccountId());
		account.setPqStatus(Status.ENABLED);
		accountRepo.save(account);
	}
}
