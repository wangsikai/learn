package com.lanking.uxb.zycon.homework.form;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 作业的过滤类型
 *
 * @author xinyu.zhou
 * @since yoomath V1.7
 */
public enum HomeworkQueryType implements Valueable {
	// 全部无过滤条件
	ALL(0),

	// 作业中无学生提交作业
	INIT(1),

	// 批改中，有学生已提交
	WORKING(2),

	// 已批改
	FINISH(3),

	// 移除状态下的数据
	REMOVE(4),

	// 已经下发的作业
	ISSUED(5);

	private int value;

	HomeworkQueryType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
