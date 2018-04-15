package com.lanking.cloud.handwriting.DAO.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.base.hw.HandwritingRecord;
import com.lanking.cloud.handwriting.DAO.HandwritingRecordDAO;
import com.lanking.cloud.sdk.data.Params;

@Component("hwProxyHandwritingRecordDAO")
public class HandwritingRecordDAOImpl extends AbstractHibernateDAO<HandwritingRecord, Long>
		implements HandwritingRecordDAO {

	@Autowired
	@Qualifier("HandwritingRecordRepo")
	@Override
	public void setRepo(Repo<HandwritingRecord, Long> repo) {
		this.repo = repo;
	}

	@Override
	public void create(long id, String scgInk) {
		HandwritingRecord hwr = new HandwritingRecord();
		hwr.setId(id);
		hwr.setScgInk(scgInk);
		hwr.setRequestAt(new Date());
		repo.save(hwr);
	}

	@Override
	public void response(long id, String response) {
		repo.execute("$hwProxyResponse",
				Params.param("id", id).put("response", response).put("responseAt", new Date()));
	}

}
