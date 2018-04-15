package com.lanking.uxb.service.report.api.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkRightRateStat;
import com.lanking.cloud.domain.yoomath.stat.StudentWeekReport;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.report.api.StudentHomeworkRightRateStatService;

@Transactional(readOnly = true)
@Service
public class StudentHomeworkRightRateStatServiceImpl implements StudentHomeworkRightRateStatService {

	@Autowired
	@Qualifier("StudentHomeworkRightRateStatRepo")
	private Repo<StudentHomeworkRightRateStat, Long> studentHomeworkRightRateStatRepo;

	@Autowired
	@Qualifier("StudentWeekReportRepo")
	private Repo<StudentWeekReport, Long> studentWeekReportRepo;

	@Override
	public List<StudentHomeworkRightRateStat> findList(Integer days, Long userId) {
		Params params = Params.param("userId", userId);
		params.put("endTime", new Date());
		params.put("startTime", getTimeByDay0(days));
		return studentHomeworkRightRateStatRepo.find("$findList", params).list();
	}

	public Date getTimeByDay0(Integer day0) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -day0);
		return calendar.getTime();
	}

	@Transactional
	@Override
	public void inintData() {
		StudentWeekReport s = studentWeekReportRepo.get(2L);
		List<Map> list = new ArrayList<Map>();
		Map<String, Object> classMap1 = new HashMap<String, Object>();
		classMap1.put("id", 346207941105295360L);
		classMap1.put("name", "111111111");
		classMap1.put("myRank", 2);
		List<Map> ranks = new ArrayList<Map>();
		Map<String, Object> pa = new HashMap<String, Object>();
		pa.put("stuId", 328822386742796288L);
		pa.put("rightRate", 15);
		pa.put("float", 1);
		pa.put("me", true);
		Map<String, Object> pa1 = new HashMap<String, Object>();
		pa1.put("stuId", 268066428584730624L);
		pa1.put("rightRate", 85);
		pa1.put("float", 1);
		pa1.put("me", false);
		ranks.add(pa);
		ranks.add(pa1);
		Map ss = new HashMap();
		ss.put("class", classMap1);
		ss.put("ranks", ranks);
		list.add(ss);

		Map<String, Object> classMap2 = new HashMap<String, Object>();
		classMap2.put("id", 409144268007940096L);
		classMap2.put("name", "2222222");
		classMap2.put("myRank", 1);
		List<Map> ranks2 = new ArrayList<Map>();
		Map<String, Object> pa2 = new HashMap<String, Object>();
		pa2.put("stuId", 328822386742796288L);
		pa2.put("rightRate", 15);
		pa2.put("float", 1);
		pa2.put("me", true);
		Map<String, Object> pa12 = new HashMap<String, Object>();
		pa12.put("stuId", 268066428584730624L);
		pa12.put("rightRate", 85);
		pa12.put("float", 1);
		pa12.put("me", false);
		ranks2.add(pa2);
		ranks2.add(pa12);
		Map ss2 = new HashMap();
		ss2.put("class", classMap2);
		ss2.put("ranks", ranks2);
		list.add(ss2);
		s.setRightRateClassRanks(JSON.toJSONString(list));
		studentWeekReportRepo.save(s);
	}
}
