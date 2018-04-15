package com.lanking.uxb.service.report.controller;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.homeworkRightRate.api.TaskHomeworkRightRateStatService;
import com.lanking.uxb.service.report.api.TaskClassStatisticsReportService;

@RestController
@RequestMapping(value = "task/t/report")
public class ReportStatController {
	@Autowired
	private TaskHomeworkRightRateStatService statService;

	private static final int SIZE = 200;
	@Autowired
	private TaskClassStatisticsReportService classStatService;

	private static final Logger logger = LoggerFactory.getLogger(ReportStatController.class);

	@RequestMapping(value = "data", method = { RequestMethod.GET, RequestMethod.POST })
	public Value data() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		CursorPage<Long, Teacher> page = classStatService.getAll(CP.cursor(Long.MAX_VALUE, SIZE));
		while (page.isNotEmpty()) {
			for (Teacher teacher : page) {
				if (teacher.getSubjectCode() != null) {
					try {
						classStatService.create(year, month, teacher);
					} catch (Exception e) {
						logger.error("YoomathClassStatisticsReportTask error[teacher ID:{}]：", teacher.getId(), e);
					}
				}
			}
			page = classStatService.getAll(CP.cursor(page.getLast().getId(), SIZE));
		}
		return new Value();
	}

}
