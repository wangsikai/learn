package com.lanking.stuWeekReport.service.impl;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.stuWeekReport.service.StuWeekReportStatService;

@Service
public class TaskStuWeekReportServiceImpl implements com.lanking.stuWeekReport.service.TaskStuWeekReportService {

	@Autowired
	private StuWeekReportStatService stuWeekService;

	@Override
	public void statWeek(String createTime) {
		int fetchCount = 200;
		CursorPage<Long, Map> ids = stuWeekService.queryUserId(CP.cursor(Long.MAX_VALUE, fetchCount));

		while (ids.isNotEmpty()) {
			// 统计每个用户
			for (Map map : ids) {
				Long studentId = ((BigInteger) map.get("id")).longValue();
				stuWeekService.stat(studentId, createTime);
			}
			// 获取下一页
			ids = stuWeekService.queryUserId(CP.cursor(ids.getNextCursor(), fetchCount));
		}

	}

	@Override
	public void statClassWeek(String createTime) {
		int fetchCount = 200;
		CursorPage<Long, Map> classIds = stuWeekService.queryClassIds(CP.cursor(Long.MAX_VALUE, fetchCount));

		while (classIds.isNotEmpty()) {
			// 统计每个班级
			for (Map map : classIds) {
				Long classId = ((BigInteger) map.get("id")).longValue();
				stuWeekService.statStuClassWeekReport(classId, createTime);
			}
			// 获取下一页
			classIds = stuWeekService.queryClassIds(CP.cursor(classIds.getNextCursor(), fetchCount));
		}
	}
}
