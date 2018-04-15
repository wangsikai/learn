package com.lanking.cloud.domain.yoomath.smartExamPaper;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 智能试卷状态
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum SmartPaperStatus implements Valueable {
	/**
	 * 最新试卷
	 */
	NEWEST(0),
	/**
	 * 往期回顾试卷
	 */
	PREVIOUS(1),
	/**
	 * 往期回顾里面重新练,在智能试卷里面不显示，会进练习历史里面
	 */
	HIDE(2),
	/**
	 * 试卷删除
	 */
	DELETED(3);

	private final int value;

	private SmartPaperStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static SmartPaperStatus findByValue(int value) {
		switch (value) {
		case 0:
			return NEWEST;
		case 1:
			return PREVIOUS;
		case 2:
			return HIDE;
		case 3:
			return DELETED;
		default:
			return null;
		}
	}
}
