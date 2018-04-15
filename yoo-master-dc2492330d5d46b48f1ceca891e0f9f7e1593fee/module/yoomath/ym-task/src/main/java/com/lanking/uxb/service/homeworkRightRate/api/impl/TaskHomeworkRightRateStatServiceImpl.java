package com.lanking.uxb.service.homeworkRightRate.api.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.HomeworkRightRateStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.homeworkRightRate.api.TaskHomeworkRightRateStatService;

@Transactional(readOnly = true)
@Service
public class TaskHomeworkRightRateStatServiceImpl implements TaskHomeworkRightRateStatService {

	@Autowired
	@Qualifier("HomeworkRightRateStatRepo")
	private Repo<HomeworkRightRateStat, Long> repo;

	private Logger logger = LoggerFactory.getLogger(TaskHomeworkRightRateStatServiceImpl.class);

	@Transactional
	@Override
	public void stat(long classId) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		Integer avgRate = this.findWeekRateList(classId, new Date());
		if (avgRate != null) {
			HomeworkRightRateStat rateStat = new HomeworkRightRateStat();
			rateStat.setHomeworkClassId(classId);
			rateStat.setRightRate(BigDecimal.valueOf(avgRate));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				rateStat.setStatisticsTime(format.parse(year + "-" + month + "-" + day));
				repo.save(rateStat);
			} catch (Exception e) {
				logger.error("TaskHomeworkRightRateStat time format error", e);
			}
		}
	}

	@Override
	public Integer findWeekRateList(long classId, Date endTime) {
		Params params = Params.param("classId", classId);
		params.put("endTime", endTime);
		return repo.find("$findWeekRateList", params).get(Integer.class);
	}
}
