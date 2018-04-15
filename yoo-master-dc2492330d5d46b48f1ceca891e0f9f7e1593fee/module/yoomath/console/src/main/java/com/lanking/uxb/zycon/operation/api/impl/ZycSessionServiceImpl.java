package com.lanking.uxb.zycon.operation.api.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.session.Session;
import com.lanking.cloud.domain.base.session.SessionStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.operation.api.ZycSessionService;
import com.lanking.uxb.zycon.operation.form.UserSearchForm;

@Transactional(readOnly = true)
@Service
public class ZycSessionServiceImpl implements ZycSessionService {

	@Autowired
	@Qualifier("SessionRepo")
	Repo<Session, Long> sessionRepo;

	@Override
	public Page<Session> getSessionByParams(UserSearchForm searchForm, Pageable p) {
		Params params = Params.param();
		if (searchForm.getLoginAtBegin() != null) {
			params.put("loginAtBegin", searchForm.getLoginAtBegin());
		}
		if (searchForm.getActiveAtBegin() != null) {
			params.put("activeAtBegin", searchForm.getActiveAtBegin());
		}
		if (searchForm.getLoginAtEnd() != null) {
			params.put("loginAtEnd", searchForm.getLoginAtEnd());
		}
		if (searchForm.getActiveAtEnd() != null) {
			params.put("activeAtEnd", searchForm.getActiveAtEnd());
		}
		if (searchForm.getName() != null && !searchForm.getName().isEmpty()) {
			params.put("accountName", "%" + searchForm.getName() + "%");
		}
		if (searchForm.getStatus() != null) {
			params.put("status", searchForm.getStatus().getValue());
		}
		if (searchForm.getType() != null) {
			params.put("type", searchForm.getType().getValue());
		}
		return sessionRepo.find("$getSessionList", params).fetch(p);
	}

	@Override
	public Page<Session> getSessionByUserId(List<Long> userIds, Pageable pageable, Date activeAtEnd) {
		Params params = Params.param();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -10);
		params.put("userIds", userIds);
		params.put("status", SessionStatus.ACTIVE.getValue());
		if (activeAtEnd != null) {
			params.put("activeAtEnd", activeAtEnd);
		}
		
		return sessionRepo.find("$getSessionListByUserId", params).fetch(pageable);
	}
}
