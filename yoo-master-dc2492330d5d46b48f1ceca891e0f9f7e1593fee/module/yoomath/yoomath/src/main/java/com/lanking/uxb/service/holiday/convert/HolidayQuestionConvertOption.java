package com.lanking.uxb.service.holiday.convert;

import com.lanking.uxb.service.common.convert.QuestionBaseConvertOption;

/**
 * 题目VO转换选项.
 * 
 * @since yoomath V 1.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月9日
 */
public class HolidayQuestionConvertOption extends QuestionBaseConvertOption {
	/**
	 * 假期学生作业专项Id
	 */
	private Long holidayStuHomeworkItemId;
	/**
	 * 假期作业专项ID
	 */
	private Long holidayHomeworkItemId;

	public HolidayQuestionConvertOption() {
		super();
	}

	public HolidayQuestionConvertOption(Long holidayStuHomeworkItemId, Long holidayHomeworkItemId) {
		super();
		this.holidayStuHomeworkItemId = holidayStuHomeworkItemId;
		this.holidayHomeworkItemId = holidayHomeworkItemId;
	}

	public HolidayQuestionConvertOption(Long holidayStuHomeworkItemId) {
		super();
		this.holidayStuHomeworkItemId = holidayStuHomeworkItemId;
	}

	public Long getHolidayStuHomeworkItemId() {
		return holidayStuHomeworkItemId;
	}

	public void setHolidayStuHomeworkItemId(Long holidayStuHomeworkItemId) {
		this.holidayStuHomeworkItemId = holidayStuHomeworkItemId;
	}

	public Long getHolidayHomeworkItemId() {
		return holidayHomeworkItemId;
	}

	public void setHolidayHomeworkItemId(Long holidayHomeworkItemId) {
		this.holidayHomeworkItemId = holidayHomeworkItemId;
	}

}
