package com.lanking.cloud.job.nationalDayActivity.DAO;

import java.util.Date;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5UV;

public interface NationalDayActivity01H5UVDAO extends IHibernateDAO<NationalDayActivity01H5UV, Long> {

	NationalDayActivity01H5UV create(NationalDayActivity01H5 h5, long userId, Date viewAt, long viewAtL);

	int updateUV(NationalDayActivity01H5 h5, long userId, long uv, Date viewAt, long viewAtL);
}