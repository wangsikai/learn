package com.lanking.cloud.job.nationalDayActivity.DAO;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;

public interface StudentHomeworkDAO extends IHibernateDAO<StudentHomework, Long> {

	List<StudentHomework> getSubmitedIssuedHomework(List<Long> homeworkIds, Date startTime, Date endTime);
}
