package com.lanking.stuWeekReport.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.common.baseData.Section;

@Component(value = "stuWeekReportSectionDAO")
public class SectionDAOImpl extends AbstractHibernateDAO<Section, Long> implements com.lanking.stuWeekReport.dao.SectionDAO {

	@Autowired
	@Qualifier("SectionRepo")
	@Override
	public void setRepo(Repo<Section, Long> repo) {
		this.repo = repo;
	}

}
