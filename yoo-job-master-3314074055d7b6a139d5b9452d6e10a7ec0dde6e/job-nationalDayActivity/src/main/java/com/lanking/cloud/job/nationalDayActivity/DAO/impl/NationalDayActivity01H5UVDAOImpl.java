package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5UV;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01H5UVDAO;
import com.lanking.cloud.sdk.data.Params;

@Component("nda01NationalDayActivity01H5UVDAO")
public class NationalDayActivity01H5UVDAOImpl extends AbstractHibernateDAO<NationalDayActivity01H5UV, Long>
		implements NationalDayActivity01H5UVDAO {

	@Autowired
	@Qualifier("NationalDayActivity01H5UVRepo")
	@Override
	public void setRepo(Repo<NationalDayActivity01H5UV, Long> repo) {
		this.repo = repo;
	}

	@Override
	public NationalDayActivity01H5UV create(NationalDayActivity01H5 h5, long userId, Date viewAt, long viewAtL) {
		NationalDayActivity01H5UV uv = new NationalDayActivity01H5UV();
		uv.setH5(h5);
		uv.setUserId(userId);
		uv.setFirstViewAt(viewAt);
		uv.setLatestViewAt(viewAt);
		uv.setViewCount(1L);
		uv.setViewAt(viewAtL);
		return repo.save(uv);
	}

	@Override
	public int updateUV(NationalDayActivity01H5 h5, long userId, long uv, Date viewAt, long viewAtL) {
		Params params = Params.param("userId", userId).put("h5", h5.ordinal()).put("uv", uv).put("latestViewAt", viewAt)
				.put("viewAtL", viewAtL);
		return repo.execute("$nda01UpdateUV", params);
	}

}
