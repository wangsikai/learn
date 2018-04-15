package com.lanking.cloud.job.doQuestionRank.DAO.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.job.doQuestionRank.DAO.StudentDAO;
import com.lanking.cloud.sdk.data.Params;

import httl.util.CollectionUtils;

@Component(value = "doQuestionRankStudentDAO")
public class StudentDAOImpl extends AbstractHibernateDAO<Student, Long> implements StudentDAO {

	@Override
	public Map<Long, Student> taskListStudent(List<Long> userIds) {
		Params param = Params.param();
		param.put("ids", userIds);

		Map<Long, Student> data = new HashMap<>();
		List<Student> lists = repo.find("$taskListStudent", param).list();
		if (CollectionUtils.isEmpty(lists)) {
			return data;
		}
		
		for (Student v : lists) {
			data.put(v.getId(), v);
		}

		return data;
	}

	@Autowired
	@Qualifier("StudentRepo")
	@Override
	public void setRepo(Repo<Student, Long> repo) {
		this.repo = repo;
	}

}
