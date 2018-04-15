package com.lanking.uxb.service.report.api.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.report.api.TaskStudentHomeworkService;

@Transactional(readOnly = true)
@Service
public class TaskHomeworkStudentClazzServiceImpl implements TaskStudentHomeworkService {

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> stuHkRepo;

	@Override
	public Map<Long, Integer> getStuStatRankByClass(int year, int month, long clazzId, List<Long> stuIds) {

		Map<Long, Integer> rankMap = Maps.newHashMap();
		// 获取当月第一天时间和最后一天
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		// 下个月第一天减去一天就是这个月最后一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date endTime = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = cal.getTime();
		List<Map> listMap = stuHkRepo.find(
				"$getStuStatRankByClass",
				Params.param("beginTime", beginTime).put("endTime", endTime).put("clazzId", clazzId)
						.put("stuIds", stuIds)).list(Map.class);
		for (int i = 0; i < listMap.size(); i++) {
			Long studentId = Long.valueOf(listMap.get(i).get("student_id").toString());
			rankMap.put(studentId, i + 1);
		}

		return rankMap;

	}

	@Override
	public List<StudentHomework> listByTime(int year, int month, long userId, long clazzId) {
		// 获取当月第一天时间和最后一天
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		// 下个月第一天减去一天就是这个月最后一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date endTime = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = cal.getTime();
		return stuHkRepo.find(
				"$listByTime",
				Params.param("beginTime", beginTime).put("endTime", endTime).put("studentId", userId)
						.put("clazzId", clazzId)).list();
	}

}
