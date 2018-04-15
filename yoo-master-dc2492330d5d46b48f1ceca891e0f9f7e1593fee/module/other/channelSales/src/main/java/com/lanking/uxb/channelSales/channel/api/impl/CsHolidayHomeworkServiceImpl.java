package com.lanking.uxb.channelSales.channel.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.channelSales.channel.api.CsHolidayHomeworkService;
import com.lanking.uxb.channelSales.channel.api.CsHomeworkQuery;

@Service
@Transactional(readOnly = true)
public class CsHolidayHomeworkServiceImpl implements CsHolidayHomeworkService {
	@Autowired
	@Qualifier("HolidayHomeworkRepo")
	private Repo<HolidayHomework, Long> holidayHomeworkRepo;

	@Override
	public Page<HolidayHomework> query(CsHomeworkQuery query, Pageable p) {
		return holidayHomeworkRepo.find("$csList", Params.param("classId", query.getClassId())).fetch(p);
	}

}
