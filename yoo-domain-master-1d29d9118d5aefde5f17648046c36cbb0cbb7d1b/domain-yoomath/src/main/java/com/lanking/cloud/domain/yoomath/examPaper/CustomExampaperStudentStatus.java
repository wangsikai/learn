package com.lanking.cloud.domain.yoomath.examPaper;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 学生组卷状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum CustomExampaperStudentStatus implements Valueable {

	/**
	 * 开卷
	 */
	OPEN(0),

	/**
	 * 统计
	 */
	STATISTICS(1),
	/**
	 * 删除
	 */
	DELETE(2);

	private int value;

	CustomExampaperStudentStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static CustomExampaperStudentStatus findByValue(int value) {
		switch (value) {
		case 0:
			return OPEN;
		case 1:
			return STATISTICS;
		case 2:
			return DELETE;
		}
		return null;
	}
}
