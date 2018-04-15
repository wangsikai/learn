package com.lanking.uxb.channelSales.channel.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.channelSales.channel.api.CsHomeworkQuery;
import com.lanking.uxb.channelSales.channel.api.CsHomeworkService;

@Service
@Transactional(readOnly = true)
public class CsHomeworkServiceImpl implements CsHomeworkService {

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> homeworkRepo;

	@Override
	public Page<Homework> query(CsHomeworkQuery query, Pageable p) {
		return homeworkRepo.find("$csList", Params.param("classId", query.getClassId())).fetch(p);
	}

}
