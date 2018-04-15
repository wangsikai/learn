package com.lanking.uxb.service.holiday.form;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;

/**
 * 学生假期作业form
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public class HolidayStuHomeworkPublishForm {
	private HolidayHomeworkType type;
	private long studentId;
	private long holidayHomeworkId;
	private int itemCount;

	public HolidayHomeworkType getType() {
		return type;
	}

	public void setType(HolidayHomeworkType type) {
		this.type = type;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getHolidayHomeworkId() {
		return holidayHomeworkId;
	}

	public void setHolidayHomeworkId(long holidayHomeworkId) {
		this.holidayHomeworkId = holidayHomeworkId;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
}
