package com.lanking.cloud.job.stuWeekReport.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.job.stuWeekReport.dao.HomeworkStudentClazzDAO;
import com.lanking.cloud.sdk.data.Params;

@Component(value = "stuWeekReportHomeworkStudentClazzDAO")
public class HomeworkStudentClazzDAOImpl extends AbstractHibernateDAO<HomeworkStudentClazz, Long> implements
		HomeworkStudentClazzDAO {

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	@Override
	public void setRepo(Repo<HomeworkStudentClazz, Long> repo) {
		this.repo = repo;
	}

	@Override
	public List<Long> listCurrentClazzsHasTeacher(long studentId) {
		return repo.find("$listCurrentClazzsHasTeacher", Params.param("studentId", studentId)).list(Long.class);
	}

}
