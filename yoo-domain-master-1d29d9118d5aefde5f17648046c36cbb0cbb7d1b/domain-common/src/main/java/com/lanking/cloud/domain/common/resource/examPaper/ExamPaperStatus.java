package com.lanking.cloud.domain.common.resource.examPaper;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 试卷状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public enum ExamPaperStatus implements Valueable {

	/**
	 * 编辑中/录入中.
	 */
	EDITING(0),

	/**
	 * 未校验/待发布.
	 */
	NOCHECK(1),

	/**
	 * 已通过/已发布.
	 */
	PASS(2),

	/**
	 * 未通过.
	 */
	NOPASS(3),

	/**
	 * 禁用.
	 */
	DISABLED(4),

	/**
	 * 删除.
	 */
	DELETED(5);

	private int value;

	ExamPaperStatus(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

	public static ExamPaperStatus findByValue(int value) {
		switch (value) {
		case 0:
			return EDITING;
		case 1:
			return NOCHECK;
		case 2:
			return PASS;
		case 3:
			return NOPASS;
		case 4:
			return DISABLED;
		case 5:
			return DELETED;
		}

		return null;
	}
}
