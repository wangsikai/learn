package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.job.nationalDayActivity.DAO.StudentHomeworkDAO;
import com.lanking.cloud.sdk.data.Params;

@Component("nda01StudentHomeworkDAO")
public class StudentHomeworkDAOImpl extends AbstractHibernateDAO<StudentHomework, Long> implements StudentHomeworkDAO {

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	@Override
	public void setRepo(Repo<StudentHomework, Long> repo) {
		this.repo = repo;
	}

	@Override
	public List<StudentHomework> getSubmitedIssuedHomework(List<Long> homeworkIds, Date startTime, Date endTime) {
		Params params = Params.param();
		params.put("homeworkIds", homeworkIds);
		params.put("startTime", startTime);
		params.put("endTime", endTime);

		return repo.find("$nda01findHomeworkByHomeworkId", params).list();
	}

}
