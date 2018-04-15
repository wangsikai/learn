package com.lanking.cloud.job.correctUserDayStat.DAO.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectUserDAO;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;

@Component(value = "UserDayStatUserDAO")
public class CorrectUserDAOImpl extends AbstractHibernateDAO<CorrectUser, Long> implements CorrectUserDAO {

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> queryUserId(CursorPageable<Long> pageable) {
		return repo.find("$taskQueryUser").fetch(pageable, Map.class, new CursorGetter<Long, Map>() {
			@Override
			public Long getCursor(Map bean) {
				return Long.parseLong(String.valueOf(bean.get("id")));
			}
		});
	}

	@Autowired
	@Qualifier("CorrectUserRepo")
	@Override
	public void setRepo(Repo<CorrectUser, Long> repo) {
		this.repo = repo;
	}
}
