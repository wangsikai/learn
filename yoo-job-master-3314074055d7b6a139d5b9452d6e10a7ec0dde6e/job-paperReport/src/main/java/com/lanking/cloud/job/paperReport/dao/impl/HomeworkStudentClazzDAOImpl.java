package com.lanking.cloud.job.paperReport.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.job.paperReport.dao.HomeworkStudentClazzDAO;
import com.lanking.cloud.sdk.data.Params;

@Component(value = "paperReportHomeworkStudentClazzDAO")
public class HomeworkStudentClazzDAOImpl extends AbstractHibernateDAO<HomeworkStudentClazz, Long> implements
		HomeworkStudentClazzDAO {

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	@Override
	public void setRepo(Repo<HomeworkStudentClazz, Long> repo) {
		this.repo = repo;
	}

	@Override
	public List<Long> findStudentIdsByClassId(Long classId) {
		return repo.find("$findStudentIdsByClassId", Params.param("classId", classId)).list(Long.class);
	}
}
