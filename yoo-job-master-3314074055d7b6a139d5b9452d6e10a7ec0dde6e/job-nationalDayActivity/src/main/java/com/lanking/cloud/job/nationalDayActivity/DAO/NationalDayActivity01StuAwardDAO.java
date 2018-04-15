package com.lanking.cloud.job.nationalDayActivity.DAO;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01StuAward;

public interface NationalDayActivity01StuAwardDAO extends IHibernateDAO<NationalDayActivity01StuAward, Long> {

	long count();
}