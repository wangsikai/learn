package com.lanking.uxb.service.data.api.impl.job;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.api.impl.HolidayHomeworkCalculateService;
import com.lanking.uxb.service.holiday.api.impl.HolidayHomeworkRankService;

/**
 * 假期作业统计定时
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public class YoomathHolidayHomeworkJob implements SimpleJob {

	private static Logger logger = LoggerFactory.getLogger(YoomathHolidayHomeworkJob.class);

	// 学生专项正确率统计每次查询条数
	private static final int HOLIDAY_STU_HOMEWORK_ITEM_FETCH_SIZE = 200;

	@Autowired
	private HolidayStuHomeworkItemService holidayStuHomeworkItemService;
	@Autowired
	private HolidayHomeworkCalculateService holidayHomeworkCalculateService;
	@Autowired
	private HolidayHomeworkRankService holidayHomeworkRankService;

	@Override
	public void execute(ShardingContext shardingContext) {
		Collection<Long> hkIds = calculate();
		rank(hkIds);

	}

	/**
	 * 计算正确率
	 *
	 * 以学生专项为入口计算单位，筛选条件符合如下几点： 1. 专项没有计算出正确率。 2. 专项中的题目全部完成。 3.
	 * 专项中的题目全部经过人工批改过。
	 */
	public Collection<Long> calculate() {
		Set<Long> hkIds = Sets.newHashSet();
		CursorPage<Long, HolidayStuHomeworkItem> page = holidayStuHomeworkItemService
				.queryNotCalculate(CP.cursor(Long.MAX_VALUE, HOLIDAY_STU_HOMEWORK_ITEM_FETCH_SIZE));
		while (page.isNotEmpty()) {
			List<HolidayStuHomeworkItem> stuItems = page.getItems();
			logger.info("begin calculate {} right rate ", JSON.toJSONString(stuItems));
			Map<String, Collection<Long>> retMap = holidayHomeworkCalculateService.calculateStuItems(stuItems);
			hkIds.addAll(retMap.get("hkIds"));
			logger.info("end calculate right rate");

			page = holidayStuHomeworkItemService.queryNotCalculate(CP.cursor(page.getLast().getId()));
		}

		return hkIds;

	}

	/**
	 * 计算排名 1. 学生各专项在班级中的排名。 2. 学生假期作业在班级中排名。
	 */
	public void rank(Collection<Long> hkIds) {
		for (Long hkId : hkIds) {
			logger.info("begin calculate {} holiday homework rank.", hkId);
			holidayHomeworkRankService.calculateStuHomeworkItemRank(hkId);
			logger.info("end calculate {} holiday homework rank.", hkId);
		}

	}

}
