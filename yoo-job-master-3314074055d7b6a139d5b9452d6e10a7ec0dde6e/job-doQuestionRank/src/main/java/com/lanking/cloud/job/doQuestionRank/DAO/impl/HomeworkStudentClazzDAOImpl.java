package com.lanking.cloud.job.doQuestionRank.DAO.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.job.doQuestionRank.DAO.HomeworkStudentClazzDAO;
import com.lanking.cloud.sdk.data.Params;

import httl.util.CollectionUtils;

@Component
public class HomeworkStudentClazzDAOImpl extends AbstractHibernateDAO<HomeworkStudentClazz, Long>
		implements HomeworkStudentClazzDAO {

	@Override
	public Map<Long, List<HomeworkStudentClazz>> taskListclazz(List<Long> userIds) {
		Params param = Params.param();
		param.put("studentIds", userIds);

		Map<Long, List<HomeworkStudentClazz>> data = new HashMap<>();
		List<HomeworkStudentClazz> lists = repo.find("$taskListclazz", param).list();
		if (CollectionUtils.isEmpty(lists)) {
			return data;
		}

		for (HomeworkStudentClazz v : lists) {
			if (data.containsKey(v.getStudentId())) {
				List<HomeworkStudentClazz> clazzList = data.get(v.getStudentId());
				clazzList.add(v);
				data.put(v.getStudentId(), clazzList);
			} else {
				List<HomeworkStudentClazz> clazzList = Lists.newArrayList();
				clazzList.add(v);
				data.put(v.getStudentId(), clazzList);
			}
		}

		return data;
	}

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	@Override
	public void setRepo(Repo<HomeworkStudentClazz, Long> repo) {
		this.repo = repo;
	}
}
