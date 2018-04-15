package com.lanking.uxb.service.activity.api.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01ClassUser;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity01ClassUserService;

/**
 * 假期活动01参与活动的班级学生接口实现
 * 
 * @author peng.zhao
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity01ClassUserServiceImpl implements HolidayActivity01ClassUserService {

	@Autowired
	@Qualifier("HolidayActivity01ClassUserRepo")
	private Repo<HolidayActivity01ClassUser, Long> repo;

	@Override
	public HolidayActivity01ClassUser get(long id) {
		return repo.get(id);
	}

	@Transactional
	@Override
	public List<HolidayActivity01ClassUser> getByClass(Long classId, Long code) {
		Params params = Params.param();
		params.put("classId", classId);
		if (code != null) {
			params.put("activityCode", code);
		}

		return repo.find("$findHolidayActivity01ClassUser", params).list();
	}

	@Transactional
	@Override
	public void create(Collection<HolidayActivity01ClassUser> classUser) {
		repo.save(classUser);
	}

}
