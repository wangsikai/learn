package com.lanking.uxb.service.holidayActivity01.api.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Class;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Statistics;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01StatisticsService;

@Service
@Transactional(readOnly = true)
public class TaskTaskHolidayActivity01StatisticsServiceImpl implements TaskHolidayActivity01StatisticsService {

	@Autowired
	@Qualifier("HolidayActivity01ClassRepo")
	private Repo<HolidayActivity01Class, Long> holidayActivity01ClassRepo;

	@Autowired
	@Qualifier("HolidayActivity01StatisticsRepo")
	private Repo<HolidayActivity01Statistics, Long> holidayActivity01StatisticsRepo;

	@Transactional
	@Override
	public void createStatistics(List<Map> maps, Date lastStartPeriodTime, Date lastEndPeriodTime, Date startPeriodTime,
			Date endPeriodTime) {
		Date dt = new Date();
		List<HolidayActivity01Statistics> statistics = Lists.newArrayList();
		for (Map map : maps) {
			Long classId = Long.parseLong((map.get("class_id").toString()));
			Long activityCode = Long.parseLong((map.get("activity_code").toString()));
			Long userId = Long.parseLong((map.get("user_id").toString()));
			int homeworkCount = Integer.parseInt(map.get("homeworkcount").toString());
			float avgSubmitRate = 0f;
			if (map.get("avgsubmitrate") != null) {
				avgSubmitRate = Float.parseFloat(map.get("avgsubmitrate").toString());
			}
			float avgRightRate = 0f;
			if (map.get("avgrightrate") != null) {
				avgRightRate = Float.parseFloat(map.get("avgrightrate").toString());
			}
			HolidayActivity01Statistics has = null;
			if (lastStartPeriodTime != null && lastEndPeriodTime != null) {
				has = getStatisticData(activityCode, lastStartPeriodTime, lastEndPeriodTime, classId);
			}
			BigDecimal submitRate = BigDecimal.valueOf(avgSubmitRate);
			HolidayActivity01Statistics hks = new HolidayActivity01Statistics();
			hks.setActivityCode(activityCode);
			hks.setClassId(classId);
			hks.setCreateAt(dt);
			hks.setUserId(userId);
			hks.setEndPeriodTime(endPeriodTime);
			hks.setStartPeriodTime(startPeriodTime);
			hks.setHomeworkCount(homeworkCount);
			hks.setSubmitRate(submitRate);
			hks.setRightRate(BigDecimal.valueOf(avgRightRate));
			// 评分 = 假期作业数（截止时间段） * 提交率（截止时间段） * 10，四舍五入取整
			int score = Math.round(homeworkCount * avgSubmitRate / 100 * 10);
			int periodScore = has == null ? score : score - has.getScore();
			hks.setPeriodScore(periodScore);
			hks.setScore(score);
			statistics.add(hks);
		}
		holidayActivity01StatisticsRepo.save(statistics);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> statisticClazzs(CursorPageable<Long> pageable, Date startPeriodTime,
			Date endPeriodTime, Long activityCode) {
		return holidayActivity01ClassRepo
				.find("$taskHolidayActivity01StatisticClazzs",
						Params.param("startPeriodTime", startPeriodTime).put("endPeriodTime", endPeriodTime)
								.put("activityCode", activityCode))
				.fetch(pageable, Map.class, new CursorGetter<Long, Map>() {
					@Override
					public Long getCursor(Map bean) {
						return Long.parseLong(String.valueOf(bean.get("class_id")));
					}
				});
	}

	@Override
	public HolidayActivity01Statistics getStatisticData(Long activityCode, Date startPeriodDate, Date endPeriodDate,
			Long clazzId) {
		Params params = Params.param("activityCode", activityCode);
		if (startPeriodDate != null) {
			params.put("startDate", startPeriodDate);
		}
		if (endPeriodDate != null) {
			params.put("endDate", endPeriodDate);
		}
		if (clazzId != null) {
			params.put("clazzId", clazzId);
		}
		return holidayActivity01StatisticsRepo.find("$taskGetStatisticOneData", params).get();
	}

	@Transactional
	@Override
	public int deleteStatistics(Long activityCode, Date startDate, Date endDate) {
		Params params = Params.param("activityCode", activityCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return holidayActivity01StatisticsRepo.execute("$taskDeleteStatistic", params);
	}

}
