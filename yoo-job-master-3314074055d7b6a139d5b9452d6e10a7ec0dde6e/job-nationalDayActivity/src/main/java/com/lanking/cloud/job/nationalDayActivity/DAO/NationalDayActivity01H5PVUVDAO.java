package com.lanking.cloud.job.nationalDayActivity.DAO;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5PVUV;

public interface NationalDayActivity01H5PVUVDAO extends IHibernateDAO<NationalDayActivity01H5PVUV, Long> {

	NationalDayActivity01H5PVUV create(NationalDayActivity01H5 h5, long pv, long uv, long viewAtL);

	int updateUVPV(NationalDayActivity01H5 h5, long pv, long uv, long viewAtL);
}