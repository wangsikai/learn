package com.lanking.uxb.service.intelligentCorrection.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.base.intelligentCorrection.AnswerArchive;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.intelligentCorrection.dao.AnswerArchiveDAO;

@Component
public class AnswerArchiveDAOImpl extends AbstractHibernateDAO<AnswerArchive, Long> implements AnswerArchiveDAO {

	@Override
	public AnswerArchive find(long answerId, String content) {
		return repo.find("$find", Params.param("answerId", answerId).put("content", content)).get();
	}

	@Override
	public void delete(long answerId, String content, HomeworkAnswerResult result) {
		Params params = Params.param("answerId", answerId);
		if (content != null) {
			params.put("content", content);
		}
		if (result != null) {
			params.put("result", result.getValue());
		}
		repo.execute("$delete", params);
	}

	@Autowired
	@Qualifier("AnswerArchiveRepo")
	@Override
	public void setRepo(Repo<AnswerArchive, Long> repo) {
		this.repo = repo;
	}

}
