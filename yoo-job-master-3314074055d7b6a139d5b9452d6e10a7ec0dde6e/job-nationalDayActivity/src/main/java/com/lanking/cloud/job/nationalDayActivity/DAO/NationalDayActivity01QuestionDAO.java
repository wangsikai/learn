package com.lanking.cloud.job.nationalDayActivity.DAO;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Question;

public interface NationalDayActivity01QuestionDAO extends IHibernateDAO<NationalDayActivity01Question, Long> {

	boolean exist(long studentId, long questionId);

	NationalDayActivity01Question create(long studentId, long questionId);
}