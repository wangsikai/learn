package com.lanking.cloud.job.stuWeekReport.service.impl;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.job.stuWeekReport.service.StuWeekReportStatService;
import com.lanking.cloud.job.stuWeekReport.service.TaskStuWeekReportService;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;

@Service
public class TaskStuWeekReportServiceImpl implements TaskStuWeekReportService {

	@Autowired
	private StuWeekReportStatService stuWeekService;

	@Override
	public void statWeek(int modVal) {
		int fetchCount = 200;
		CursorPage<Long, Map> ids = stuWeekService.queryUserId(CP.cursor(Long.MAX_VALUE, fetchCount), modVal);

		while (ids.isNotEmpty()) {
			// 统计每个用户
			for (Map map : ids) {
				Long studentId = ((BigInteger) map.get("id")).longValue();
				stuWeekService.stat(studentId);
			}
			// 获取下一页
			ids = stuWeekService.queryUserId(CP.cursor(ids.getNextCursor(), fetchCount), modVal);
		}

	}

	@Override
	public void statClassWeek(int modVal) {
		int fetchCount = 200;
		CursorPage<Long, Map> classIds = stuWeekService.queryClassIds(CP.cursor(Long.MAX_VALUE, fetchCount), modVal);

		while (classIds.isNotEmpty()) {
			// 统计每个班级
			for (Map map : classIds) {
				Long classId = ((BigInteger) map.get("id")).longValue();
				stuWeekService.statStuClassWeekReport(classId);
			}
			// 获取下一页
			classIds = stuWeekService.queryClassIds(CP.cursor(classIds.getNextCursor(), fetchCount), modVal);
		}
	}
}
