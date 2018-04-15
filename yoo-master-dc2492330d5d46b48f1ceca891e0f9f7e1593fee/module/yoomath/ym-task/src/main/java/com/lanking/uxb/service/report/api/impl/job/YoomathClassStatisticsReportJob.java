package com.lanking.uxb.service.report.api.impl.job;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.uxb.service.report.api.TaskClassStatisticsReportService;

/**
 * yoomath班级统计
 * 
 * @since 2.6.0
 * @author wangsenhao
 * @describe 业务逻辑描述<br>
 *           1.主要是对班级当月作业数、学生数、正确率、完成率的一个统计(普通作业) <br>
 *           2.通过教材获取对应章节的练习情况进行统计(对应统计表字段knowpoint_analysis )<br>
 *           相关表：student_exercise_section(学生章节练习统计情况)，存class_statistics_report
 *
 */
public class YoomathClassStatisticsReportJob implements SimpleJob {

	private Logger logger = LoggerFactory.getLogger(YoomathClassStatisticsReportJob.class);

	private static final int SIZE = 200;
	@Autowired
	private TaskClassStatisticsReportService classStatService;

	@Override
	public void execute(ShardingContext shardingContext) {
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
	}
}
