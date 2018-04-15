package com.lanking.uxb.service.diagnostic.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.diagnostic.api.StaticHomeworkClassService;
import com.lanking.uxb.service.diagnostic.api.student.TaskStuDiagnosticService;
import com.lanking.uxb.service.diagnostic.api.teacher.StaticDiagnosticService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticClassStudentService;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticService;

/**
 * 教师班级相关统计.
 * 
 * @author wlche
 *
 */
@RestController
@RequestMapping(value = "task/t/diag")
public class TeaDiagnosticStatController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StaticHomeworkClassService homeworkClassService;
	@Autowired
	private StaticDiagnosticService staticDiagnosticService;
	@Autowired
	private TaskDiagnosticService diagnosticService;
	@Autowired
	private TaskStuDiagnosticService stuDiagService;
	@Autowired
	private TaskDiagnosticClassStudentService diagnosticClassStudentService;

	/**
	 * 初始化所有班级需要重新统计的数据.
	 * 
	 * @return
	 */
	@RequestMapping(value = "init", method = { RequestMethod.GET, RequestMethod.POST })
	public Value init(Long classId) {
		if (classId == null) {
			int classSize = 200;

			CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, classSize);
			CursorPage<Long, HomeworkClazz> cursorPage = homeworkClassService.findEnableClass(cursorPageable);

			long t1 = System.currentTimeMillis();
			logger.info("[diag init] -- start");

			while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
				List<HomeworkClazz> homeworkClazzs = cursorPage.getItems();
				for (HomeworkClazz homeworkClazz : homeworkClazzs) {
					try {
						long tt1 = System.currentTimeMillis();
						staticDiagnosticService.initDiagnosticClass(homeworkClazz);
						long tt2 = System.currentTimeMillis();
						logger.info("[diag init] -- 班级" + homeworkClazz.getId() + "内作业完成统计，耗时=" + ((tt2 - tt1) / 1000));
					} catch (Exception e) {
						logger.error("task class {} init has error {}", homeworkClazz.getId(), e);
					}
				}
				Long nextCursor = cursorPage.getNextCursor();
				cursorPageable = CP.cursor(nextCursor, classSize);
				cursorPage = homeworkClassService.findEnableClass(cursorPageable);
			}

			long t2 = System.currentTimeMillis();
			logger.info("[diag init] -- 班级数据完成，耗时=" + ((t2 - t1) / 1000));
		} else {
			HomeworkClazz homeworkClazz = homeworkClassService.get(classId);

			try {
				staticDiagnosticService.initDiagnosticClass(homeworkClazz);
			} catch (Exception e) {
				logger.error("task class {} init has error {}", homeworkClazz.getId(), e);
			}
		}

		// 处理全国平均做题数
		diagnosticService.doDiagnostic();

		return new Value();
	}

	/**
	 * 跑每日任务.
	 * 
	 * @return
	 */
	@RequestMapping(value = "dayTask", method = { RequestMethod.GET, RequestMethod.POST })
	public Value dayTask(Long classId) {
		try {
			if (classId == null) {
				int CLASS_FETCH_SIZE = 200;
				CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, CLASS_FETCH_SIZE);
				CursorPage<Long, HomeworkClazz> cursorPage = homeworkClassService.findEnableClass(cursorPageable);
				// boolean init =
				// !diagnosticClassKnowpointService.hasClassKpData();

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
			} else {
				HomeworkClazz homeworkClazz = homeworkClassService.get(classId);
				try {
					diagnosticClassStudentService.doClassStudentRankStat(homeworkClazz.getId(), null);
				} catch (Exception e) {
					logger.error("static class {} has error {}", homeworkClazz.getId(), e);
				}
			}

			// 处理当天的全国平均做题数
			diagnosticService.doDiagnostic();
		} catch (Exception e) {
			logger.error("dayTask 执行-错误!" + e.getMessage(), e);
		}
		return new Value();
	}
}
