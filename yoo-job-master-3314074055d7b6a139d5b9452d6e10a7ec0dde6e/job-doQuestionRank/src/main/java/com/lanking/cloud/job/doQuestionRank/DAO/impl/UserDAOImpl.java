package com.lanking.cloud.job.doQuestionRank.DAO.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.job.doQuestionRank.DAO.UserDAO;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

@Component(value = "doQuestionRankUserDAO")
public class UserDAOImpl extends AbstractHibernateDAO<User, Long> implements UserDAO {

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
	@Qualifier("UserRepo")
	@Override
	public void setRepo(Repo<User, Long> repo) {
		this.repo = repo;
	}
}
