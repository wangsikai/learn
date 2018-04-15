package com.lanking.uxb.service.report.api.impl.job;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.uxb.service.report.api.TaskHomeworkClassService;
import com.lanking.uxb.service.report.api.TaskStudentStatisticsReportService;

public class YoomathStudentStatisticsReportStatJob implements SimpleJob {
	private Logger logger = LoggerFactory.getLogger(YoomathStudentStatisticsReportStatJob.class);

	@Autowired
	private TaskStudentStatisticsReportService studentStatisticsReportService;

	@Autowired
	private TaskHomeworkClassService homeworkClassService;

	private static final int SIZE = 50;

	@Override
	public void execute(ShardingContext shardingContext) {
		// 当前毫秒数
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		// 查询游标查询班级
		CursorPage<Long, HomeworkClazz> page = homeworkClassService.getAll(CP.cursor(Long.MAX_VALUE, SIZE));
		while (page.isNotEmpty()) {
			for (HomeworkClazz clazz : page) {
				try {
					// 统计当前班级时间范围内作业相关数据
					studentStatisticsReportService.create(year, month, clazz.getId());
				} catch (Exception e) {
					logger.error("YoomathStudentStatisticsReportStatTask error[class ID:{}]：", clazz.getId(), e);
				}
			}
			page = homeworkClassService.getAll(CP.cursor(page.getLast().getId(), SIZE));
		}
	}

}
