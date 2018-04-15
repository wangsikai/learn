package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Question;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01QuestionDAO;
import com.lanking.cloud.sdk.data.Params;

@Component("nda01NationalDayActivity01QuestionDAO")
public class NationalDayActivity01QuestionDAOImpl extends AbstractHibernateDAO<NationalDayActivity01Question, Long>
		implements NationalDayActivity01QuestionDAO {

	@Autowired
	@Qualifier("NationalDayActivity01QuestionRepo")
	@Override
	public void setRepo(Repo<NationalDayActivity01Question, Long> repo) {
		this.repo = repo;
	}

	@Override
	public boolean exist(long studentId, long questionId) {
		Params params = Params.param("studentId", studentId).put("questionId", questionId);
		return repo.find("$nda01Exist", params).count() > 0;
	}

	@Override
	public NationalDayActivity01Question create(long studentId, long questionId) {
		NationalDayActivity01Question nationalDayActivit01Question = new NationalDayActivity01Question();
		nationalDayActivit01Question.setStudentId(studentId);
		nationalDayActivit01Question.setQuestionId(questionId);
		return repo.save(nationalDayActivit01Question);
	}

}
