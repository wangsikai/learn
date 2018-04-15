package com.lanking.cloud.job.doQuestionRank.DAO.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.job.doQuestionRank.DAO.StudentQuestionAnswerDAO;
import com.lanking.cloud.sdk.data.Params;

@Component
public class StudentQuestionAnswerDAOImpl extends AbstractHibernateDAO<StudentQuestionAnswer, Long>
		implements StudentQuestionAnswerDAO {

	@Override
	public List<StudentQuestionAnswer> taskStaticDoQuestionStudent(Date startDate, Date endDate, List<Long> userIds,
			HomeworkAnswerResult result) {
		Params param = Params.param();
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		param.put("userIds", userIds);
		param.put("result", result.getValue());

		return repo.find("$taskStaticDoQuestionStudent", param).list();
	}

	@Autowired
	@Qualifier("StudentQuestionAnswerRepo")
	@Override
	public void setRepo(Repo<StudentQuestionAnswer, Long> repo) {
		this.repo = repo;
	}

}
