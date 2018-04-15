package com.lanking.uxb.service.report.api.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseSection;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.report.api.TaskClassReportBaseService;

@Transactional(readOnly = true)
@Service
public class TaskClassReportBaseServiceImpl implements TaskClassReportBaseService {
	@Autowired
	@Qualifier("HomeworkClazzRepo")
	Repo<HomeworkClazz, Long> homeworkClazzRepo;

	@Autowired
	@Qualifier("StudentExerciseSectionRepo")
	private Repo<StudentExerciseSection, Long> repo;

	@Autowired
	@Qualifier("HomeworkRepo")
	Repo<Homework, Long> homeworkRepo;

	@Override
	public List<HomeworkClazz> listCurrentClazzs(long teacherId) {
		return homeworkClazzRepo.find("$taskZyQuery",
				Params.param("teacherId", teacherId).put("status", Status.ENABLED.getValue())).list();
	}

	@Override
	public List<Homework> listByTime(long userId, long clazzId, int year, int month) {
		// 获取当月第一天时间和最后一天
		// Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.YEAR, year);
		// cal.set(Calendar.MONTH, month);
		// // 下个月第一天减去一天就是这个月最后一天
		// cal.set(Calendar.DAY_OF_MONTH, 1);
		// cal.add(Calendar.DAY_OF_MONTH, -1);
		// Date endTime = cal.getTime();
		// cal.set(Calendar.DAY_OF_MONTH, 1);
		// Date beginTime = cal.getTime();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		return homeworkRepo.find(
				"$taskListByTime",
				Params.param("beginTime", cal.getTime()).put("endTime", new Date()).put("createId", userId)
						.put("clazzId", clazzId)).list();
	}

	@Override
	public Homework getFirstIssuedHomeworkInMonth(int year, int month, long classId) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		List<Homework> hkList = homeworkRepo.find("$taskGetFirstIssuedHomeworkInMonth",
				Params.param("beginTime", cal.getTime()).put("endTime", new Date()).put("classId", classId)).list();
		if (CollectionUtils.isEmpty(hkList)) {
			return null;
		}
		return hkList.get(0);
	}

	@Override
	public StudentExerciseSection get(long id) {
		return repo.get(id);
	}

	@Override
	public StudentExerciseSection getBySection(long userId, long sectionCode) {
		return repo.find("$taskGetBySection", Params.param("userId", userId).put("sectionCode", sectionCode)).get();
	}

	@Override
	public Map<Long, StudentExerciseSection> mgetBySection(long userId, Collection<Long> sectionCodes, Long lastMonth) {
		Params p = Params.param();
		if (lastMonth != null) {
			p.put("lastMonth", lastMonth);
		}
		p.put("userId", userId);
		p.put("sectionCodes", sectionCodes);
		List<StudentExerciseSection> list = repo.find("$taskGetBySections",
				Params.param("userId", userId).put("sectionCodes", sectionCodes)).list();
		Map<Long, StudentExerciseSection> map = Maps.newHashMap();
		for (StudentExerciseSection s : list) {
			map.put(s.getSectionCode(), s);
		}
		return map;
	}

	@Override
	public List<StudentExerciseSection> findByClassIdAndSectionCode(long classId, long sectionCode, Long lastMonth) {
		Params params = Params.param("sectionCode", sectionCode + "%");
		params.put("classId", classId);
		params.put("lastMonth", lastMonth);
		List<StudentExerciseSection> list = repo.find("$taskFindByClassIdAndSectionCode", params).list();
		if (list.size() == 0) {
			params.remove("lastMonth");
			params.put("curMonth", lastMonth);
			list = repo.find("$taskFindByClassIdAndSectionCode", params).list();
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> findStuAvgRightRateList(long classId, Date startTime, Date endTime) {
		Params params = Params.param("classId", classId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		if (endTime != null) {
			params.put("endTime", endTime);
		}
		return homeworkRepo.find("$findStuAvgRightRateList", params).list(Map.class);
	}
}
