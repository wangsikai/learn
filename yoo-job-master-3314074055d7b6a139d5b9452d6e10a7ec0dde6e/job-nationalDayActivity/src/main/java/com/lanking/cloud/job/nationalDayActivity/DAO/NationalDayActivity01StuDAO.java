package com.lanking.cloud.job.nationalDayActivity.DAO;

import java.util.List;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Stu;

public interface NationalDayActivity01StuDAO extends IHibernateDAO<NationalDayActivity01Stu, Long> {

	NationalDayActivity01Stu create(long studentId, long rightCount);

	int incrRightCount(long studentId, long rightCount);

	List<NationalDayActivity01Stu> top50();
}