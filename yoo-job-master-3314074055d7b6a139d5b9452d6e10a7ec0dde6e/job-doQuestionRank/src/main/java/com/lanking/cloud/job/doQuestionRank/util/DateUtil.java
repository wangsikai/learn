package com.lanking.cloud.job.doQuestionRank.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class DateUtil {

	/**
	 * 根据查询条件取开始时间和结束时间
	 * 
	 * @param day
	 * @author peng.zhao
	 * @return map
	 */
	public static Map<String, Date> getNowTime(int day) {
		Date startDate = new Date();
		Date endDate = new Date();

		int week = LocalDate.now().getDayOfWeek().getValue();
		switch (day) {
		// 周
		case 7:
			startDate = Date.from(LocalDateTime.now().minusDays(week - 1).withHour(0).withMinute(0).withSecond(0)
					.withNano(0).atZone(ZoneId.systemDefault()).toInstant());
			endDate = Date.from(LocalDateTime.now().plusDays(7 - week).withHour(23).withMinute(59).withSecond(59)
					.withNano(0).atZone(ZoneId.systemDefault()).toInstant());
			break;
		// 月
		case 30:
			startDate = Date.from(LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).withHour(0)
					.withMinute(0).withSecond(0).withNano(0).atZone(ZoneId.systemDefault()).toInstant());
			endDate = Date.from(LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59)
					.withSecond(59).withNano(0).atZone(ZoneId.systemDefault()).toInstant());
			break;
		// 年
		case 365:
			startDate = Date.from(LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).withHour(0).withMinute(0)
					.withSecond(0).withNano(0).atZone(ZoneId.systemDefault()).toInstant());
			endDate = Date.from(LocalDateTime.now().with(TemporalAdjusters.lastDayOfYear()).withHour(23).withMinute(59)
					.withSecond(59).withNano(0).atZone(ZoneId.systemDefault()).toInstant());
			break;
		// 默认按周
		default:
			startDate = Date.from(LocalDateTime.now().minusDays(week - 1).withHour(0).withMinute(0).withSecond(0)
					.withNano(0).atZone(ZoneId.systemDefault()).toInstant());
			endDate = Date.from(LocalDateTime.now().plusDays(7 - week).withHour(23).withMinute(59).withSecond(59)
					.withNano(0).atZone(ZoneId.systemDefault()).toInstant());
			break;
		}

		Map<String, Date> data = new HashMap<>();
		data.put("startDate", startDate);
		data.put("endDate", endDate);
		return data;
	}

	/**
	 * 根据查询条件取开始时间和结束时间
	 * 
	 * @param day
	 * @author peng.zhao
	 * @return map
	 */
	public static Map<String, Integer> getNowTimeInteger(int day) {
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
