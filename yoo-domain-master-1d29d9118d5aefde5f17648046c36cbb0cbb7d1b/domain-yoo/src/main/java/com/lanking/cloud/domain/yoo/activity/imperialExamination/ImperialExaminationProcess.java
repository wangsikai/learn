package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 科举考试流程
 * 
 * @since 3.9.4
 * @author senhao.wang
 * @version 2017年3月30日
 */
public enum ImperialExaminationProcess implements Valueable {
	/**
	 * 报名
	 */
	PROCESS_SIGNUP(1, 0, "报名"),
	/**
	 * 乡试准备阶段<br>
	 * 1.推送通知已报名教师<br>
	 * 2.作业自动分发给学生
	 */
	PROCESS_PROVINCIAL1(2, 1, "乡试准备阶段"),
	/**
	 * 乡试答题阶段<br>
	 * 1.学生开始答题
	 */
	PROCESS_PROVINCIAL2(3, 1, "乡试答题阶段"),
	/**
	 * 乡试批改下发阶段<br>
	 * 1.作业截止<br>
	 * 2.作业自动提交自动批改 <br>
	 * 3.部分未批改出来或者学生有提交图片的，后台手动批改
	 */
	PROCESS_PROVINCIAL3(4, 1, "乡试批改下发阶段"),
	/**
	 * 乡试成绩调整阶段<br>
	 * 1.至少下发一份作业,否则成绩作废 <br>
	 * 2.后台可以介入修改分数 <br>
	 * 3.结束时更新最新成绩、最新排名
	 */
	PROCESS_PROVINCIAL4(5, 1, "乡试成绩调整阶段"),
	/**
	 * 乡试公布成绩阶段
	 */
	PROCESS_PROVINCIAL5(6, 1, "乡试公布成绩阶段"),
	/**
	 * 会试
	 */
	PROCESS_METROPOLITAN1(7, 2, "会试准备阶段"),
	/**
	 * 
	 */
	PROCESS_METROPOLITAN2(8, 2, "会试答题阶段"),
	/**
	 * 
	 */
	PROCESS_METROPOLITAN3(9, 2, "会试批改下发阶段"),
	/**
	 * 
	 */
	PROCESS_METROPOLITAN4(10, 2, "会试成绩调整"),
	/**
	 * 
	 */
	PROCESS_METROPOLITAN5(11, 2, "会试公布成绩阶段"),
	/**
	 * 殿试
	 */
	PROCESS_FINAL_IMPERIAL1(12, 3, "殿试准备阶段"),
	/**
	 * 
	 */
	PROCESS_FINAL_IMPERIAL2(13, 3, "殿试答题阶段"),
	/**
	 * 
	 */
	PROCESS_FINAL_IMPERIAL3(14, 3, "殿试批改下发阶段"),
	/**
	 * 
	 */
	PROCESS_FINAL_IMPERIAL4(15, 3, "殿试成绩调整"),
	/**
	 * 
	 */
	PROCESS_FINAL_IMPERIAL5(16, 3, "殿试公布成绩阶段"),
	/**
	 * 总排名调整
	 */
	PROCESS_TOTALRANKING(17, 4, "总排名调整"),
	/**
	 * 颁奖
	 */
	PROCESS_AWARDS(18, 5, "颁奖");

	private int value;
	private int parentId;
	private String name;

	ImperialExaminationProcess(int value, int parentId, String name) {
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
