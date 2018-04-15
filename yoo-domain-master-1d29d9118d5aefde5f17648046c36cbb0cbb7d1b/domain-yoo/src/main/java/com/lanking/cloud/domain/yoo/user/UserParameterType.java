package com.lanking.cloud.domain.yoo.user;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 用户通用参数类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum UserParameterType implements Valueable {
	/**
	 * 引导<br>
	 * p0:首页引导<br>
	 * p1:作业引导<br>
	 * p2:班级引导
	 */
	GUIDE(0),
	/**
	 * 学生客户端使用<br>
	 * p0:邀请提醒<br>
	 * p1:寒假作业提醒<br>
	 */
	STUDENT_APP_USE(1),
	/**
	 * 学生WEB端使用<br>
	 * p0:给家长的一封信<br>
	 */
	STUDENT_WEB_USE(2);

	private final int value;

	private UserParameterType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static UserParameterType findByValue(int value) {
		switch (value) {
		case 0:
			return GUIDE;
		case 1:
			return STUDENT_APP_USE;
		case 2:
			return STUDENT_WEB_USE;
		default:
			return null;
		}
	}
}
