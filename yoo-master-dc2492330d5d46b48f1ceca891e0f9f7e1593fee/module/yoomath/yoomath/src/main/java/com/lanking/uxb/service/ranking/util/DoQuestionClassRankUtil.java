package com.lanking.uxb.service.ranking.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

/**
 * 排行榜工具类
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月20日
 */
public final class DoQuestionClassRankUtil {

	/**
	 * 根据查询条件取开始时间和结束时间
	 * 
	 * @param day
	 * @author peng.zhao
	 * @return map
	 */
	public static Map<String, Integer> getNowTime(int day) {
		int startDate = 0;
		int endDate = 0;

		int week = LocalDate.now().getDayOfWeek().getValue();
		switch (day) {
		// 周
		case 7:
			startDate = Integer.parseInt(LocalDate.now().minusDays(week - 1).toString().replaceAll("-", ""));
			endDate = Integer.parseInt(LocalDate.now().plusDays(7 - week).toString().replaceAll("-", ""));
			break;
		// 月
		case 30:
			startDate = Integer
					.parseInt(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).toString().replaceAll("-", ""));
			endDate = Integer
					.parseInt(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).toString().replaceAll("-", ""));
			break;
		// 年
		case 365:
			startDate = Integer
					.parseInt(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).toString().replaceAll("-", ""));
			endDate = Integer
					.parseInt(LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).toString().replaceAll("-", ""));
			break;
		// 默认按周
		default:
			startDate = Integer.parseInt(LocalDate.now().minusDays(week - 1).toString().replaceAll("-", ""));
			endDate = Integer.parseInt(LocalDate.now().plusDays(7 - week).toString().replaceAll("-", ""));
			break;
		}

		Map<String, Integer> data = new HashMap<>();
		data.put("startDate", startDate);
		data.put("endDate", endDate);
		return data;
	}
}
