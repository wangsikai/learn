package com.lanking.uxb.zycon.operation.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.session.SessionHistory;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.zycon.operation.api.ZycSessionHistoryService;
import com.lanking.uxb.zycon.operation.form.UserSearchForm;

@Transactional(readOnly = true)
@Service
public class ZycSessionHistoryServiceImpl implements ZycSessionHistoryService {

	@Autowired
	@Qualifier("SessionHistoryRepo")
	Repo<SessionHistory, Long> sessionHistoryRepo;

	@Override
	public Page<SessionHistory> getHistoryUsers(UserSearchForm searchForm, Pageable pageable) {
		Params params = Params.param();
		if (StringUtils.isNotBlank(searchForm.getName())) {
			params.put("accountName", "%" + searchForm.getName() + "%");
		}
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
		if (searchForm.getType() != null) {
			params.put("type", searchForm.getType().getValue());
		}
		return sessionHistoryRepo.find("$getSessionHisList", params).fetch(pageable);
	}

}
