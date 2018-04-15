package com.lanking.uxb.service.activity.api.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01UserLuckyDraw;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserLuckyDrawService;

/**
 * 参与活动的用户抽奖记录接口实现
 * 
 * @author peng.zhao
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity01UserLuckyDrawServiceImpl implements HolidayActivity01UserLuckyDrawService {

	@Autowired
	@Qualifier("HolidayActivity01UserLuckyDrawRepo")
	private Repo<HolidayActivity01UserLuckyDraw, Long> repo;

	@Override
	public HolidayActivity01UserLuckyDraw get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void save(HolidayActivity01UserLuckyDraw record) {
		repo.save(record);
	}

	@Override
	public List<HolidayActivity01UserLuckyDraw> getByUser(long code, long userId, Date date) {
		Params params = Params.param();
		params.put("code", code);
		params.put("userId", userId);
		if (date != null) {
			params.put("nowdate", new SimpleDateFormat("yyyy-MM-dd").format(date));
		}
		return repo.find("$findUserLuckyByUserId", params).list();
	}

	@Override
	public List<HolidayActivity01UserLuckyDraw> getByCode(long code, Date date) {
		Params params = Params.param();
		params.put("code", code);
		if (date != null) {
			params.put("nowdate", new SimpleDateFormat("yyyy-MM-dd").format(date));
		}
		return repo.find("$findLuckyDrawByCode", params).list();
	}

	@Override
	public long getCountByUser(long code, long userId, Date date) {
		Params params = Params.param();
		params.put("code", code);
		params.put("userId", userId);
		if (date != null) {
			params.put("nowdate", new SimpleDateFormat("yyyy-MM-dd").format(date));
		}

		return repo.find("$findUserLuckyCountByUserId", params).count();
	}

}
