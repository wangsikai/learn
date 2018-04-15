package com.lanking.uxb.service.diagnostic.api.impl.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.diagnostic.api.student.TaskStuDiagnosticService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassStudentService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticService;

public class DiagnosticClassJob implements SimpleJob {
	private static final int CLASS_FETCH_SIZE = 200;
	private static final Logger logger = LoggerFactory.getLogger(DiagnosticClassJob.class);

	@Autowired
	private StaticHomeworkClassService homeworkClassService;
	@Autowired
	private TaskDiagnosticService diagnosticService;
	@Autowired
	private TaskDiagnosticClassStudentService diagnosticClassStudentService;
	@Autowired
	private TaskStuDiagnosticService stuDiagService;

	@Override
	public void execute(ShardingContext shardingContext) {
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, CLASS_FETCH_SIZE);
		CursorPage<Long, HomeworkClazz> cursorPage = homeworkClassService.findEnableClass(cursorPageable);
		// boolean init = !diagnosticClassKnowpointService.hasClassKpData();

		// 全量统计近期N段时间的排名情况
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			List<HomeworkClazz> homeworkClazzs = cursorPage.getItems();

			for (HomeworkClazz homeworkClazz : homeworkClazzs) {
				try {
					diagnosticClassStudentService.doClassStudentRankStat(homeworkClazz.getId(), null);
				} catch (Exception e) {
					logger.error("static class {} has error {}", homeworkClazz.getId(), e);
				}
			}

			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, CLASS_FETCH_SIZE);
			cursorPage = homeworkClassService.findEnableClass(cursorPageable);
		}

		// 学生相关数据统计
		long t = System.currentTimeMillis();
		logger.debug("DiagnosticStudentTask start:", t);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date endTime = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date startTime = calendar.getTime();
		stuDiagService.taskStatTopnStu(startTime, endTime);
		logger.debug("DiagnosticStudentTask end cost:", System.currentTimeMillis() - t);

		// 处理当天的全国平均做题数
		diagnosticService.doDiagnostic();
	}

}
