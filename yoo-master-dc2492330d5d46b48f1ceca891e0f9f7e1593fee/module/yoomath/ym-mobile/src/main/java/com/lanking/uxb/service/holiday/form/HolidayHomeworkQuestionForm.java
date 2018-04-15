package com.lanking.uxb.service.holiday.form;

import com.lanking.uxb.service.base.form.AbstractHomeworkQuestionForm;

/**
 * 客户端保存寒假作业答案参数
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年01月13日
 */
public class HolidayHomeworkQuestionForm extends AbstractHomeworkQuestionForm {

	// 作业ID
	private long holidayHkItemId;
	// 学生作业ID
	private long holidayStuHkItemId;
	// 学生题目ID
	private long holidayStuHkItemQuestionId;

	public long getHolidayHkItemId() {
		return holidayHkItemId;
	}

	public void setHolidayHkItemId(long holidayHkItemId) {
		this.holidayHkItemId = holidayHkItemId;
	}

	public long getHolidayStuHkItemId() {
		return holidayStuHkItemId;
	}

	public void setHolidayStuHkItemId(long holidayStuHkItemId) {
		this.holidayStuHkItemId = holidayStuHkItemId;
	}

	public long getHolidayStuHkItemQuestionId() {
		return holidayStuHkItemQuestionId;
	}

	public void setHolidayStuHkItemQuestionId(long holidayStuHkItemQuestionId) {
		this.holidayStuHkItemQuestionId = holidayStuHkItemQuestionId;
	}

}
