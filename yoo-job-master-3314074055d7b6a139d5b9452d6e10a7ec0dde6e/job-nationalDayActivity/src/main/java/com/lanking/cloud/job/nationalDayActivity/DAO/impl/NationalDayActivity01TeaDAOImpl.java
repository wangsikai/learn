package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Tea;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01TeaDAO;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;

@Component("nda01NationalDayActivity01TeaDAO")
public class NationalDayActivity01TeaDAOImpl extends AbstractHibernateDAO<NationalDayActivity01Tea, Long>
		implements NationalDayActivity01TeaDAO {

	@Autowired
	@Qualifier("NationalDayActivity01TeaRepo")
	@Override
	public void setRepo(Repo<NationalDayActivity01Tea, Long> repo) {
		this.repo = repo;
	}

	@Override
	public CursorPage<Long, NationalDayActivity01Tea> getAllTeaByCursor(CursorPageable<Long> pageable) {
		return repo.find("$findAllTeaByCursor").fetch(pageable);
	}

	@Override
	public void deletes(List<Long> ids) {
		repo.deleteByIds(ids);
	}

	@Override
	public List<Long> getDeteleUserIds() {
		return repo.find("$findDeteleUserIds").list(Long.class);
	}

	@Override
	public List<NationalDayActivity01Tea> getTopNTea(int count) {
		Params param = Params.param();
		param.put("topn", count);

		return repo.find("$taskFindTopN", param).list();
	}

}
