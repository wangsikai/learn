package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5PVUV;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01H5PVUVDAO;
import com.lanking.cloud.sdk.data.Params;

@Component("nda01NationalDayActivity01H5PVUVDAO")
public class NationalDayActivity01H5PVUVDAOImpl extends AbstractHibernateDAO<NationalDayActivity01H5PVUV, Long>
		implements NationalDayActivity01H5PVUVDAO {

	@Autowired
	@Qualifier("NationalDayActivity01H5PVUVRepo")
	@Override
	public void setRepo(Repo<NationalDayActivity01H5PVUV, Long> repo) {
		this.repo = repo;
	}

	@Override
	public NationalDayActivity01H5PVUV create(NationalDayActivity01H5 h5, long pv, long uv, long viewAtL) {
		NationalDayActivity01H5PVUV pvuv = new NationalDayActivity01H5PVUV();
		pvuv.setH5(h5);
		pvuv.setPv(pv);
		pvuv.setUv(uv);
		pvuv.setViewAt(viewAtL);
		return repo.save(pvuv);
	}

	@Override
	public int updateUVPV(NationalDayActivity01H5 h5, long pv, long uv, long viewAtL) {

		Params params = Params.param("h5", h5.ordinal()).put("pv", pv).put("uv", uv).put("viewAtL", viewAtL);
		return repo.execute("$nda01UpdatePVUV", params);
	}

}
