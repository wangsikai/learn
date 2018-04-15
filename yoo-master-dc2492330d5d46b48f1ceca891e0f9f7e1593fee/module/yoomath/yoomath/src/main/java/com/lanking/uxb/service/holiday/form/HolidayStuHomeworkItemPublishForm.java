package com.lanking.uxb.service.holiday.form;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkType;

/**
 * 学生专项布置form
 *
 * @author xinyu.zhou
 * @since yoomath V1.9
 */
public class HolidayStuHomeworkItemPublishForm {
	private long studentId;
	private HolidayHomeworkType type;
	private long holidayHomeworkId;
	private long holidayHomeworkItemId;
	private long holidayStuHomeworkId;

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public HolidayHomeworkType getType() {
		return type;
	}

	public void setType(HolidayHomeworkType type) {
		this.type = type;
	}

	public long getHolidayHomeworkId() {
		return holidayHomeworkId;
	}

	public void setHolidayHomeworkId(long holidayHomeworkId) {
		this.holidayHomeworkId = holidayHomeworkId;
	}

	public long getHolidayHomeworkItemId() {
		return holidayHomeworkItemId;
	}

	public void setHolidayHomeworkItemId(long holidayHomeworkItemId) {
		this.holidayHomeworkItemId = holidayHomeworkItemId;
	}

	public long getHolidayStuHomeworkId() {
		return holidayStuHomeworkId;
	}

	public void setHolidayStuHomeworkId(long holidayStuHomeworkId) {
		this.holidayStuHomeworkId = holidayStuHomeworkId;
	}
}
