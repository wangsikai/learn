package com.lanking.uxb.zycon.operation.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceSession;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.operation.api.ZycYoomathCustomerServiceSessionService;

/**
 * @see ZycYoomathCustomerServiceSessionService
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
@Service
@Transactional(readOnly = true)
public class ZycYoomathCustomerServiceSessionServiceImpl implements ZycYoomathCustomerServiceSessionService {

	@Autowired
	@Qualifier("YoomathCustomerServiceSessionRepo")
	private Repo<YoomathCustomerServiceSession, Long> repo;

	@Override
	public List<YoomathCustomerServiceSession> findAll() {
		return repo.find("$zycFindAll", Params.param()).list();
	}

	@Override
	public YoomathCustomerServiceSession getByUser(long userId) {
		return repo.find("$zycGetByUser", Params.param("userId", userId)).get();
	}

	@Override
	@Transactional
	public YoomathCustomerServiceSession update(long userId, Status status) {
		YoomathCustomerServiceSession yoomathCustomerServiceSession = repo.find("$zycGetByUser",
				Params.param("userId", userId)).get();
		if (yoomathCustomerServiceSession != null) {
			yoomathCustomerServiceSession.setStatus(status);
			return repo.save(yoomathCustomerServiceSession);
		}

		return null;
	}

}
