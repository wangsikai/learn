package com.lanking.cloud.job.stuWeekReport.dao.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.dao.AbstractHibernateDAO;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.job.stuWeekReport.dao.HomeworkClazzDAO;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;

@Component(value = "stuWeekReportHomeworkClazzDAO")
public class HomeworkClazzDAOImpl extends AbstractHibernateDAO<HomeworkClazz, Long> implements HomeworkClazzDAO {

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	@Override
	public void setRepo(Repo<HomeworkClazz, Long> repo) {
		this.repo = repo;
	}

	@Override
	public CursorPage<Long, Map> queryClassIds(CursorPageable<Long> pageable, int modVal) {
		return repo.find("$queryClassIdsByWeek", Params.param("modVal", modVal)).fetch(pageable, Map.class,
				new CursorGetter<Long, Map>() {
					@Override
					public Long getCursor(Map bean) {
						return Long.parseLong(String.valueOf(bean.get("id")));
					}
				});
	}
}
