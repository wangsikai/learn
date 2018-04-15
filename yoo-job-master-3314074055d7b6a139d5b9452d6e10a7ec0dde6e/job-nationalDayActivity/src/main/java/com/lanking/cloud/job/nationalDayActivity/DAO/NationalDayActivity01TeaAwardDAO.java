package com.lanking.cloud.job.nationalDayActivity.DAO;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01TeaAward;

public interface NationalDayActivity01TeaAwardDAO extends IHibernateDAO<NationalDayActivity01TeaAward, Long> {

	long count();
}
