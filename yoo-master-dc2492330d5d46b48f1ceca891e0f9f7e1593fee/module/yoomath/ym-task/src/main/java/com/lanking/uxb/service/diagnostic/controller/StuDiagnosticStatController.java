package com.lanking.uxb.service.diagnostic.controller;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.diagnostic.api.student.StaticStuDiagnosticClassKpService;
import com.lanking.uxb.service.diagnostic.api.student.StaticStuDiagnosticClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.api.student.TaskStuDiagnosticService;

@RestController
@RequestMapping(value = "task/s/diag")
public class StuDiagnosticStatController {
	@Autowired
	private TaskStuDiagnosticService stuDiagService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private StaticStuDiagnosticClassLatestHomeworkService sdService;

	@Autowired
	private StaticStuDiagnosticClassKpService staticStuDiagnosticClassKpService;

	/**
	 * 学生历史数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "data", method = { RequestMethod.GET, RequestMethod.POST })
	public Value data() {
		logger.error("-stuData--【开始统计】---");
		stuDiagService.taskStatStuClassKp(null, new Date());
		logger.error("-stuData--【结束统计】---");
		return new Value();
	}

	/**
	 * topn历史数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "topn", method = { RequestMethod.GET, RequestMethod.POST })
	public Value topn() {
		logger.error("-topn--【开始统计】---");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date endTime = calendar.getTime();
		// 今天凌晨之前有下发作业的班级，topn数据重新初始化
		stuDiagService.taskStatTopnStu(null, endTime);
		logger.error("-topn--【结束统计】---");
		return new Value();
	}

	/**
	 * 统计学生所有班级的初始化数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "stuAllClassStat", method = { RequestMethod.GET, RequestMethod.POST })
	public Value stuAllClassStat() {
		CursorPageable<Long> cursorPageable = CP.cursor(Long.MAX_VALUE, 200);
		CursorPage<Long, Long> cursorPage = staticStuDiagnosticClassKpService.getAllStudent(cursorPageable);
		while (CollectionUtils.isNotEmpty(cursorPage.getItems())) {
			try {
				sdService.initStuHkStatistic(cursorPage.getItems());
			} catch (Exception e) {
				logger.info("stuAllClassStat error", e);
			}
			Long nextCursor = cursorPage.getNextCursor();
			cursorPageable = CP.cursor(nextCursor, 200);
			cursorPage = staticStuDiagnosticClassKpService.getAllStudent(cursorPageable);
		}
		return new Value();
	}
}
