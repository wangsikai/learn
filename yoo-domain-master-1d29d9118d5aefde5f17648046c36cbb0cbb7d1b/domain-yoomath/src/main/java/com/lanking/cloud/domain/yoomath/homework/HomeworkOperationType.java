package com.lanking.cloud.domain.yoomath.homework;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 作业操作类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum HomeworkOperationType implements Valueable {
	/**
	 * 截止时间一小时前推送消息给学生客户端
	 */
	PUSH2STUDENT_BEFORE1HOUR(0),
	/**
	 * 截止时间30分钟前，若还有学生未提交作业，推送数据给教师端
	 */
	PUSH2TEACHER_DELAYHKDEADLINE(1);

	private int value;

	private HomeworkOperationType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}
}
