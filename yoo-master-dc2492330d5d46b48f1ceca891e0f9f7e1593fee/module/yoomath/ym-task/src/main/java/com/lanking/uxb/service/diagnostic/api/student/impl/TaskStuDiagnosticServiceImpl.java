package com.lanking.uxb.service.diagnostic.api.student.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.diagnostic.api.student.StaticDiagnosticClassTopnKpService;
import com.lanking.uxb.service.diagnostic.api.student.StaticStuDiagnosticClassKpService;
import com.lanking.uxb.service.diagnostic.api.student.TaskStuDiagnosticService;

@Service
public class TaskStuDiagnosticServiceImpl implements TaskStuDiagnosticService {

	@Autowired
	private StaticStuDiagnosticClassKpService staticStuDiagnosticClassKpService;
	@Autowired
	private StaticHomeworkClassService homeworkClassService;
	@Autowired
	private StaticDiagnosticClassTopnKpService topnKpService;

	@Override
	public void taskStatStuClassKp(Date startTime, Date endTime) {
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, 50);
		CursorPage<Long, Long> cursorPage = staticStuDiagnosticClassKpService.getAllStudent(cursorPageable);
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			for (Long studentId : cursorPage.getItems()) {
				staticStuDiagnosticClassKpService.statStuClassKp(startTime, endTime, studentId);
			}
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, 50);
			cursorPage = staticStuDiagnosticClassKpService.getAllStudent(cursorPageable);
		}
	}

	@Override
	public void taskStatTopnStu(Date startTime, Date endTime) {
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, 20);
		CursorPage<Long, Long> cursorPage = homeworkClassService.curDayIssuedClass(cursorPageable, startTime, endTime);
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			List<Long> classIds = cursorPage.getItems();
			for (Long classId : classIds) {
				topnKpService.staticTopnStu(classId);
			}
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, 20);
			cursorPage = homeworkClassService.curDayIssuedClass(cursorPageable, startTime, endTime);
		}

	}

}
