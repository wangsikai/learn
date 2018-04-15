package com.lanking.uxb.service.report.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentClassWeekReport;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.report.api.StudentClassWeekReportService;

@Transactional(readOnly = true)
@Service
public class StudentClassWeekReportServiceImpl implements StudentClassWeekReportService {

	@Autowired
	@Qualifier("StudentClassWeekReportRepo")
	private Repo<StudentClassWeekReport, Long> repo;

	@Override
	public List<StudentClassWeekReport> findList(List<Long> classIds, String startDate, String endDate) {
		Params params = Params.param("classIds", classIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return repo.find("$findList", params).list();
	}

	@Override
	public Map<Long, StudentClassWeekReport> getReportMap(Long userId, List<Long> classIds, String startDate,
			String endDate) {
		Params params = Params.param("userId", userId);
		params.put("classIds", classIds);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		List<StudentClassWeekReport> list = repo.find("$findList", params).list();
		Map<Long, StudentClassWeekReport> map = new HashMap<Long, StudentClassWeekReport>();
		for (StudentClassWeekReport s : list) {
			map.put(s.getClazzId(), s);
		}
		return map;
	}

}
