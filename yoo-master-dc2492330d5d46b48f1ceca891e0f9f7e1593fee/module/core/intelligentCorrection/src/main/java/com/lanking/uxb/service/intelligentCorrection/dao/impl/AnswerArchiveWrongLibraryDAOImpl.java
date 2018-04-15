package com.lanking.uxb.service.intelligentCorrection.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.base.intelligentCorrection.AnswerArchiveWrongLibrary;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.intelligentCorrection.dao.AnswerArchiveWrongLibraryDAO;

@Component
public class AnswerArchiveWrongLibraryDAOImpl extends AbstractHibernateDAO<AnswerArchiveWrongLibrary, Long>
		implements AnswerArchiveWrongLibraryDAO {

	@Override
	public void delete(long answerId, String content) {
		repo.execute("$delete", Params.param("answerId", answerId).put("content", content));
	}

	@Override
	public int update(long answerId, String content) {
		return repo.execute("$updateTimes", Params.param("answerId", answerId).put("content", content));
	}

	@Autowired
	@Qualifier("AnswerArchiveWrongLibraryRepo")
	@Override
	public void setRepo(Repo<AnswerArchiveWrongLibrary, Long> repo) {
		this.repo = repo;
	}

}
