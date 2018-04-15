package com.lanking.uxb.service.code.util;

import java.util.Calendar;
import java.util.Date;

public class TeachUtil {

	public static String getTeachYear(Date workAt) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(workAt);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		Calendar currentCalendar = Calendar.getInstance();
		int currentYear = currentCalendar.get(Calendar.YEAR);
		int currentMonth = currentCalendar.get(Calendar.MONTH) + 1;
		int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
		if (currentYear <= year) {
			return "<5年";
		} else {
			int teachYear = currentYear - year;
			if (currentMonth > month || (currentMonth == month && currentDay >= day)) {
				teachYear += 1;
			}
			if (teachYear <= 5) {
				return "<5年";
			} else if (teachYear > 5 && teachYear <= 10) {
				return "5-10年";
			} else if (teachYear >= 10 && teachYear <= 15) {
				return "10-15年";
			} else {
				return ">15年";
			}
		}
	}
}
