package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.job.nationalDayActivity.DAO.HomeworkDAO;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;

@Component("nda01HomeworkDAO")
public class HomeworkDAOImpl extends AbstractHibernateDAO<Homework, Long> implements HomeworkDAO {

	@Autowired
	@Qualifier("HomeworkRepo")
	@Override
	public void setRepo(Repo<Homework, Long> repo) {
		this.repo = repo;
	}

	@Override
	public List<Homework> pullHomework(long maxHkId, Date minStartAt, Date maxEndAt, int minDistributeCount) {
		Params params = Params.param("maxHkId", maxHkId).put("minStartAt", minStartAt).put("maxStartAt", maxEndAt)
				.put("minDistributeCount", minDistributeCount);
		return repo.find("$nda01PullHomework", params).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, List<Long>> queryRightQuestion(long homeworkId) {
		Map<Long, List<Long>> maps = Maps.newHashMap();
		List<Map> lists = repo.find("$nda01QueryRightQuestion", Params.param("homeworkId", homeworkId)).list(Map.class);
		if (CollectionUtils.isNotEmpty(lists)) {
			for (Map map : lists) {
				long studentId = ((BigInteger) map.get("student_id")).longValue();
				long questionId = ((BigInteger) map.get("question_id")).longValue();
				List<Long> list = maps.get(studentId);
				if (list == null) {
					list = Lists.newArrayList();
				}
				list.add(questionId);
				maps.put(studentId, list);
			}
		}
		return maps;
	}

}
