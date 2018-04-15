package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 科举考试流程--给用户端看的时间<br>
 * 答题阶段是开始时间,批改下发阶段是结束时间
 * 
 * @since 3.9.4
 * @author senhao.wang
 * @version 2017年3月30日
 */
public enum ImperialExaminationUserProcess implements Valueable {
	/**
	 * 报名
	 */
	USER_PROCESS_SIGNUP(1, 0, "报名"),
	/**
	 * 乡试开始时间--乡试答题阶段
	 */
	USER_PROCESS_PROVINCIAL1(2, 1, "乡试答题阶段"),
	/**
	 * 乡试结束时间-- 乡试批改下发阶段
	 */
	USER_PROCESS_PROVINCIAL2(3, 1, "乡试批改下发阶段"),
	/**
	 * 会试开始时间--会试答题阶段
	 */
	USER_PROCESS_METROPOLITAN1(4, 2, "会试答题阶段"),
	/**
	 * 会试结束时间-- 会试批改下发阶段
	 */
	USER_PROCESS_METROPOLITAN2(5, 2, "会试批改下发阶段"),
	/**
	 * 殿试开始时间--殿试答题阶段
	 */
	USER_PROCESS_EXAMINATION1(6, 3, "殿试答题阶段"),
	/**
	 * 殿试结束时间-- 殿试批改下发阶段
	 */
	USER_PROCESS_EXAMINATION2(7, 3, "殿试批改下发阶段");

	private int value;
	private int parentId;
	private String name;

	ImperialExaminationUserProcess(int value, int parentId, String name) {
		this.value = value;
		this.parentId = parentId;
		this.name = name;
	}

	@Override
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public int getParentId() {
		return parentId;
	}

}
