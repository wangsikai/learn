package com.lanking.cloud.job.nationalDayActivity.DAO.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Stu;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01StuDAO;
import com.lanking.cloud.sdk.data.Params;

@Component("nda01NationalDayActivity01StuDAO")
public class NationalDayActivity01StuDAOImpl extends AbstractHibernateDAO<NationalDayActivity01Stu, Long>
		implements NationalDayActivity01StuDAO {

	@Autowired
	@Qualifier("NationalDayActivity01StuRepo")
	@Override
	public void setRepo(Repo<NationalDayActivity01Stu, Long> repo) {
		this.repo = repo;
	}

	@Override
	public NationalDayActivity01Stu create(long studentId, long rightCount) {
		NationalDayActivity01Stu nationalDayActivit01Stu = new NationalDayActivity01Stu();
		nationalDayActivit01Stu.setUserId(studentId);
		nationalDayActivit01Stu.setRightCount(rightCount);
		return repo.save(nationalDayActivit01Stu);
	}

	@Override
	public int incrRightCount(long studentId, long rightCount) {
		Params params = Params.param("userId", studentId).put("rightCount", rightCount);
		return repo.execute("$nda01IncrRightCount", params);
	}

	@Override
	public List<NationalDayActivity01Stu> top50() {
		return repo.find("$ndaAwardList").list();
	}

}
