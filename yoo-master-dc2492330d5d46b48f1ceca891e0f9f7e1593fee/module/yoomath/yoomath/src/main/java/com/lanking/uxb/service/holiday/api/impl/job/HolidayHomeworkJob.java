package com.lanking.uxb.service.holiday.api.impl.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.api.impl.HolidayStuPublishService;
import com.lanking.uxb.service.honor.api.CoinsService;

/**
 * 假期作业定时监听类
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public class HolidayHomeworkJob implements SimpleJob {

	private static final Logger logger = LoggerFactory.getLogger(HolidayHomeworkJob.class);

	private static final int SIZE = 500;
	@Autowired
	private HolidayStuPublishService holidayStuPublishService;
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;
	@Autowired
	private CoinsService coinsService;

	/**
	 * 定时监听任务方法
	 */
	@Override
	public void execute(ShardingContext shardingContext) {
		publishHomework();
		finishHomework();
	}

	/**
	 * 查询待布置的假期作业。
	 */
	public void publishHomework() {
		Date now = new Date();
		CursorPage<Long, HolidayHomework> page = holidayHomeworkService.queryNotPublishHomework(now,
				CP.cursor(Long.MAX_VALUE, SIZE));
		while (page.isNotEmpty()) {
			for (HolidayHomework h : page.getItems()) {
				logger.info("auto publish holidayhomework start, {}.", JSON.toJSONString(h));

				holidayStuPublishService.publish(h);

				logger.info("auto publish holidayhomework end.");
			}

			page = holidayHomeworkService.queryNotPublishHomework(now, CP.cursor(page.getLast().getId(), SIZE));
		}
	}

	/**
	 * 更新已经到期的作业为NOT_ISSUED
	 */
	public void finishHomework() {
		Date now = new Date();
		CursorPage<Long, HolidayHomework> page = holidayHomeworkService.queryAfterDeadline(now,
				CP.cursor(Long.MAX_VALUE, SIZE));
		while (page.isNotEmpty()) {
			for (HolidayHomework h : page.getItems()) {
				// holidayHomeworkService.updateAfterDeadLine(h.getId());
				holidayHomeworkService.updateStatus(h.getId(), HomeworkStatus.NOT_ISSUE);
				coinsService.earn(CoinsAction.TEA_HOLIDAY_HOMEWORK_RESULT, h.getCreateId(), -1, Biz.HOLIDAY_HOMEWORK,
						h.getId());
			}

			page = holidayHomeworkService.queryAfterDeadline(now, CP.cursor(page.getLast().getId(), SIZE));
		}
	}

}
