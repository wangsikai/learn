 package com.lanking.uxb.service.holidayActivity02.job;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.holidayActivity02.api.TaskHolidayActivity02PowerRankService;
import com.lanking.uxb.service.holidayActivity02.api.TaskHolidayActivity02Service;
import com.lanking.uxb.service.holidayActivity02.api.TaskHolidayActivity02WeekPowerRankService;

public class HolidayActivity02Job implements SimpleJob {
	private Logger logger = LoggerFactory.getLogger(HolidayActivity02Job.class);
	@Autowired
	private TaskHolidayActivity02Service taskHolidayActivity02Service;
	@Autowired
	private TaskHolidayActivity02PowerRankService taskHolidayActivity02PowerRankService;
	@Autowired
	private TaskHolidayActivity02WeekPowerRankService taskHolidayActivity02WeekPowerRankService;

	@Override
	public void execute(ShardingContext shardingContext) {

		List<HolidayActivity02> allActivitys = taskHolidayActivity02Service
				.listAllProcessingActivity();
		if (CollectionUtils.isNotEmpty(allActivitys)) {
			for (HolidayActivity02 activity : allActivitys) {
				try {
					logger.info("---HolidayActivity02Job---start---");
					Date currentTime = new Date();
					if(currentTime.after(activity.getStartTime()) && currentTime.before(activity.getEndTime())) {
						long start = System.currentTimeMillis();
						logger.info("---HolidayActivity02Job---start--- time " + start);
						taskHolidayActivity02PowerRankService.processRank(activity);
						taskHolidayActivity02WeekPowerRankService.processRank(activity);
						long end = System.currentTimeMillis();
						logger.info("---HolidayActivity02Job---end--- time " + end);
						
						logger.info("---HolidayActivity02Job---end--- costtime " + (end-start));
					}
					logger.info("---HolidayActivity02Job---end---");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("process activity:{} fail", JSONObject.toJSON(activity));
				}
			}
		}
	}

}
