package com.lanking.cloud.job.correctUserDayStat.DAO.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectQuestionDAO;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;

@Component(value = "StatCorrectQuestionDAO")
public class CorrectQuestionDAOImpl extends AbstractHibernateDAO<CorrectQuestion, Long> implements CorrectQuestionDAO {

	@Autowired
	@Qualifier("CorrectQuestionRepo")
	@Override
	public void setRepo(Repo<CorrectQuestion, Long> repo) {
		this.repo = repo;
	}

	@Override
	public List<CorrectQuestion> getCompleteQuestionsByUserId(Long userId, Date date, Date dateEnd) {
		Params params = Params.param();
		params.put("userId", userId);
		if (date != null) {
			params.put("datefrom", date);
		}
		if (dateEnd != null) {
			params.put("dateEnd", dateEnd);
		}
		
		return repo.find("$queryCompleteQuestionsByUserId", params).list();
	}

}
