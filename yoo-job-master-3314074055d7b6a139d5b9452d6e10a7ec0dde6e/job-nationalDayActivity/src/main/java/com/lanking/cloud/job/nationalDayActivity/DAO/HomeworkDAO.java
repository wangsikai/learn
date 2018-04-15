package com.lanking.cloud.job.nationalDayActivity.DAO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoomath.homework.Homework;

public interface HomeworkDAO extends IHibernateDAO<Homework, Long> {

	List<Homework> pullHomework(long maxHkId, Date minStartAt, Date maxEndAt, int minDistributeCount);

	Map<Long, List<Long>> queryRightQuestion(long homeworkId);
}