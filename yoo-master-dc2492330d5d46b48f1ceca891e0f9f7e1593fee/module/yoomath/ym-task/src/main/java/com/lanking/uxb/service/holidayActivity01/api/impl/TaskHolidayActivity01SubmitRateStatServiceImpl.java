package com.lanking.uxb.service.holidayActivity01.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.uxb.service.holidayActivity01.api.StatHolidayActivity01SubmitRateStatService;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01SubmitRateStatService;

@Service
public class TaskHolidayActivity01SubmitRateStatServiceImpl implements TaskHolidayActivity01SubmitRateStatService {

	@Autowired
	private StatHolidayActivity01SubmitRateStatService statService;

	private static final int FETCH_SIZE = 50;

	private static final Logger logger = LoggerFactory.getLogger(TaskHolidayActivity01SubmitRateStatServiceImpl.class);

	@Override
	public void updateHkSubmitRate() {
		logger.info("---stat homework and class submitRate start---");
		CursorPage<Long, Long> data = statService.findHolidayActivity01HkList(CP.cursor(Long.MAX_VALUE, FETCH_SIZE));
		while (data.isNotEmpty()) {
			statService.statHkSubmitRate(data.getItems());
			data = statService.findHolidayActivity01HkList(CP.cursor(data.getNextCursor(), FETCH_SIZE));
		}
		logger.info("---stat homework and class submitRate end---");
	}

	@Override
	public void updateClassSubmitRate() {
		logger.info("---stat class submitRate start---");
		CursorPage<Long, Long> data = statService.findClassList(CP.cursor(Long.MAX_VALUE, FETCH_SIZE));
		while (data.isNotEmpty()) {
			statService.statClassSubmitRate(data.getItems());
			data = statService.findClassList(CP.cursor(data.getNextCursor(), FETCH_SIZE));
		}
		logger.info("---stat class submitRate end---");
	}

	@Override
	public void updateClassUserSubmitRate() {
		logger.info("---stat user submitRate start---");
		CursorPage<Long, Long> data = statService.findClassUserList(CP.cursor(Long.MAX_VALUE, FETCH_SIZE));
		while (data.isNotEmpty()) {
			statService.statClassUserSubmitRate(data.getItems());
			data = statService.findClassUserList(CP.cursor(data.getNextCursor(), FETCH_SIZE));
		}
		logger.info("---stat user submitRate end---");
	}

}
