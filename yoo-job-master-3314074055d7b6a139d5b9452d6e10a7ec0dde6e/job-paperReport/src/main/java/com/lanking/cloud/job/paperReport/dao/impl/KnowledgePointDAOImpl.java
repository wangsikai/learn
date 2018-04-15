package com.lanking.cloud.job.paperReport.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.job.paperReport.dao.KnowledgePointDAO;

@Component(value = "knowledgePointDAO")
public class KnowledgePointDAOImpl extends AbstractHibernateDAO<KnowledgePoint, Long> implements KnowledgePointDAO {

	@Autowired
	@Qualifier("KnowledgePointRepo")
	@Override
	public void setRepo(Repo<KnowledgePoint, Long> repo) {
		this.repo = repo;
	}
}
