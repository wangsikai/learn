package com.lanking.uxb.service.database;

import java.util.Calendar;

public class ApiMonitorLogUtil {
	public static void main(String[] args) {
		String sql = "CREATE TABLE `api_monitor_log_$YEAR_$MONTH_$DAYOFMONTH` (`id` bigint(20) NOT NULL,`api` varchar(512) DEFAULT NULL,`cost_time` int(11) DEFAULT NULL,`create_at` datetime(3) DEFAULT NULL,`ex` longtext,`host_name` varchar(64) DEFAULT NULL,`params` longtext,`token` varchar(32) DEFAULT NULL,`user_id` bigint(20) DEFAULT NULL,PRIMARY KEY (`id`));";
		Calendar calendar = Calendar.getInstance();
		for (int i = 0; i < 365; i++) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			if (year > 2017) {
				break;
			}
			System.err.println(sql.replace("$YEAR", String.valueOf(year)).replace("$MONTH", String.valueOf(month))
					.replace("$DAYOFMONTH", String.valueOf(dayOfMonth)));
		}
	}
}
