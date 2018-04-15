package com.lanking.uxb.service.holidayActivity01.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Homework;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01HomeWorkPublishService;
import com.lanking.uxb.service.holidayActivity01.api.TaskHolidayActivity01HomeWorkService;

@Service
public class TaskHolidayActivity01HomeWorkPublishServiceImpl implements TaskHolidayActivity01HomeWorkPublishService {

	private static final int FETCH_SIZE = 20;

	@Autowired
	private TaskHolidayActivity01HomeWorkService holidayActivity01HomeWorkService;

	@Override
	public void publish(Long activityCode) {
		CursorPage<Long, HolidayActivity01Homework> data = holidayActivity01HomeWorkService
				.findHolidayActivity01HkList(activityCode, CP.cursor(Long.MAX_VALUE, FETCH_SIZE));
		while (data.isNotEmpty()) {
			// 练习id
			System.out.println("开始时间" + System.currentTimeMillis());
			holidayActivity01HomeWorkService.publish(data.getItems());
			System.out.println("结束时间" + System.currentTimeMillis());
			// 获取下一页
			data = holidayActivity01HomeWorkService.findHolidayActivity01HkList(activityCode,
					CP.cursor(data.getNextCursor(), FETCH_SIZE));
		}
	}

}
