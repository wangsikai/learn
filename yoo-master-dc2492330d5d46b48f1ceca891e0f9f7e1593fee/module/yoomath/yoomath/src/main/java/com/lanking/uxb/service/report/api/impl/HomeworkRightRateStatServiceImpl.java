package com.lanking.uxb.service.report.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.HomeworkRightRateStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.report.api.HomeworkRightRateStatService;

@Transactional(readOnly = true)
@Service
public class HomeworkRightRateStatServiceImpl implements HomeworkRightRateStatService {

	@Autowired
	@Qualifier("HomeworkRightRateStatRepo")
	private Repo<HomeworkRightRateStat, Long> homeworkRightRateStatRepo;

	@Override
	public List<HomeworkRightRateStat> getStat(Long classId, Date startTime, Date endTime) {
		Params params = Params.param("classId", classId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return homeworkRightRateStatRepo.find("$getStat", params).list();
	}

}
